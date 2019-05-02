package br.edu.ifsul.vendas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.adapter.ProdutosAdapter;
import br.edu.ifsul.vendas.barcode.BarcodeCaptureActivity;
import br.edu.ifsul.vendas.model.Produto;
import br.edu.ifsul.vendas.setup.AppSetup;

public class ProdutosActivity extends AppCompatActivity {

    private static final String TAG = "produtosactivity";
    private static final int RC_BARCODE_CAPTURE = 1;
    private ListView lvProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        lvProdutos = findViewById(R.id.lv_produtos);
        lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProdutosActivity.this, ProdutoDetalheActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("produtos");

        // Read from the database
        myRef.orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "Value is: " + dataSnapshot.getValue());

                AppSetup.produtos = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Produto produto = ds.getValue(Produto.class);
                    produto.setKey(ds.getKey());
                    AppSetup.produtos.add(produto);
                }

                //carrega os dados na View
                lvProdutos.setAdapter(new ProdutosAdapter(ProdutosActivity.this, AppSetup.produtos));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_produtos, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuitem_pesquisar).getActionView();
        searchView.setQueryHint(getString(R.string.hint_nome_searchview));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Produto> produtosTemp = new ArrayList<>();
                for (Produto produto : AppSetup.produtos) {
                    if (produto.getNome().toLowerCase().contains(newText.toLowerCase())) {
                        produtosTemp.add(produto);
                    }
                }
                lvProdutos.setAdapter(new ProdutosAdapter(ProdutosActivity.this, produtosTemp));
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_barcode:
                Intent intent = new Intent(ProdutosActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                    //localiza o produto na lista (ou não)
                    boolean flag = true;
                    int position = 0;
                    for (Produto produto : AppSetup.produtos) {
                        if (String.valueOf(produto.getCodigoDeBarras()).equals(barcode.displayValue)) {
                            flag = false;
                            Intent intent = new Intent(ProdutosActivity.this, ProdutoDetalheActivity.class);
                            intent.putExtra("position", position);
                            startActivity(intent);
                            break;
                        }
                        position++;
                    }
                    if (flag) {
                        Snackbar.make(findViewById(R.id.container_activity_produtos), "codigo de barras não cadastrado", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Falha na leitura do código", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(this, String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}


package br.edu.ifsul.vendas.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.adapter.PedidosAdapter;
import br.edu.ifsul.vendas.barcode.BarcodeCaptureActivity;
import br.edu.ifsul.vendas.model.Pedido;
import br.edu.ifsul.vendas.setup.AppSetup;

public class PedidosActivity extends AppCompatActivity {
    private ListView lv_pedidos;
    private static final String TAG = "pedidosactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        //ativa o botão home na actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        lv_pedidos = findViewById(R.id.lv_pedidos);
        lv_pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("clientes" + AppSetup.cliente.getKey() + "pedidos");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Pedido pedido = ds.getValue(Pedido.class);
                    pedido.setKey(ds.getKey());
                    AppSetup.pedidos.add(pedido);
                }

                //carrega os dados na View
                lv_pedidos.setAdapter(new PedidosAdapter(PedidosActivity.this, AppSetup.pedidos));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}

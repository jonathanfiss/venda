package br.edu.ifsul.vendas.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.adapter.CarrinhoAdapter;
import br.edu.ifsul.vendas.adapter.ProdutosAdapter;
import br.edu.ifsul.vendas.model.ItemPedido;
import br.edu.ifsul.vendas.model.Produto;
import br.edu.ifsul.vendas.setup.AppSetup;


public class CarrinhoActivity extends AppCompatActivity {

    private ListView lv_carrinho;
    private double total;
    private static final String TAG = "carrinhactivity";
    private Produto produto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_carrinho);
        TextView tvTotalPedidoCarrinho = findViewById(R.id.tvTotalPedidoCarrinho);
        TextView tvClienteCarinho = findViewById(R.id.tvClienteCarrinho);

        lv_carrinho = findViewById(R.id.lv_carrinho);
        atualizaView();
        lv_carrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editaItem(position);

            }
        });
        lv_carrinho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("produtos/" + AppSetup.produtos.get(position).getKey() + "/quantidade");


                myRef.setValue(AppSetup.produtos.get(position).getQuantidade() + AppSetup.carrinho.get(position).getQuantidade());
                for (ItemPedido item : AppSetup.carrinho) {
                    if (item.getProduto().equals(AppSetup.carrinho.get(position).getProduto())){
                        Log.d("forfor1", item.getProduto().toString());
                    }else{
                        AppSetup.produtos.add(item.getProduto());

                    }


//                    ItemPedido item = new ItemPedido();
//                    item.setProduto(produto);
//                    item.setQuantidade(quantidade);
//                    item.setTotalItem(quantidade * produto.getValor());
//                    item.setSituacao(true);
//                    AppSetup.carrinho.add(item);

                }
                Log.d("abc",AppSetup.produtos.toString());
//                AppSetup.carrinho.get(position).remove(AppSetup.carrinho.get(position).getProduto());
                atualizaView();
                Log.d("forfor2",AppSetup.carrinho.get(position).toString());
                return false;
            }
        });
        tvTotalPedidoCarrinho.setText(String.valueOf(total));
        tvClienteCarinho.setText(String.valueOf(AppSetup.cliente.getNome().concat(" " + AppSetup.cliente.getSobrenome())));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_carrinho, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_salvar:
                confirmaSalvar();
                break;
            case R.id.menuitem_cancelar:
                confirmaCancelar();
                break;
        }
        return true;
    }

    private void editaItem(int position) {

    }

    private void confirmaCancelar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.message_confirma_cancelar);

        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppSetup.carrinho.clear();
                finish();
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void confirmaSalvar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.message_confirma_salvar);
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void atualizaView() {
        lv_carrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this, AppSetup.carrinho));
        total = 0;
        for (ItemPedido itemPedido : AppSetup.carrinho) {
            total = total + itemPedido.getTotalItem();
        }
    }
}

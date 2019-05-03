package br.edu.ifsul.vendas.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.concurrent.atomic.AtomicMarkableReference;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_carrinho);
        TextView tvClienteCarinho = findViewById(R.id.tvClienteCarrinho);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("produtos");

        // Read from the database
        myRef.orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                AppSetup.produtos = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Produto produto = ds.getValue(Produto.class);
                    produto.setKey(ds.getKey());
                    AppSetup.produtos.add(produto);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

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
                excluiItem(position);
                return false;
            }
        });

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confimar);
        builder.setMessage("Você tem certeza que deseja editar esse item?");
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CarrinhoActivity.this, ProdutosActivity.class);
//                intent.putExtra();passar o valor para o produtosactivity
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void excluiItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confimar);
        builder.setMessage("Você tem certeza que deseja excluir esse item?");
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem(position);
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void confirmaCancelar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.message_confirma_cancelar);

        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppSetup.carrinho.clear();
                AppSetup.cliente = null;
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
                if (AppSetup.carrinho== null){
                    Toast.makeText(CarrinhoActivity.this, "Você não possui nenhum produto, que possa ser salvo", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference("pedidos");
                    myRef.setValue(AppSetup.carrinho);//feita a alteração do dado no firebase
                }
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void removeItem(int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("produtos/" + AppSetup.carrinho.get(position).getProduto().getKey() + "/quantidade");
        myRef.setValue(AppSetup.produtos.get(position).getQuantidade() + AppSetup.carrinho.get(position).getQuantidade());
        AppSetup.carrinho.remove(position);
        Log.d("item", "item removido");
        atualizaView();
        Toast.makeText(CarrinhoActivity.this, "Produto removido com sucesso!", Toast.LENGTH_SHORT).show();

    }

    public void atualizaView() {
        TextView tvTotalPedidoCarrinho = findViewById(R.id.tvTotalPedidoCarrinho);
        lv_carrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this, AppSetup.carrinho));
        total = 0;
        for (ItemPedido itemPedido : AppSetup.carrinho) {
            total = total + itemPedido.getTotalItem();
        }
        tvTotalPedidoCarrinho.setText(String.valueOf(total));
    }
}
// criar get e set index
// armazenar o tamanho do size
// para depois mandar a intent putextra(index)
//
//atualizaEstoque
//final databasereference myref =data
//altera banco myref(item.getproduto().getkey().child("atualiza o campo filho").setvalue(valor);
//remove item
//e chama atualiza view

//produto.setindex(Appsetup.produtos.size());

//cancela pedido
//for nos itens appsetup.carrinho
//set no banco de dados cada item
//para voltar o estado com valores sem modificação
//
//salvar no banco
//salva cliente
//datacriaçã
//datamodificação
//estado"aberto/fechado"
//formade pagamento avista/prazo
//itens salva itens
//key
//situaçao true
//totalpedido
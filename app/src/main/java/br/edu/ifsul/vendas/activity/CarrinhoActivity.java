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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Date;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.adapter.CarrinhoAdapter;
import br.edu.ifsul.vendas.model.ItemPedido;
import br.edu.ifsul.vendas.model.Pedido;
import br.edu.ifsul.vendas.setup.AppSetup;


public class CarrinhoActivity extends AppCompatActivity {

    private ListView lv_carrinho;
    private double total;
    //    private static final String TAG = "carrinhactivity";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_carrinho);
        TextView tvClienteCarinho = findViewById(R.id.tvClienteCarrinho);
        tvClienteCarinho.setText(String.valueOf(AppSetup.cliente.getNome().concat(" " + AppSetup.cliente.getSobrenome())));
        lv_carrinho = findViewById(R.id.lv_carrinho);

        atualizaView();
        lv_carrinho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                excluiItem(position);
                return false;
            }
        });

        lv_carrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editaItem(position);
            }
        });

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
            case R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void editaItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.mensagens_edita);
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(CarrinhoActivity.this, ProdutoDetalheActivity.class);
                intent.putExtra("position", AppSetup.carrinho.get(position).getProduto().getIndex());
                atualizaEstoque(position);
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
        builder.setMessage(R.string.mensagem_exclui);
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                atualizaEstoque(position);
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
                Log.d("tamanho", String.valueOf(AppSetup.carrinho.size()));
                for (ItemPedido item : AppSetup.carrinho) {
                    DatabaseReference myRef = database.getReference("produtos/" + item.getProduto().getKey() + "/quantidade");
                    myRef.setValue(item.getQuantidade() + item.getProduto().getQuantidade());
                    Log.d("removido", item.toString());
                    Log.d("item", "item removido");
                }
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
                if (AppSetup.carrinho == null) {
                    Toast.makeText(CarrinhoActivity.this, getString(R.string.carrinho_vazio), Toast.LENGTH_SHORT).show();
                } else {
                    Date dataHoraAtual = new Date();

                    DatabaseReference myRef = database.getReference("pedidos");
                    String key = myRef.push().getKey();

                    Pedido pedido = new Pedido();
                    pedido.setCliente(AppSetup.cliente);
                    pedido.setDataCriacao(dataHoraAtual);
                    pedido.setDataModificacao(dataHoraAtual);
                    pedido.setEstado("aberto");
                    pedido.setFormaDePagamento("avista");
                    pedido.setItens(AppSetup.carrinho);
//                    pedido.setKey(Long.valueOf(key));
                    pedido.setSituacao(true);
                    pedido.setTotalPedido(total);

                    myRef.child(key).setValue(pedido);

                    AppSetup.cliente = null;
                    AppSetup.carrinho.clear();
                    AppSetup.pedido = null;
                    finish();
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

    public void atualizaEstoque(int position) {
        DatabaseReference myRef = database.getReference("produtos/" + AppSetup.carrinho.get(position).getProduto().getKey() + "/quantidade");
        myRef.setValue(AppSetup.carrinho.get(position).getQuantidade() + AppSetup.carrinho.get(position).getProduto().getQuantidade());

        Log.d("removido", AppSetup.carrinho.get(position).toString());
        AppSetup.carrinho.remove(position);
        Log.d("item", "item removido");

        atualizaView();

        Toast.makeText(CarrinhoActivity.this, "Produto removido com sucesso!", Toast.LENGTH_SHORT).show();
    }

    public void atualizaView() {
        TextView tvTotalPedidoCarrinho = findViewById(R.id.tvTotalPedidoCarrinho);
        lv_carrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this, AppSetup.carrinho));
        for (ItemPedido itemPedido : AppSetup.carrinho) {
            total = total + itemPedido.getTotalItem();
        }
        tvTotalPedidoCarrinho.setText(NumberFormat.getCurrencyInstance().format(total));
    }
}
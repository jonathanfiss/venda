package br.edu.ifsul.vendas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.adapter.CarrinhoAdapter;
import br.edu.ifsul.vendas.adapter.ClientesAdapter;
import br.edu.ifsul.vendas.model.Cliente;
import br.edu.ifsul.vendas.model.ItemPedido;
import br.edu.ifsul.vendas.setup.AppSetup;

public class CarrinhoActivity extends AppCompatActivity {

    private ListView lv_carrinho;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        TextView tvTotalPedidoCarrinho = findViewById(R.id.tvTotalPedidoCarrinho);
        TextView tvClienteCarinho = findViewById(R.id.tvClienteCarrinho);

        lv_carrinho = findViewById(R.id.lv_carrinho);
        lv_carrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this, AppSetup.carrinho));;;;;;;
        Log.d("retorno",AppSetup.carrinho.toString());

        for (ItemPedido itemPedido : AppSetup.carrinho){
        total = total + itemPedido.getTotalItem();
        }

        tvTotalPedidoCarrinho.setText(String.valueOf(total));
        tvClienteCarinho.setText(String.valueOf(AppSetup.cliente.getNome().concat(" "+AppSetup.cliente.getSobrenome())));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity_carrinho, menu);

        return true;
    }
}
//separar em atualiza view
//zera a variavel
//e o for
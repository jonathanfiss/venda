package br.edu.ifsul.vendas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.adapter.CarrinhoAdapter;
import br.edu.ifsul.vendas.adapter.ClientesAdapter;
import br.edu.ifsul.vendas.model.Cliente;
import br.edu.ifsul.vendas.setup.AppSetup;

public class CarrinhoActivity extends AppCompatActivity {

    private ListView lv_carrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        lv_carrinho = findViewById(R.id.lv_carrinho);
        lv_carrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this, AppSetup.carrinho));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity_carrinho, menu);

        return true;
    }
}

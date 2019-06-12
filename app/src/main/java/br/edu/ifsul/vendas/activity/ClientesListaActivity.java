package br.edu.ifsul.vendas.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import br.edu.ifsul.vendas.adapter.ClientesAdapter;
import br.edu.ifsul.vendas.model.Cliente;

import static br.edu.ifsul.vendas.setup.AppSetup.clientes;

public class ClientesListaActivity extends AppCompatActivity {
    private ListView lvClientesList;
    private static final String TAG = "clienteslistaactivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_lista);

        lvClientesList = findViewById(R.id.lv_clientes_list);
        lvClientesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ClientesListaActivity.this, PedidosActivity.class);
                intent.putExtra("cliente", position);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("clientes");

        myRef.orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                Log.d(TAG, "Value is: " + dataSnapshot.getValue());

                clientes = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Cliente cliente = ds.getValue(Cliente.class);
                    cliente.setKey(ds.getKey());
                    clientes.add(cliente);
                }

                //carrega os dados na View
                lvClientesList.setAdapter(new ClientesAdapter(ClientesListaActivity.this, clientes));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}

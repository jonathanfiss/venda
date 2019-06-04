package br.edu.ifsul.vendas.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.model.User;
import br.edu.ifsul.vendas.setup.AppSetup;

public class UsuarioAdminActivity extends AppCompatActivity {
    private static final String TAG = "loginActivity";
    private FirebaseAuth mAuth;
    private EditText etEmailUser, etPasswordUser, etNomeUser, etSobrenomeUser;
    private Spinner spFuncaoUser;
    private String[] FUNCAO = new String[]{"Vendedor", "Administrador"};
    private Button cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_admin);

        //obtém a instância do serviço de autenticação
        mAuth = FirebaseAuth.getInstance();
        AppSetup.mAuth = mAuth;

        //mapeia os campos de input
        etNomeUser = findViewById(R.id.etNomeUser);
        etSobrenomeUser = findViewById(R.id.etSobrenomeUser);
        etEmailUser = findViewById(R.id.etEmail);
        etPasswordUser = findViewById(R.id.etSenha);
        spFuncaoUser = findViewById(R.id.spFuncaoUser);
        cadastrar = findViewById(R.id.btCadastrarUser);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FUNCAO);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFuncaoUser.setAdapter(adapter);

        //trata o evento onClick do button
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailUser.getText().toString();
                String senha = etPasswordUser.getText().toString();

                if(!email.isEmpty() && !senha.isEmpty()) {
                    signup(email,senha);
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
                    etEmailUser.setError(getString(R.string.input_error_invalido));
                    etPasswordUser.setError(getString(R.string.input_error_invalido));
                }
            }
        });
    }

    private void signup(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            cadastrarUser();
//                            sendEmailVerification();
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (Objects.requireNonNull(task.getException()).getMessage().contains("email")) {
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_already, Snackbar.LENGTH_LONG).show();
                                etEmailUser.setError(getString(R.string.input_error_invalido));
                            } else {
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.signup_fail, Snackbar.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }

    private void cadastrarUser() {
        User user = new User();
        user.setFirebaseUser(mAuth.getCurrentUser());
        user.setNome(etNomeUser.getText().toString());
        user.setSobrenome(etSobrenomeUser.getText().toString());
        user.setFuncao(FUNCAO[spFuncaoUser.getSelectedItemPosition()]);
        user.setEmail(mAuth.getCurrentUser().getEmail());
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(user.getFirebaseUser().getUid())
                .setValue(user);
        AppSetup.user = user;
    }

//    private void sendEmailVerification() {
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(UsuarioAdminActivity.this,
//                                    "Email de verificação enviado para " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.e(TAG, "sendEmailVerification", task.getException());
//                            Toast.makeText(UsuarioAdminActivity.this,
//                                    "Envio de email para verifiacão falhou.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

}

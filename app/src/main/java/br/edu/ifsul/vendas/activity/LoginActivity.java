package br.edu.ifsul.vendas.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "loginActivity";
    private FirebaseAuth mAuth;
    private EditText etEmail, etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //obtém a instância do serviço de autenticação
        mAuth = FirebaseAuth.getInstance();
        AppSetup.mAuth = mAuth;

        //mapeia os campos de input
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);

        //trata o evento onClick do button
        findViewById(R.id.bt_sigin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();
                if(!email.isEmpty() && !senha.isEmpty()) {
                    signin(email,senha);
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
                    etEmail.setError(getString(R.string.input_error_invalido));
                    etSenha.setError(getString(R.string.input_error_invalido));
                }
            }
        });

        //trata o evento onClick do textview (reset de senha)
        findViewById(R.id.tvEsqueceuSenha_tela_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                if(!email.isEmpty()){
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Reset pass email sent to " + email);
                                        Toast.makeText(LoginActivity.this, "Reset da senha enviado para " + email, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Log.d(TAG, "Reset pass falhou." + task.getException());
                                        Snackbar.make(findViewById(R.id.container_activity_login), R.string.signup_fail, Snackbar.LENGTH_LONG).show();
                                        etEmail.setError(getString(R.string.input_error_invalido));
                                    }
                                }
                            });
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_login), R.string.snack_insira_email, Snackbar.LENGTH_LONG).show();
                    etEmail.setError(getString(R.string.input_error_invalido));
                }
            }
        });
    }

    private void signin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Log.d(TAG, "signInWithEmail:success");
                                setUserSessao(mAuth.getCurrentUser());
                            }else{
                                Snackbar.make(findViewById(R.id.container_activity_login), "Valide seu email para o singin.", Snackbar.LENGTH_LONG).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure ",  task.getException());
                            if(Objects.requireNonNull(task.getException()).getMessage().contains("password")){
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.password_fail, Snackbar.LENGTH_LONG).show();
                                etSenha.setError(getString(R.string.input_error_invalido));
                            }else{
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_fail, Snackbar.LENGTH_LONG).show();
                                etEmail.setError(getString(R.string.input_error_invalido));
                            }
                        }
                    }
                });
    }

    private void setUserSessao(final FirebaseUser firebaseUser) {

        FirebaseDatabase.getInstance().getReference()
                .child("users").child(firebaseUser.getUid())
                .addListenerForSingleValueEvent (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AppSetup.user = dataSnapshot.getValue(User.class);
                        AppSetup.user.setFirebaseUser(firebaseUser);
                        startActivity(new Intent(LoginActivity.this, ProdutosActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_problemas_signin), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

//    private static final String TAG = "loginActivity";
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        //mapeia os botões e trata o evento onClick
//        findViewById(R.id.bt_sigin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
//                String senha = ((EditText)findViewById(R.id.etSenha)).getText().toString();
//                if(!email.isEmpty() && !senha.isEmpty()) {
//                    signin(email,senha);
//                }else{
//                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
//                }
//            }
//        });
//        findViewById(R.id.bt_sigup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
//                String senha = ((EditText)findViewById(R.id.etSenha)).getText().toString();
//                if(!email.isEmpty() && !senha.isEmpty()) {
//                    signup(email,senha);
//                }else{
//                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    private void signup(String email, String senha) {
//        mAuth.createUserWithEmailAndPassword(email, senha)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign up success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            cadastrarUser(mAuth.getCurrentUser());
//                            startActivity(new Intent(LoginActivity.this, ProdutosActivity.class));
//                            finish();
//                        } else {
//                            // If sign up fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            if(Objects.requireNonNull(task.getException()).getMessage().contains("email")){
//                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_already, Snackbar.LENGTH_LONG).show();
//                            }else {
//                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.signup_fail, Snackbar.LENGTH_LONG).show();
//                            }
//
//                        }
//                    }
//                });
//    }
//
//    private void cadastrarUser(FirebaseUser firebaseUser) {
//        User user = new User();
//        user.setFirebaseUser(firebaseUser);
//        user.setFuncao("vendedor");
//        user.setEmail(firebaseUser.getEmail());
//        FirebaseDatabase.getInstance().getReference().child("users")
//                .child(user.getFirebaseUser().getUid())
//                .setValue(user);
//        AppSetup.user = user;
//    }
//
//    private void signin(String email, String password){
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            setUserSessao(mAuth.getCurrentUser());
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure ",  task.getException());
//                            if(Objects.requireNonNull(task.getException()).getMessage().contains("password")){
//                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.password_fail, Snackbar.LENGTH_LONG).show();
//                            }else{
//                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_fail, Snackbar.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void setUserSessao(final FirebaseUser firebaseUser) {
//
//        FirebaseDatabase.getInstance().getReference()
//                .child("users").child(firebaseUser.getUid())
//                .addListenerForSingleValueEvent (new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        AppSetup.user = dataSnapshot.getValue(User.class);
//                        AppSetup.user.setFirebaseUser(firebaseUser);
//                        startActivity(new Intent(LoginActivity.this, ProdutosActivity.class));
//                        finish();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(LoginActivity.this, getString(R.string.toast_problemas_signin), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

//public class LoginActivity extends AppCompatActivity {
//
//    private static final String TAG = "loginActivity";
//    private FirebaseAuth mAuth;
//    private FirebaseUser user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        //mapeia os botões e trata o evento onClick
//        findViewById(R.id.bt_sigin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
//                String senha = ((EditText) findViewById(R.id.etSenha)).getText().toString();
//                ProgressBar pbLogin = findViewById(R.id.pb_login);
//                if (!email.isEmpty() && !senha.isEmpty()) {
//                    pbLogin.setVisibility(View.VISIBLE);
//                    signin(email, senha, pbLogin);
//                } else {
//                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
//                    //Toast.makeText(LoginActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        findViewById(R.id.bt_sigup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
//                String senha = ((EditText) findViewById(R.id.etSenha)).getText().toString();
//                ProgressBar pbLogin = findViewById(R.id.pb_login);
//                if (!email.isEmpty() && !senha.isEmpty()) {
//                    pbLogin.setVisibility(View.VISIBLE);
//                    signup(email, senha, pbLogin);
//                } else {
//                    pbLogin.setVisibility(View.INVISIBLE);
//                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
//                    //Toast.makeText(LoginActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        ProgressBar pbLogin = findViewById(R.id.pb_login);
//        pbLogin.setVisibility(View.INVISIBLE);
//    }
//
//    private void cadastrarUser(FirebaseUser firebaseUser) {
//        User user = new User();
//        user.setFirebaseUser(firebaseUser);
//        user.setFuncao("vendedor");
//        user.setEmail(firebaseUser.getEmail());
//        FirebaseDatabase.getInstance().getReference().child("vendas/users")
//                .child(user.getFirebaseUser().getUid())
//                .setValue(user);
//        AppSetup.user = user;
//    }
//
//    private void signup(String email, String senha, final ProgressBar pbLogin) {
//        mAuth.createUserWithEmailAndPassword(email, senha)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign up success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            user = mAuth.getCurrentUser();
//                            startActivity(new Intent(LoginActivity.this, ProdutosActivity.class));
//                        } else {
//                            pbLogin.setVisibility(View.INVISIBLE);
//                            // If sign up fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            if (task.getException().getMessage().contains("email")) {
//                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_already, Snackbar.LENGTH_LONG).show();
//                            } else {
//                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.signup_fail, Snackbar.LENGTH_LONG).show();
//                            }
//                            //Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }
//
//    private void signin(final String email, String password, final ProgressBar pbLogin) {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            user = mAuth.getCurrentUser();
//                            Log.d(TAG, user.getUid());
//                            setUserSessao(mAuth.getCurrentUser());
//                            startActivity(new Intent(LoginActivity.this, ProdutosActivity.class));
//                        } else {
//                            pbLogin.setVisibility(View.INVISIBLE);
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure ", task.getException());
//                            if (task.getException().getMessage().contains("password")) {
//                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.password_fail, Snackbar.LENGTH_LONG).show();
//                                //Toast.makeText(LoginActivity.this, "Senha não cadastrada.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_fail, Snackbar.LENGTH_LONG).show();
//                                //Toast.makeText(LoginActivity.this, "Email não cadastrado.", Toast.LENGTH_SHORT).show();
//                            }
//
//                            //updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }
//    private void setUserSessao(final FirebaseUser firebaseUser) {
//
//        FirebaseDatabase.getInstance().getReference()
//                .child("vendas/users").child(firebaseUser.getUid())
//                .addListenerForSingleValueEvent (new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        AppSetup.user = dataSnapshot.getValue(User.class);
//                        AppSetup.user.setFirebaseUser(firebaseUser);
//                        startActivity(new Intent(LoginActivity.this, ProdutosActivity.class));
//                        finish();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(LoginActivity.this, getString(R.string.toast_problemas_signin), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//}

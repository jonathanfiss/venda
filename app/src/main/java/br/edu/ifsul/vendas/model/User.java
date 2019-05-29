package br.edu.ifsul.vendas.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

public class User {
    private FirebaseUser firebaseUser;
    private String Funcao;
    private String Email;

    public User() {
    }

    @Exclude
    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    @Exclude
    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public String getFuncao() {
        return Funcao;
    }


    public void setFuncao(String funcao) {
        Funcao = funcao;
    }


    public String getEmail() {
        return Email;
    }


    public void setEmail(String email) {
        Email = email;
    }
}

package br.edu.ifsul.vendas.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

public class User {
    private com.google.firebase.auth.FirebaseUser FirebaseUser;
    private String Funcao;
    private String Email;

    public User() {
    }
    @Exclude
    public com.google.firebase.auth.FirebaseUser getFirebaseUser() {
        return FirebaseUser;
    }
    @Exclude

    public void setFirebaseUser(com.google.firebase.auth.FirebaseUser firebaseUser) {
        FirebaseUser = firebaseUser;
    }
    @Exclude

    public String getFuncao() {
        return Funcao;
    }
    @Exclude

    public void setFuncao(String funcao) {
        Funcao = funcao;
    }
    @Exclude

    public String getEmail() {
        return Email;
    }
    @Exclude

    public void setEmail(String email) {
        Email = email;
    }
}

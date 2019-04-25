package br.edu.ifsul.vendas.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private Long codigoDeBarras;
    private String email;
    private boolean situacao;
    private String key; //atributo apenas local

    public Usuario() {
    }

    public Long getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public void setCodigoDeBarras(Long codigoDeBarras) {
        this.codigoDeBarras = codigoDeBarras;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSituacao() {
        return situacao;
    }

    public void setSituacao(boolean situacao) {
        this.situacao = situacao;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "codigoDeBarras=" + codigoDeBarras +
                ", email='" + email + '\'' +
                ", situacao=" + situacao +
                '}';
    }
}

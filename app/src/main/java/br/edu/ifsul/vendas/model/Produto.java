package br.edu.ifsul.vendas.model;

import java.io.Serializable;
import java.util.List;

public class Produto implements Serializable {
    private Long codigoDeBarras;
    private String nome;
    private String descricao;
    private Double valor;
    private Integer quantidade;
    private boolean situacao;
    private String url_foto;
    private String key; //atributo apenas local

    public Produto() {
    }

    public Long getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public void setCodigoDeBarras(Long codigoDeBarras) {
        this.codigoDeBarras = codigoDeBarras;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isSituacao() {
        return situacao;
    }

    public void setSituacao(boolean situacao) {
        this.situacao = situacao;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "\nProduto{" +
                "codigoDeBarras=" + codigoDeBarras +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", quantidade=" + quantidade +
                ", situacao=" + situacao +
                ", url_foto='" + url_foto + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}

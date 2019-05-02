package br.edu.ifsul.vendas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.model.Produto;


public class ProdutosAdapter extends ArrayAdapter<Produto> {

    private Context context;
    private List<Produto> produtos;

    public ProdutosAdapter(@NonNull Context context, @NonNull List<Produto> produtos) {
        super(context, 0, produtos);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Devolve o objeto do modelo
        Produto produto = getItem(position);

        //infla a view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_produto_adapter, parent, false);
        }

        //mapeia os componentes da UI para vincular os dados do objeto de modelo
        TextView tvNome = convertView.findViewById(R.id.tvNomeProdutoAdapter);
        TextView tvEstoque = convertView.findViewById(R.id.tvEstoqueProdutoAdapater);
        TextView tvValor = convertView.findViewById(R.id.tvValorProdutoItemAdapter);
        ImageView imvFoto = convertView.findViewById(R.id.imvFotoProdutoAdapter);

        //vincula os dados do objeto de modelo à view
        tvNome.setText(produto.getNome());
        tvEstoque.setText(produto.getQuantidade().toString());
        tvValor.setText(NumberFormat.getCurrencyInstance().format(produto.getValor()));
        if (produto.getUrl_foto() != null) {
            //aqui vai vincular a foto do produto vindo do firebase usando a biblioteca Picasso
        } else {
            imvFoto.setImageResource(R.drawable.img_carrinho_de_compras);
        }


        return convertView;
    }
}

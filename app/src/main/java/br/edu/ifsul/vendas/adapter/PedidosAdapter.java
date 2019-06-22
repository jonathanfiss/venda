package br.edu.ifsul.vendas.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.List;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.model.ItemPedido;
import br.edu.ifsul.vendas.model.Pedido;
import br.edu.ifsul.vendas.setup.AppSetup;

import static android.support.constraint.Constraints.TAG;

public class PedidosAdapter extends ArrayAdapter<Pedido>{

    private final Context context;

    public PedidosAdapter(Context context, List<Pedido> pedido) {
        super(context, 0, pedido);
        this.context = context;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Bitmap[] fotoEmBitmap = new Bitmap[1];
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pedido_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //bindview
        final Pedido itemPedido = getItem(position);

        holder.nomeProduto.setText(itemPedido.getKey());
//        holder.quantidade.setText(itemPedido.getDataCriacao().toString());
        holder.quantidade.setText(String.format("%d %d %d", itemPedido.getDataCriacao().getDate(), itemPedido.getDataCriacao().getDay(),itemPedido.getDataCriacao().getYear()));
        holder.totalDoItem.setText(NumberFormat.getCurrencyInstance().format(itemPedido.getTotalPedido()));

        return convertView;
    }

    private class ViewHolder {
        TextView nomeProduto;
        TextView quantidade;
        TextView totalDoItem;
        ImageView fotoProduto;
        ProgressBar pbFoto;

        public ViewHolder(View convertView) {
            //mapeia os componentes da UI para vincular os dados do objeto de modelo
            nomeProduto = convertView.findViewById(R.id.tvNomeProdutoCarrinhoAdapter);
            quantidade = convertView.findViewById(R.id.tvQuantidadeDeProdutoCarrinhoAdapater);
            totalDoItem = convertView.findViewById(R.id.tvTotalItemCarrinhoAdapter);
            fotoProduto = convertView.findViewById(R.id.imvFotoProdutoCarrinhoAdapter);
            pbFoto = convertView.findViewById(R.id.pb_foto_carrinho);
        }
    }
}

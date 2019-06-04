package br.edu.ifsul.vendas.setup;

import android.graphics.Bitmap;
import android.hardware.camera2.CaptureRequest;

import com.google.firebase.auth.FirebaseAuth;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifsul.vendas.model.Cliente;
import br.edu.ifsul.vendas.model.ItemPedido;
import br.edu.ifsul.vendas.model.Pedido;
import br.edu.ifsul.vendas.model.Produto;
import br.edu.ifsul.vendas.model.User;
import br.edu.ifsul.vendas.model.Usuario;

public class AppSetup {

    public static List<Produto> produtos = new ArrayList<>();
    public static List<Cliente> clientes = new ArrayList<>();
    public static List<ItemPedido> carrinho = new ArrayList<>();
    public static Cliente cliente = null;
    public static Pedido pedido = null;
    public static User user = null;
    public static FirebaseAuth mAuth = null;
    public static Map<String, Bitmap> cacheProdutos = new HashMap<>();
    public static Map<String, Bitmap> cacheClientes = new HashMap<>();

}

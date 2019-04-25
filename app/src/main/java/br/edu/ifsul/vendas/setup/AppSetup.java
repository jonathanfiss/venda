package br.edu.ifsul.vendas.setup;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.vendas.model.Cliente;
import br.edu.ifsul.vendas.model.ItemPedido;
import br.edu.ifsul.vendas.model.Produto;
import br.edu.ifsul.vendas.model.Usuario;

public class AppSetup {

    public static List<Produto> produtos = new ArrayList<>();
    public static List<Cliente> clientes = new ArrayList<>();
    public static Usuario usuario = null;
    public static Cliente cliente = null;
    public static List<ItemPedido> carrinho = new ArrayList<>();
}

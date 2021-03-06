package com.blascoweb.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.blascoweb.cursomc.domain.Categoria;
import com.blascoweb.cursomc.domain.Cidade;
import com.blascoweb.cursomc.domain.Cliente;
import com.blascoweb.cursomc.domain.Endereco;
import com.blascoweb.cursomc.domain.Estado;
import com.blascoweb.cursomc.domain.ItemPedido;
import com.blascoweb.cursomc.domain.Pagamento;
import com.blascoweb.cursomc.domain.PagamentoComBoleto;
import com.blascoweb.cursomc.domain.PagamentoComCartao;
import com.blascoweb.cursomc.domain.Pedido;
import com.blascoweb.cursomc.domain.Produto;
import com.blascoweb.cursomc.domain.enums.StatusPagamento;
import com.blascoweb.cursomc.domain.enums.TipoCliente;
import com.blascoweb.cursomc.repositories.CategoriaRepository;
import com.blascoweb.cursomc.repositories.CidadeRepository;
import com.blascoweb.cursomc.repositories.ClienteRepository;
import com.blascoweb.cursomc.repositories.EnderecoRepository;
import com.blascoweb.cursomc.repositories.EstadoRepository;
import com.blascoweb.cursomc.repositories.ItemPedidoRepository;
import com.blascoweb.cursomc.repositories.PagamentoRepository;
import com.blascoweb.cursomc.repositories.PedidoRepository;
import com.blascoweb.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		//Adicionando Categorias
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");		
		//Adicionando Produtos
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);		
		//Informar quais produtos estão em cada categoria.
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		//Informar quais categorias são cada produto.
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));		
		//Salvando Produtos e Categoria no Banco
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		
		//Adicionando Estados
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");		
		//Adicionando Cidades(Obs.: relação muitos pra 1 associação no proprio construtor
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		//Associação Estado - Cidade
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));
		//Salvar no Banco
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		//Adicionando Cliente e seus dados.
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "363.789.123-77", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("2736-3323", "9383-8393"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "38220-834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777-012", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));
		
		//Adicionando Pedido
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pagamento pagto1 = new PagamentoComCartao(null, StatusPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		Pagamento pagto2 = new PagamentoComBoleto(null, StatusPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		
		//Instanciando os Itens de cada pedido.
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, p1.getPreco());
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, p3.getPreco());
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, p2.getPreco());
		
		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
		
	}

}

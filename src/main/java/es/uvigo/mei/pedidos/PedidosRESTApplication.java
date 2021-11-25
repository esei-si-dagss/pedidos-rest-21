package es.uvigo.mei.pedidos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.uvigo.mei.pedidos.daos.ArticuloDAO;
import es.uvigo.mei.pedidos.daos.ClienteDAO;
import es.uvigo.mei.pedidos.daos.FamiliaDAO;
import es.uvigo.mei.pedidos.entidades.Articulo;
import es.uvigo.mei.pedidos.entidades.Cliente;
import es.uvigo.mei.pedidos.entidades.Familia;
import es.uvigo.mei.pedidos.entidades.Direccion;

@SpringBootApplication
public class PedidosRESTApplication implements CommandLineRunner {
	@Autowired
	FamiliaDAO familiaDAO;
	
	@Autowired
	ArticuloDAO articuloDAO;
	
	@Autowired
	ClienteDAO clienteDAO;
	
	public static void main(String[] args) {
		SpringApplication.run(PedidosRESTApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		Familia f1 = new Familia("pepes", "familia  de pepes");
		f1 = familiaDAO.save(f1);		  
		  
		Familia f2 = new Familia("anas", "familia  de anas");
		f2 = familiaDAO.save(f1);		  

		articuloDAO.save(new Articulo("pepe1", "pepe1", f1, 10.0));
	    articuloDAO.save(new Articulo("pepe2", "pepe2", f1, 10.0));
	    articuloDAO.save(new Articulo("pepe3", "pepe3", f1, 10.0));

	    articuloDAO.save(new Articulo("ana1", "ana1", f2, 20.0));
	    articuloDAO.save(new Articulo("ana2", "ana2", f2, 20.0));
	        
	    Direccion d1 = new Direccion("calle 1", "Ourense", "11111", "Ourense", "123456789");
	    Direccion d2 = new Direccion("calle 2", "Santiago", "22222", "A Coruña", "123456789");
	    Direccion d3 = new Direccion("calle 3", "Santiago", "33333", "A Coruña", "123456789");
	    
	    clienteDAO.save(new Cliente("11111111A", "Pepe Cliente1 Cliente1", d1));
	    clienteDAO.save(new Cliente("22222222A", "Ana Cliente2 Cliente2", d1));
	    clienteDAO.save(new Cliente("33333333A", "Eva Cliente1 Cliente2", d1));
	    clienteDAO.save(new Cliente("44444444A", "Luis Cliente3 Cliente3", d2));
	    clienteDAO.save(new Cliente("55555555A", "Juab Cliente4 Cliente4", d3));
	    
	    
	 }
}

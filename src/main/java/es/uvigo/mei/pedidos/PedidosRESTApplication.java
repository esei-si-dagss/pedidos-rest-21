package es.uvigo.mei.pedidos;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.uvigo.mei.pedidos.daos.AlmacenDAO;
import es.uvigo.mei.pedidos.daos.ArticuloAlmacenDAO;
import es.uvigo.mei.pedidos.daos.ArticuloDAO;
import es.uvigo.mei.pedidos.daos.ClienteDAO;
import es.uvigo.mei.pedidos.daos.FamiliaDAO;
import es.uvigo.mei.pedidos.daos.PedidoDAO;
import es.uvigo.mei.pedidos.entidades.Almacen;
import es.uvigo.mei.pedidos.entidades.Articulo;
import es.uvigo.mei.pedidos.entidades.ArticuloAlmacen;
import es.uvigo.mei.pedidos.entidades.Cliente;
import es.uvigo.mei.pedidos.entidades.Familia;
import es.uvigo.mei.pedidos.entidades.LineaPedido;
import es.uvigo.mei.pedidos.entidades.Pedido;
import es.uvigo.mei.pedidos.entidades.Direccion;

@SpringBootApplication
public class PedidosRESTApplication implements CommandLineRunner {
	@Autowired
	FamiliaDAO familiaDAO;
	
	@Autowired
	ArticuloDAO articuloDAO;
	
	@Autowired
	ClienteDAO clienteDAO;
	
	@Autowired 
	PedidoDAO pedidoDAO;
	
	@Autowired 
	AlmacenDAO almacenDAO;

	@Autowired 
	ArticuloAlmacenDAO articuloAlmacenDAO;

	public static void main(String[] args) {
		SpringApplication.run(PedidosRESTApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
	    Direccion d1 = new Direccion("calle 1", "Ourense", "11111", "Ourense", "123456789");
	    Direccion d2 = new Direccion("calle 2", "Santiago", "22222", "A Coruña", "123456789");
	    Direccion d3 = new Direccion("calle 3", "Santiago", "33333", "A Coruña", "123456789");
	    
	    clienteDAO.save(new Cliente("11111111A", "Pepe Cliente1 Cliente1", d1));
	    clienteDAO.save(new Cliente("22222222A", "Ana Cliente2 Cliente2", d1));
	    clienteDAO.save(new Cliente("33333333A", "Eva Cliente1 Cliente2", d1));
	    clienteDAO.save(new Cliente("44444444A", "Luis Cliente3 Cliente3", d2));
	    clienteDAO.save(new Cliente("55555555A", "Juab Cliente4 Cliente4", d3));
	    
	    
	    Familia f11 = familiaDAO.save(new Familia("tubos", "tubos de todas clases"));
        Articulo a1 = articuloDAO.save(new Articulo("tubo acero", "tubo de acero", f11, 10.0));
        Articulo a2 = articuloDAO.save(new Articulo("tubo plastico", "tubo de plastico", f11, 5.0));
        
        Familia f22 = familiaDAO.save(new Familia("tuercas", "tuercas de todas clases"));
        Articulo a3 = articuloDAO.save(new Articulo("tuerca acero", "tuerca de acero 10/18", f22, 10.0));
        Articulo a4 = articuloDAO.save(new Articulo("tuerca plástico", "tuerca de plástico", f22, 5.0));
        

        Direccion d = new Direccion("calle", "localidad", "12345", "provincia", "123456789");
        Cliente c = clienteDAO.save(new Cliente("12345678A", "juan rey rey", d));

        Almacen al = almacenDAO.save(new Almacen("principal", "almacen principal", d));

        ArticuloAlmacen aa1 = articuloAlmacenDAO.save(new ArticuloAlmacen(a1, al, 10));
        ArticuloAlmacen aa2 = articuloAlmacenDAO.save(new ArticuloAlmacen(a2, al, 15));
        ArticuloAlmacen aa3 = articuloAlmacenDAO.save(new ArticuloAlmacen(a3, al, 20));
        ArticuloAlmacen aa4 = articuloAlmacenDAO.save(new ArticuloAlmacen(a4, al, 25));

        

        Pedido p = new Pedido(Calendar.getInstance().getTime(), c);
        p.anadirLineaPedido(new LineaPedido(p, 2, a1, a1.getPrecioUnitario()));
        p.anadirLineaPedido(new LineaPedido(p, 5, a2, a2.getPrecioUnitario()));
        p.anadirLineaPedido(new LineaPedido(p, 1, a3, a3.getPrecioUnitario()));
        
        pedidoDAO.save(p);
        
	    
	    
	 }
}

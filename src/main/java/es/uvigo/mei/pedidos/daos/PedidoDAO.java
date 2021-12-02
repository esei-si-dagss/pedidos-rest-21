package es.uvigo.mei.pedidos.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uvigo.mei.pedidos.entidades.Pedido;

public interface PedidoDAO extends JpaRepository<Pedido, Long>{

	@Query("SELECT p FROM Pedido AS p JOIN FETCH p.lineas WHERE p.id = :id")
	public Pedido findPedidoConLineas(Long id);

	public List<Pedido> findPedidoByClienteDNI(String dni);
}

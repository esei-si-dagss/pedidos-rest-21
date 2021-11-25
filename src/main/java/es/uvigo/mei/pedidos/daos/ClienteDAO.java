package es.uvigo.mei.pedidos.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uvigo.mei.pedidos.entidades.Cliente;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, String>{
	List<Cliente> findByNombreContaining(String nombre);
	List<Cliente> findByDireccionLocalidad(String localidad);
}

package es.uvigo.mei.pedidos.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uvigo.mei.pedidos.entidades.Familia;

public interface FamiliaDAO extends JpaRepository<Familia, Long> {
	public List<Familia> findFamiliaByNombre(String nombre);
    
	@Query("SELECT f FROM Familia f WHERE f.descripcion LIKE %?1")
	public List<Familia> findFamiliaByPatronDescripcion(String patron);
}

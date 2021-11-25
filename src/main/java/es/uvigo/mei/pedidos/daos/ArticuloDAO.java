package es.uvigo.mei.pedidos.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uvigo.mei.pedidos.entidades.Articulo;

public interface ArticuloDAO extends JpaRepository<Articulo, Long>{
	public List<Articulo> findArticuloByFamiliaId(Long id);
	public List<Articulo> findArticuloByNombre(String nombre);
    
	@Query("SELECT a FROM Articulo a WHERE a.descripcion LIKE %?1")
	public List<Articulo> findArticuloByPatronDescripcion(String patron);
}

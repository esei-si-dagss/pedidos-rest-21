package es.uvigo.mei.pedidos.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.uvigo.mei.pedidos.entidades.Almacen;
import es.uvigo.mei.pedidos.entidades.Articulo;

@Repository
public interface AlmacenDAO extends JpaRepository<Almacen, Long>{
	List<Almacen> findByNombreContaining(String nombre);
	List<Almacen> findByDireccionLocalidad(String localidad);
	@Query("SELECT aa.almacen FROM ArticuloAlmacen AS aa WHERE aa.articulo.id = :articuloId")
	List<Almacen> findByArticuloId(Long articuloId);
}

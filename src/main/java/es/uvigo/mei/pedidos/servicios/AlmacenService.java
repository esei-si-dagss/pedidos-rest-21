package es.uvigo.mei.pedidos.servicios;

import java.util.List;
import java.util.Optional;

import es.uvigo.mei.pedidos.entidades.Almacen;
import es.uvigo.mei.pedidos.entidades.ArticuloAlmacen;
import es.uvigo.mei.pedidos.entidades.Articulo;

public interface AlmacenService {
	public Almacen crear(Almacen almacen);
	public Almacen modificar(Almacen almacen);
	public void eliminar(Almacen almacen);
	public Optional<Almacen> buscarPorId(Long id);
	public List<Almacen> buscarTodos();
	public List<Almacen> buscarPorNombre(String patron);
	public List<Almacen> buscarPorLocalidad(String localidad);
	public List<Almacen> buscarPorArticuloId(Long articuloId);

	public ArticuloAlmacen crearArticuloAlmacen(ArticuloAlmacen articuloAlmacen);
	public ArticuloAlmacen crearArticuloAlmacen(Articulo articulo, Almacen almacen, Integer stock);
	public ArticuloAlmacen modificarArticuloAlmacen(ArticuloAlmacen articuloAlmacen);
	public void eliminarArticuloAlmacen(ArticuloAlmacen articuloAlmacen);
	public void eliminarArticuloAlmacen(Long idArticulo, Long idAlmacen);
	public Optional<ArticuloAlmacen> buscarArticuloAlmacenPorArticuloIdAlmacenId(Long articuloId, Long almacenId);
	public List<ArticuloAlmacen> buscarArticulosAlmacenPorAlmacenId(Long almacenId);
	public List<ArticuloAlmacen> buscarArticulosAlmacenPorArticuloId(Long articuloId);
}

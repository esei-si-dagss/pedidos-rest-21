package es.uvigo.mei.pedidos.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uvigo.mei.pedidos.daos.AlmacenDAO;
import es.uvigo.mei.pedidos.daos.ArticuloAlmacenDAO;
import es.uvigo.mei.pedidos.daos.ArticuloDAO;
import es.uvigo.mei.pedidos.entidades.Almacen;
import es.uvigo.mei.pedidos.entidades.Articulo;
import es.uvigo.mei.pedidos.entidades.ArticuloAlmacen;
import es.uvigo.mei.pedidos.entidades.ArticuloAlmacenId;

@Service
public class AlmacenServiceImpl implements AlmacenService {
	@Autowired
	private AlmacenDAO almacenDAO;
	
	@Autowired
	private ArticuloAlmacenDAO articuloAlmacenDAO;

	@Override
	@Transactional
	public Almacen crear(Almacen almacen) {
		return almacenDAO.save(almacen);
	}

	@Override
	@Transactional
	public Almacen modificar(Almacen almacen) {
		return almacenDAO.save(almacen);
	}

	@Override
	@Transactional
	public void eliminar(Almacen almacen) {
		almacenDAO.delete(almacen);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Almacen> buscarPorId(Long id) {
		return almacenDAO.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Almacen> buscarTodos() {
		return almacenDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Almacen> buscarPorNombre(String patron) {
		return almacenDAO.findByNombreContaining(patron);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Almacen> buscarPorLocalidad(String localidad) {
		return almacenDAO.findByDireccionLocalidad(localidad);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Almacen> buscarPorArticuloId(Long articuloId) {
		return almacenDAO.findByArticuloId(articuloId);		
	}

	@Override
	public ArticuloAlmacen crearArticuloAlmacen(ArticuloAlmacen articuloAlmacen) {
		return articuloAlmacenDAO.save(articuloAlmacen);
	}

	@Override
	@Transactional
	public ArticuloAlmacen crearArticuloAlmacen(Articulo articulo, Almacen almacen, Integer stock) {
		return this.crearArticuloAlmacen(new ArticuloAlmacen(articulo, almacen, stock));
	}

	@Override
	@Transactional
	public void eliminarArticuloAlmacen(ArticuloAlmacen articuloAlmacen) {
		articuloAlmacenDAO.delete(articuloAlmacen);
	}

	@Override
	public void eliminarArticuloAlmacen(Long articuloId, Long almacenId) {
		ArticuloAlmacen aa = articuloAlmacenDAO.getOne(new ArticuloAlmacenId(articuloId, almacenId));
		this.eliminarArticuloAlmacen(aa);
	}

	@Override
	@Transactional(readOnly = true)
	public ArticuloAlmacen buscarArticuloAlmacenPorArticuloIdAlmacenId(Long articuloId, Long almacenId) {
		return articuloAlmacenDAO.getOne(new ArticuloAlmacenId(articuloId, almacenId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArticuloAlmacen> buscarArticulosAlmacenPorAlmacenId(Long almacenId) {
		return articuloAlmacenDAO.findByAlmacenId(almacenId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArticuloAlmacen> buscarArticulosAlmacenPorArticuloId(Long articuloId) {
		return articuloAlmacenDAO.findByArticuloId(articuloId);
	}



}

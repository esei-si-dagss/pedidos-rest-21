package es.uvigo.mei.pedidos.controladores;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.uvigo.mei.pedidos.entidades.Almacen;
import es.uvigo.mei.pedidos.entidades.Articulo;
import es.uvigo.mei.pedidos.entidades.ArticuloAlmacen;
import es.uvigo.mei.pedidos.servicios.AlmacenService;
import es.uvigo.mei.pedidos.servicios.ArticuloService;
import io.swagger.v3.oas.annotations.Operation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/almacenes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlmacenController {
	@Autowired
	AlmacenService almacenService;
	@Autowired
	ArticuloService articuloService;

	@Operation(summary = "Recuperar el listado de Almacenes")
	@GetMapping()
	public ResponseEntity<List<EntityModel<Almacen>>> buscarTodos(
			@RequestParam(name = "localidad", required = false) String localidad,
			@RequestParam(name = "aticuloId", required = false) Long articuloId) {
		try {
			List<Almacen> resultado = new ArrayList<>();

			if (localidad != null) {
				resultado = almacenService.buscarPorLocalidad(localidad);
			} else if (articuloId != null) {
				resultado = almacenService.buscarPorArticuloId(articuloId);
			} else {
				resultado = almacenService.buscarTodos();
			}

			if (resultado.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			List<EntityModel<Almacen>> resultadoDTO = new ArrayList<>();
			resultado.forEach(a -> resultadoDTO.add(crearDTOAlmacen(a)));

			return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Recuperar datos de un Almacén")
	@GetMapping(path = "{id}")
	public ResponseEntity<EntityModel<Almacen>> buscarPorId(@PathVariable("id") Long id) {
		Optional<Almacen> almacen = almacenService.buscarPorId(id);

		if (almacen.isPresent()) {
			EntityModel<Almacen> dto = crearDTOAlmacen(almacen.get());
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Eliminar un Almacén")
	@DeleteMapping(path = "{id}")
	public ResponseEntity<HttpStatus> eliminar(@PathVariable("id") Long id) {
		try {
			Optional<Almacen> almacen = almacenService.buscarPorId(id);
			if (almacen.isPresent()) {
				almacenService.eliminar(almacen.get());
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Actualizar un Almacén")
	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<Almacen>> modificar(@PathVariable("id") Long id,
			@Valid @RequestBody Almacen almacen) {
		Optional<Almacen> almacenOptional = almacenService.buscarPorId(id);

		if (almacenOptional.isPresent()) {
			Almacen nuevoAlmacen = almacenService.modificar(almacen);
			EntityModel<Almacen> dto = crearDTOAlmacen(nuevoAlmacen);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Crear un nuevo Almacén")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<Almacen>> crear(@Valid @RequestBody Almacen almacen) {
		try {
			Almacen nuevoAlmacen = almacenService.crear(almacen);
			EntityModel<Almacen> dto = crearDTOAlmacen(nuevoAlmacen);
			URI uri = crearURIAlmacen(nuevoAlmacen);

			return ResponseEntity.created(uri).body(dto);
		} catch (

		Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// GET {id}/articulos
	@Operation(summary = "Recuperar los datos de Stock de todos los Artículos del Almacén indicado")
	@GetMapping(path = "{idAlmacen}/articulos")
	public ResponseEntity<List<EntityModel<ArticuloAlmacen>>> buscarArticulosAlmacen(
			@PathVariable("idAlmacen") Long idAlmacen) {
		try {
			List<ArticuloAlmacen> resultado = new ArrayList<>();

			resultado = almacenService.buscarArticulosAlmacenPorAlmacenId(idAlmacen);

			if (resultado.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			List<EntityModel<ArticuloAlmacen>> resultadoDTO = new ArrayList<>();
			resultado.forEach(aa -> resultadoDTO.add(crearDTOArticuloAlmacen(aa)));

			return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// GET {id}/articulos/{id}
	@Operation(summary = "Recuperar los datos de Stock de un Artículo en el Almacén indicado")
	@GetMapping(path = "{idAlmacen}/articulos/{idArticulo}")
	public ResponseEntity<EntityModel<ArticuloAlmacen>> buscarArticuloAlmacenPorId(
			@PathVariable("idAlmacen") Long idAlmacen, @PathVariable("idArticulo") Long idArticulo) {
		Optional<ArticuloAlmacen> articuloAlmacen = almacenService
				.buscarArticuloAlmacenPorArticuloIdAlmacenId(idArticulo, idAlmacen);

		if (articuloAlmacen.isPresent()) {
			EntityModel<ArticuloAlmacen> dto = crearDTOArticuloAlmacen(articuloAlmacen.get());
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// GET {id}/articulos/{id}/stock
	@Operation(summary = "Recuperar directamente el stock de un Articulo en el Almacén indicado")
	@GetMapping(path = "{idAlmacen}/articulos/{idArticulo}/stock")
	public ResponseEntity<Integer> leerStockArticuloAlmacenPorId(
			@PathVariable("idAlmacen") Long idAlmacen, @PathVariable("idArticulo") Long idArticulo) {
		Optional<ArticuloAlmacen> articuloAlmacen = almacenService
				.buscarArticuloAlmacenPorArticuloIdAlmacenId(idArticulo, idAlmacen);

		if (articuloAlmacen.isPresent()) {
			return new ResponseEntity<>(articuloAlmacen.get().getStock(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// PUT {id}/articulos/{id}
	@Operation(summary = "Actualizar los datos de Stock de un Artículo en el Almacén indicado")
	@PutMapping(path = "{idAlmacen}/articulos/{idArticulo}")
	public ResponseEntity<EntityModel<ArticuloAlmacen>> modificarArticuloAlmacen(
			@PathVariable("idAlmacen") Long idAlmacen, @PathVariable("idArticulo") Long idArticulo,
			@Valid @RequestBody ArticuloAlmacen articuloAlmacen) {
		// Recuperar nuevo stock
		Integer nuevoStock = articuloAlmacen.getStock();
		return _modificarStockArticuloAlmacen(idAlmacen, idArticulo, nuevoStock);
	}

	// PUT {id}/articulos/{id}/stock
	@Operation(summary = "Actualizar directamente el stock de un Articulo en el Almacén indicado")
	@PutMapping(path = "{idAlmacen}/articulos/{idArticulo}/stock")
	public ResponseEntity<EntityModel<ArticuloAlmacen>> modificarArticuloAlmacenDirecto(
			@PathVariable("idAlmacen") Long idAlmacen, @PathVariable("idArticulo") Long idArticulo,
			@Valid @RequestBody Integer nuevoStock) {
		return _modificarStockArticuloAlmacen(idAlmacen, idArticulo, nuevoStock);
	}

	private ResponseEntity<EntityModel<ArticuloAlmacen>> _modificarStockArticuloAlmacen(Long idAlmacen, Long idArticulo,
			Integer stock) {
		Optional<ArticuloAlmacen> articuloAlmacenOptional = almacenService
				.buscarArticuloAlmacenPorArticuloIdAlmacenId(idArticulo, idAlmacen);

		if (articuloAlmacenOptional.isPresent()) {
			ArticuloAlmacen articuloAlmacenAModificar = articuloAlmacenOptional.get();
			articuloAlmacenAModificar.setStock(stock);
			ArticuloAlmacen nuevoArticuloAlmacen = almacenService.modificarArticuloAlmacen(articuloAlmacenAModificar);
			EntityModel<ArticuloAlmacen> dto = crearDTOArticuloAlmacen(nuevoArticuloAlmacen);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	// DELETE {id}/articulos/{id}
	@Operation(summary = "Eliminar los datos de Stock de un Artículo en el Almacén indicado")
	@DeleteMapping(path = "{idAlmacen}/articulos/{idArticulo}")
	public ResponseEntity<HttpStatus> eliminarArticuloAlmacen(@PathVariable("idAlmacen") Long idAlmacen,
			@PathVariable("idArticulo") Long idArticulo) {
		try {
			Optional<ArticuloAlmacen> articuloAlmacen = almacenService
					.buscarArticuloAlmacenPorArticuloIdAlmacenId(idArticulo, idAlmacen);
			if (articuloAlmacen.isPresent()) {
				almacenService.eliminarArticuloAlmacen(articuloAlmacen.get());
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// POST {id}/articulos/
	@Operation(summary = "Crear los datos de Stock de un Artículo nuevo en el Almacén indicado")
	@PostMapping(path = "{idAlmacen}/articulos")
	public ResponseEntity<EntityModel<ArticuloAlmacen>> crearArticuloAlmacen(@PathVariable("idAlmacen") Long idAlmacen,
			@Valid @RequestBody ArticuloAlmacen articuloAlmacen) {
		Long idArticulo = articuloAlmacen.getArticulo().getId();
		Integer stock = articuloAlmacen.getStock();
		return _crearArticuloAlmacen(idAlmacen, idArticulo, stock);
	}

	// POST {id}/articulos/{id}/stock
	@Operation(summary = "Crear directamente el stock de un Articulo nuevo en el Almacén indicado")
	@PostMapping(path = "{idAlmacen}/articulos/{idArticulo}/stock")
	public ResponseEntity<EntityModel<ArticuloAlmacen>> crearArticuloAlmacenDirecto(
			@PathVariable("idAlmacen") Long idAlmacen, @PathVariable("idArticulo") Long idArticulo,
			@Valid @RequestBody Integer stock) {
		return _crearArticuloAlmacen(idAlmacen, idArticulo, stock);
	}

	private ResponseEntity<EntityModel<ArticuloAlmacen>> _crearArticuloAlmacen(Long idAlmacen, Long idArticulo,
			Integer stock) {
		try {
			Optional<ArticuloAlmacen> articuloAlmacenOptional = almacenService
					.buscarArticuloAlmacenPorArticuloIdAlmacenId(idArticulo, idAlmacen);
			if (articuloAlmacenOptional.isEmpty()) {
				Optional<Almacen> almacen = almacenService.buscarPorId(idAlmacen);
				Optional<Articulo> articulo = articuloService.buscarPorId(idArticulo);

				if ((almacen.isPresent()) && (articulo.isPresent())) {
					ArticuloAlmacen nuevoArticuloAlmacen = almacenService.crearArticuloAlmacen(articulo.get(),
							almacen.get(), stock);
					EntityModel<ArticuloAlmacen> dto = crearDTOArticuloAlmacen(nuevoArticuloAlmacen);
					URI uri = crearURIArticuloAlmacen(nuevoArticuloAlmacen);

					return ResponseEntity.created(uri).body(dto);
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} else {
				// Ya existe el artículo en el almacen
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// Crear los DTO con enlaces HATEOAS
	private EntityModel<Almacen> crearDTOAlmacen(Almacen almacen) {
		Long id = almacen.getId();
		EntityModel<Almacen> dto = EntityModel.of(almacen);
		dto.add(linkTo(methodOn(AlmacenController.class).buscarPorId(id)).withSelfRel());
		dto.add(linkTo(methodOn(AlmacenController.class).buscarArticulosAlmacen(id)).withRel("articulos"));
		return dto;
	}

	// Construye la URI del nuevo recurso creado con POST
	private URI crearURIAlmacen(Almacen almacen) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(almacen.getId()).toUri();
	}

	// Crear los DTO con enlaces HATEOAS
	private EntityModel<ArticuloAlmacen> crearDTOArticuloAlmacen(ArticuloAlmacen articuloAlmacen) {
		Long idAlmacen = articuloAlmacen.getAlmacen().getId();
		Long idArticulo = articuloAlmacen.getArticulo().getId();
		EntityModel<ArticuloAlmacen> dto = EntityModel.of(articuloAlmacen);
		dto.add(Link.of(this.crearURIArticuloAlmacen(articuloAlmacen).getPath(), "self"));
		dto.add(linkTo(methodOn(AlmacenController.class).buscarPorId(idAlmacen)).withRel("almacen"));
		dto.add(linkTo(methodOn(ArticuloController.class).buscarPorId(idArticulo)).withRel("articulo"));
		return dto;
	}

	// Construye la URI del nuevo recurso creado con POST
	private URI crearURIArticuloAlmacen(ArticuloAlmacen articuloAlmacen) {
		Long idArticulo = articuloAlmacen.getArticulo().getId();
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").path("articulos")
				.buildAndExpand(idArticulo).toUri();
	}

}

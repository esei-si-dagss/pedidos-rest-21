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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/almacenes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlmacenController {
	@Autowired
	AlmacenService almacenService;


	@GetMapping()
	public ResponseEntity<List<EntityModel<Almacen>>> buscarTodos(
			@RequestParam(name = "localidad", required = false) String localidad) {
		try {
			List<Almacen> resultado = new ArrayList<>();

			if (localidad != null) {
				resultado = almacenService.buscarPorLocalidad(localidad);
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
	
	
	// GET {id}/articulos  (?nombre=)
	// GET {id}/articulos/{id}
	// PUT {id}/articulos/{id}
	// DELETE {id}/articulos/{id}
	// POST {id}/articulos/

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

	// Crear los DTO con enlaces HATEOAS
	private EntityModel<Almacen> crearDTOAlmacen(Almacen almacen) {
		Long id = almacen.getId();
		EntityModel<Almacen> dto = EntityModel.of(almacen);
		Link linkSelf = linkTo(methodOn(AlmacenController.class).buscarPorId(id)).withSelfRel();
		dto.add(linkSelf);
		return dto;
	}

	// Construye la URI del nuevo recurso creado con POST
	private URI crearURIAlmacen(Almacen almacen) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(almacen.getId()).toUri();
	}

}

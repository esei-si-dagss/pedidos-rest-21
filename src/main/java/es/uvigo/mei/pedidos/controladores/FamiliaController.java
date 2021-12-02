package es.uvigo.mei.pedidos.controladores;

import java.net.URI;
import java.util.ArrayList;
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

import es.uvigo.mei.pedidos.entidades.Familia;
import es.uvigo.mei.pedidos.servicios.ArticuloService;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/familias", produces = MediaType.APPLICATION_JSON_VALUE)
public class FamiliaController {
	@Autowired
	ArticuloService articuloService;

	@GetMapping()
	public ResponseEntity<List<EntityModel<Familia>>> buscarTodos(
			@RequestParam(name = "descripcion", required = false) String descripcion) {
		try {
			List<Familia> resultado = new ArrayList<>();

			if (descripcion != null) {
				resultado = articuloService.buscarFamiliasPorDescripcion(descripcion);
			} else {
				resultado = articuloService.buscarFamilias();
			}

			if (resultado.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			List<EntityModel<Familia>> resultadoDTO = new ArrayList<>();
			resultado.forEach(f -> resultadoDTO.add(crearDTOFamilia(f)));

			return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<EntityModel<Familia>> buscarPorId(@PathVariable("id") Long id) {
		Optional<Familia> familia = articuloService.buscarFamiliaPorId(id);

		if (familia.isPresent()) {
			EntityModel<Familia> dto = crearDTOFamilia(familia.get());
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<HttpStatus> eliminar(@PathVariable("id") Long id) {
		try {
			Optional<Familia> familia = articuloService.buscarFamiliaPorId(id);
			if (familia.isPresent()) {
				articuloService.eliminarFamilia(familia.get());
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<Familia>> modificar(@PathVariable("id") Long id,
			@Valid @RequestBody Familia familia) {
		Optional<Familia> familiaOptional = articuloService.buscarFamiliaPorId(id);

		if (familiaOptional.isPresent()) {
			Familia nuevaFamilia = articuloService.modificarFamilia(familia);
			EntityModel<Familia> dto = crearDTOFamilia(nuevaFamilia);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<Familia>> crear(@Valid @RequestBody Familia familia) {
		try {
			Familia nuevaFamilia = articuloService.crearFamilia(familia);
			EntityModel<Familia> dto = crearDTOFamilia(nuevaFamilia);
			URI uri = crearURIFamilia(nuevaFamilia);

			return ResponseEntity.created(uri).body(dto);
		} catch (

		Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Crear los DTO con enlaces HATEOAS
	private EntityModel<Familia> crearDTOFamilia(Familia familia) {
		Long id = familia.getId();
		EntityModel<Familia> dto = EntityModel.of(familia);
		Link link = linkTo(methodOn(FamiliaController.class).buscarPorId(id)).withSelfRel();

		dto.add(link);
		return dto;
	}

	// Construye la URI del nuevo recurso creado con POST
	private URI crearURIFamilia(Familia familia) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(familia.getId()).toUri();
	}

}

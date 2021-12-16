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

import es.uvigo.mei.pedidos.entidades.Cliente;
import es.uvigo.mei.pedidos.servicios.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/clientes",
                produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "cliente", description = "API REST para Clientes")
public class ClienteController {
	@Autowired
	ClienteService clienteService;

	@Operation(summary = "Recuperar listado de Clientes")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Hay Clientes", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)) }),
			@ApiResponse(responseCode = "404", description = "No hay Clientes"),
			@ApiResponse(responseCode = "500", description = "Error en el acceso a los Clientes") })
	@GetMapping()
	public ResponseEntity<List<EntityModel<Cliente>>> buscarTodos(
			@Parameter(description = "Localidad del Cliente", required = false)
			@RequestParam(name = "localidad", required = false) String localidad,
			@Parameter(description = "Nombre del Cliente", required = false)
			@RequestParam(name = "nombre", required = false) String nombre) {
		try {
			List<Cliente> resultado = new ArrayList<>();

			if (localidad != null) {
				resultado = clienteService.buscarPorLocalidad(localidad);
			} else if (nombre != null) {
				resultado = clienteService.buscarPorNombre(nombre);
			} else {
				resultado = clienteService.buscarTodos();
			}

			if (resultado.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			List<EntityModel<Cliente>> resultadoDTO = new ArrayList<>();
			resultado.forEach(c -> resultadoDTO.add(crearDTOCliente(c)));
				
			return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// OTRA OPCION: separar los @GetMapping para los accesos con QueryParams
	//
	// @GetMapping(params={"localidad"})
	// public List<Cliente> buscarPorLocalidad(@RequestParam(name = "localidad",
	// required = false) String localidad) {...}
	// @GetMapping(params={"nombre"})
	// public List<Cliente> buscarPorNombre(@RequestParam(name = "nombre", required
	// = false) String nombre) {...}

	@Operation(summary = "Recuperar un Cliente por su DNI")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente encontrado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)) }),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado") })
	@GetMapping(path = "{dni}")
	public ResponseEntity<EntityModel<Cliente>> buscarPorDNI(@PathVariable("dni") String dni) {
		Optional<Cliente> cliente = clienteService.buscarPorDNI(dni);

		if (cliente.isPresent()) {
			EntityModel<Cliente> dto = crearDTOCliente(cliente.get());
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	

	@Operation(summary = "Eliminar el Cliente identificado por su DNI")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Cliente eliminado"),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
			@ApiResponse(responseCode = "500", description = "Error en el acceso al Cliente") })
	@DeleteMapping(path = "{dni}")
	public ResponseEntity<HttpStatus> eliminar(@PathVariable("dni") String dni) {
		try {
			Optional<Cliente> cliente = clienteService.buscarPorDNI(dni);
			if (cliente.isPresent()) {
				clienteService.eliminar(cliente.get());
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Actualizar los datos del Cliente identificado por su DNI")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente actualizado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)) }),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado") })
	@PutMapping(path = "{dni}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<Cliente>> modificar(@PathVariable("dni") String dni, @Valid @RequestBody Cliente cliente) {
		Optional<Cliente> clienteOptional = clienteService.buscarPorDNI(dni);

		if (clienteOptional.isPresent()) {
			Cliente nuevoCliente = clienteService.modificar(cliente);
			EntityModel<Cliente> dto = crearDTOCliente(nuevoCliente);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Crear un nuevo Cliente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente actualizado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)) }),
			@ApiResponse(responseCode = "400", description = "No se aporta DNI o ya existia un Cliente con ese DNI"),
			@ApiResponse(responseCode = "500", description = "Error en el acceso al Cliente")})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<Cliente>> crear(@Valid @RequestBody Cliente cliente) {
		try {
			String dni = cliente.getDNI();
			if ((dni != null) && !dni.isBlank()) {
				Optional<Cliente> clienteOptional = clienteService.buscarPorDNI(dni);

				if (clienteOptional.isEmpty()) {
					Cliente nuevoCliente = clienteService.crear(cliente);
					EntityModel<Cliente> dto = crearDTOCliente(nuevoCliente);
					URI uri = crearURICliente(nuevoCliente);

					return ResponseEntity.created(uri).body(dto);

				}
			}
			// No aporta DNI o DNI ya existe
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Crear los DTO con enlaces HATEOAS
	private EntityModel<Cliente> crearDTOCliente(Cliente cliente) {
		String dni = cliente.getDNI();
		EntityModel<Cliente> dto = EntityModel.of(cliente);
		Link link = linkTo(methodOn(ClienteController.class).buscarPorDNI(dni)).withSelfRel();
		dto.add(link);
		return dto;
	}
	
	// Construye la URI del nuevo recurso creado con POST 
	private URI crearURICliente(Cliente cliente) {
		return ServletUriComponentsBuilder.fromCurrentRequest()
		.path("/{dni}")
		.buildAndExpand(cliente.getDNI())
		.toUri();				
	}

}

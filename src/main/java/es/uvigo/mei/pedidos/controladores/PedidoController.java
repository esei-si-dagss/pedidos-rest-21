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

import es.uvigo.mei.pedidos.entidades.Pedido;
import es.uvigo.mei.pedidos.entidades.Cliente;
import es.uvigo.mei.pedidos.entidades.LineaPedido;
import es.uvigo.mei.pedidos.servicios.PedidoService;
import es.uvigo.mei.pedidos.servicios.ClienteService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController {
	@Autowired
	PedidoService pedidoService;

	@Autowired
	ClienteService clienteService;

	@GetMapping()
	public ResponseEntity<List<EntityModel<Pedido>>> buscarTodos(
			@RequestParam(name = "clienteDni", required = false) String clienteDni) {
		try {
			List<Pedido> resultado = new ArrayList<>();

			if (clienteDni != null) {
				Optional<Cliente> cliente = clienteService.buscarPorDNI(clienteDni);
				if (cliente.isPresent()) {
					resultado = pedidoService.buscarPorCliente(cliente.get());
				}
			} else {
				resultado = pedidoService.buscarTodos();
			}

			if (resultado.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			List<EntityModel<Pedido>> resultadoDTO = new ArrayList<>();
			resultado.forEach(a -> resultadoDTO.add(crearDTOPedido(a)));

			return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<EntityModel<Pedido>> buscarPorId(@PathVariable("id") Long id) {
		Pedido pedido = pedidoService.buscarPorIdConLineas(id);

		if (pedido != null) {
			EntityModel<Pedido> dto = crearDTOPedido(pedido);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<HttpStatus> eliminar(@PathVariable("id") Long id) {
		try {
			Optional<Pedido> pedido = pedidoService.buscarPorId(id);
			if (pedido.isPresent()) {
				pedidoService.eliminar(pedido.get());
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<Pedido>> modificar(@PathVariable("id") Long id,
			@Valid @RequestBody Pedido pedido) {
		Optional<Pedido> pedidoOptional = pedidoService.buscarPorId(id);

		if (pedidoOptional.isPresent()) {
			Pedido nuevoPedido = pedidoService.modificar(pedido);
			EntityModel<Pedido> dto = crearDTOPedido(nuevoPedido);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<Pedido>> crear(@Valid @RequestBody Pedido pedido) {
		try {
			System.out.println("PEIDIDO :  "+pedido);
			Pedido nuevoPedido = pedidoService.crear(pedido);
			EntityModel<Pedido> dto = crearDTOPedido(nuevoPedido);
			URI uri = crearURIPedido(nuevoPedido);

			return ResponseEntity.created(uri).body(dto);
		} catch (

		Exception e) {
			System.out.println("Falla PEIDIDO con  "+e.toString());
			
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Crear los DTO con enlaces HATEOAS
	private EntityModel<Pedido> crearDTOPedido(Pedido pedido) {
		Long id = pedido.getId();
		EntityModel<Pedido> dto = EntityModel.of(pedido);
		Link linkSelf = linkTo(methodOn(PedidoController.class).buscarPorId(id)).withSelfRel();
		dto.add(linkSelf);

		if (pedido.getCliente() != null) {
			String dni = pedido.getCliente().getDNI();
			Link linkCliente = linkTo(methodOn(ClienteController.class).buscarPorDNI(dni)).withRel("cliente");
			dto.add(linkCliente);
		}

		return dto;
	}

	// Construye la URI del nuevo recurso creado con POST
	private URI crearURIPedido(Pedido pedido) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedido.getId()).toUri();
	}

}

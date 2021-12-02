package es.uvigo.mei.pedidos.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uvigo.mei.pedidos.daos.PedidoDAO;
import es.uvigo.mei.pedidos.entidades.Cliente;
import es.uvigo.mei.pedidos.entidades.Pedido;

@Service
public class PedidoServiceImpl implements PedidoService {
	@Autowired
	PedidoDAO dao;

	@Override
	@Transactional
	public Pedido crear(Pedido pedido) {
		return dao.save(pedido);
	}

	@Override
	@Transactional
	public Pedido modificar(Pedido pedido) {
		return dao.save(pedido);
	}

	@Override
	@Transactional
	public void eliminar(Pedido pedido) {
		dao.delete(pedido);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Pedido> buscarPorId(Long id) {
		return dao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Pedido buscarPorIdConLineas(Long id) {
		return dao.findPedidoConLineas(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Pedido> buscarTodos() {
		return dao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Pedido> buscarPorCliente(Cliente cliente) {
		return dao.findPedidoByClienteDNI(cliente.getDNI());
	}

}

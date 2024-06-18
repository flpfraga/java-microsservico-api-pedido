package br.com.felipefraga.pedidos.service;

import br.com.felipefraga.pedidos.dto.PedidoDto;
import br.com.felipefraga.pedidos.model.Pedido;
import br.com.felipefraga.pedidos.enuns.EStatus;
import br.com.felipefraga.pedidos.repository.PedidoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final ModelMapper mapper;

    public PedidoService(PedidoRepository pedidoRepository, ModelMapper mapper) {
        this.pedidoRepository = pedidoRepository;
        this.mapper = mapper;
    }


    public List<PedidoDto> obterTodos() {
        return pedidoRepository.findAll().stream()
                .map(p -> mapper.map(p, PedidoDto.class))
                .collect(Collectors.toList());
    }

    public PedidoDto obterPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        return mapper.map(pedido, PedidoDto.class);
    }

    public PedidoDto criarPedido(PedidoDto dto) {
        Pedido pedido = mapper.map(dto, Pedido.class);

        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(EStatus.REALIZADO);
        pedido.getItens().forEach(item -> item.setPedido(pedido));
        Pedido salvo = pedidoRepository.save(pedido);

        return mapper.map(pedido, PedidoDto.class);
    }

    public PedidoDto atualizaStatus(Long id, String status) {

        Pedido pedido = pedidoRepository.porIdComItens(id);

        if (pedido == null) {
            throw new RuntimeException();
        }

        pedido.setStatus(EStatus.valueOf(status));
        pedidoRepository.atualizaStatus(EStatus.valueOf(status), pedido);
        return mapper.map(pedido, PedidoDto.class);
    }

    public void aprovaPagamentoPedido(Long id) {

        Pedido pedido = pedidoRepository.porIdComItens(id);

        if (pedido == null) {
            throw new RuntimeException();
        }

        pedido.setStatus(EStatus.PAGO);
        pedidoRepository.atualizaStatus(EStatus.PAGO, pedido);
    }
}

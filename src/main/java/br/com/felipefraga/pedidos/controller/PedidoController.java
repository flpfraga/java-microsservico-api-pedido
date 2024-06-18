package br.com.felipefraga.pedidos.controller;

import br.com.felipefraga.pedidos.dto.PedidoDto;
import br.com.felipefraga.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
        public List<PedidoDto> listarTodos() {
            return pedidoService.obterTodos();
        }

        @GetMapping("/{id}")
        public ResponseEntity<PedidoDto> listarPorId(@PathVariable @NotNull Long id) {
            PedidoDto dto = pedidoService.obterPorId(id);

            return  ResponseEntity.ok(dto);
        }

        @PostMapping()
        public ResponseEntity<PedidoDto> realizaPedido(@RequestBody @Valid PedidoDto dto, UriComponentsBuilder uriBuilder) {
            PedidoDto pedidoRealizado = pedidoService.criarPedido(dto);

            URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedidoRealizado.getId()).toUri();

            return ResponseEntity.created(endereco).body(pedidoRealizado);

        }

        @PutMapping("/{id}/status")
        public ResponseEntity<PedidoDto> atualizaStatus(@PathVariable Long id, @RequestBody String status){
           PedidoDto dto = pedidoService.atualizaStatus(id, status);
            return ResponseEntity.ok(dto);
        }


        @PutMapping("/{id}/pago")
        public ResponseEntity<Void> aprovaPagamento(@PathVariable @NotNull Long id) {
            pedidoService.aprovaPagamentoPedido(id);
            return ResponseEntity.ok().build();

        }
}

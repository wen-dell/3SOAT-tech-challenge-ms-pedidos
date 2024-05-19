package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.PagamentoRepository;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.domain.entidades.Pagamento;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.enums.StatusPagamento;
import br.com.tech.challenge.domain.enums.StatusPedido;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    private final PedidoRepository pedidoRepository;

    @Transactional
    public Pagamento save(Pedido pedido) {
        log.info("Montando pagamento antes de salvar");
        var pagamento = Pagamento.builder()
                .pedido(pedido)
                .valorTotal(pedido.getValorTotal())
                .statusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO)
                .build();
        log.info("Salvando pagamento do pedido {}", pedido.getId());
        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public Pagamento checkout(Long idPedido) {
        log.info("Checkout de pedido {}", idPedido);
        var pedido = getPedido(idPedido);
        var pagamento = findPagamentoByPedidoId(pedido.getId());

        log.info("Alterando status do pagamento para PAGO");
        pagamento.setStatusPagamento(StatusPagamento.PAGO);
        pagamento.setDataHoraPagamento(LocalDateTime.now());

        log.info("Alterando status do pedido para EM_PREPARACAO");
        pedido.setStatusPedido(StatusPedido.EM_PREPARACAO);
        pedidoRepository.save(pedido);
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento findPagamentoByPedidoId(Long idPedido) {
        log.info("Buscando pagamento por id do pedido {}", idPedido);
        return pagamentoRepository.findPagamentoByPedidoId(idPedido)
                .orElseThrow(() -> new ObjectNotFoundException("Pagamento não encontrado."));
    }

    @Generated
    private Pedido getPedido(Long idPedido) {
        log.info("Buscando pedido por id {}", idPedido);
        return pedidoRepository.findById(idPedido).orElseThrow(() -> new ObjectNotFoundException("Pedido não encontrado."));
    }

}

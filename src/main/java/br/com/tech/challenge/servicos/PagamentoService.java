package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.PagamentoRepository;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.domain.entidades.Pagamento;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.enums.StatusPagamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Pagamento findPagamentoByPedidoId(Long idPedido) {
        log.info("Buscando pagamento por id do pedido {}", idPedido);
        return pagamentoRepository.findPagamentoByPedidoId(idPedido)
                .orElseThrow(() -> new ObjectNotFoundException("Pagamento não encontrado."));
    }

}

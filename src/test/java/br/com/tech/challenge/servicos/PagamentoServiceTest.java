package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.client.MercadoPagoClient;
import br.com.tech.challenge.bd.repositorios.PagamentoRepository;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.domain.dto.external.DataDTO;
import br.com.tech.challenge.domain.dto.external.EventDTO;
import br.com.tech.challenge.domain.dto.external.MerchantOrderDTO;
import br.com.tech.challenge.domain.entidades.*;
import br.com.tech.challenge.domain.enums.StatusPagamento;
import br.com.tech.challenge.domain.enums.StatusPedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PagamentoServiceTest {

    private final PagamentoService pagamentoService;

    @Mock
    private MercadoPagoClient mercadoPagoClient;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private CozinhaTopicProducer cozinhaTopicProducer;

    PagamentoServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.pagamentoService = new PagamentoService(
                mercadoPagoClient,
                pagamentoRepository,
                pedidoRepository,
                produtoService,
                cozinhaTopicProducer
        );
    }

    @DisplayName("Deve criar um pagamento com sucesso")
    @Test
    void shouldCreatePagamentoSuccess() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var pedido = setPedido();
        var pagamento = setPagamento(clock);

        when(pagamentoRepository.save(any())).thenReturn(pagamento);

        var returnedPagamento = pagamentoService.save(pedido);

        assertEquals(1L, returnedPagamento.getId());
        assertEquals(1L, returnedPagamento.getPedido().getId());
        assertEquals(LocalDateTime.now(clock), returnedPagamento.getDataHoraPagamento());
        assertEquals(new BigDecimal("10.00"), returnedPagamento.getValorTotal());
        assertNull(returnedPagamento.getQrData());
        assertEquals(StatusPagamento.AGUARDANDO_PAGAMENTO, returnedPagamento.getStatusPagamento());
    }

    @DisplayName("Deve encontrar o pagamento pelo id do pedido")
    @Test
    void shouldFindPagamentoByPedidoId() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        final Long idPedido = 1L;

        when(pagamentoRepository.findPagamentoByPedidoId(idPedido)).thenReturn(Optional.of(setPagamento(clock)));

        var returnedPagamento = pagamentoService.findPagamentoByPedidoId(idPedido);

        assertEquals(1L, returnedPagamento.getId());
        assertEquals(1L, returnedPagamento.getPedido().getId());
        assertEquals(LocalDateTime.now(clock), returnedPagamento.getDataHoraPagamento());
        assertEquals(new BigDecimal("10.00"), returnedPagamento.getValorTotal());
        assertNull(returnedPagamento.getQrData());
        assertEquals(StatusPagamento.AGUARDANDO_PAGAMENTO, returnedPagamento.getStatusPagamento());
    }

    @DisplayName("Deve fazer o checkout")
    @Test
    void shouldDoCheckout() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var pagamento = setPagamento(clock);
        var pedido = setPedido();

        pedido = pedido.toBuilder().statusPedido(StatusPedido.EM_PREPARACAO).build();
        pagamento = pagamento.toBuilder().pedido(pedido).build();
        pedido = pedido.toBuilder().pagamento(Pagamento.builder()
                .id(1L)
                .pedido(setPedido())
                .dataHoraPagamento(LocalDateTime.now(clock))
                .valorTotal(new BigDecimal("10.00"))
                .qrData(null)
                .statusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO)
                .build()).build();

        String id = "999999999";

        doNothing().when(cozinhaTopicProducer).enviarPedidoParaCozinha(anyLong());
        when(mercadoPagoClient.getMerchantOrder(id)).thenReturn(setMerchantOrderDTO());
        when(pedidoRepository.findBySenhaRetirada(setPedido().getSenhaRetirada())).thenReturn(Optional.of(pedido));
        when(pagamentoRepository.save(any())).thenReturn(pagamento.toBuilder()
                .statusPagamento(StatusPagamento.PAGO)
                .dataHoraPagamento(LocalDateTime.now(clock))
                .build()
        );

        var returnedPagamento = pagamentoService.checkout(setEventDTO());
        assertEquals(StatusPagamento.PAGO, returnedPagamento.getStatusPagamento());
        assertNotNull(returnedPagamento.getDataHoraPagamento());
        assertEquals(StatusPedido.EM_PREPARACAO, returnedPagamento.getPedido().getStatusPedido());
    }

    private Pagamento setPagamento(Clock clock) {
        return Pagamento.builder()
                .id(1L)
                .pedido(setPedido())
                .dataHoraPagamento(LocalDateTime.now(clock))
                .valorTotal(new BigDecimal("10.00"))
                .qrData(null)
                .statusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO)
                .build();
    }

    private Pedido setPedido() {
        return Pedido.builder()
                .id(1L)
                .senhaRetirada(123456)
                .cliente(setCliente())
                .produtos(List.of(setProduto()))
                .valorTotal(BigDecimal.valueOf(10.00))
                .statusPedido(StatusPedido.RECEBIDO)
                .dataHora(LocalDateTime.now())
                .build();
    }

    private Produto setProduto() {
        return Produto.builder()
                .id(1L)
                .descricao("Coca Cola")
                .valorUnitario(BigDecimal.valueOf(5.00))
                .categoria(setCategoria())
                .build();
    }

    private Categoria setCategoria() {
        return Categoria.builder()
                .id(2L)
                .descricao("Bebida")
                .build();
    }

    private Cliente setCliente() {
        return Cliente.builder()
                .id(1L)
                .nome("Anthony Samuel Joaquim Teixeira")
                .email("anthony.samuel.teixeira@said.adv.br")
                .cpf("143.025.400-95")
                .build();
    }

    private EventDTO setEventDTO() {
        return EventDTO.builder()
                .id(12345L)
                .liveMode(true)
                .type("payment")
                .dateCreated("2015-03-25T10:04:58.396-04:00")
                .userId(44444L)
                .apiVersion("v1")
                .action("payment.created")
                .data(DataDTO.builder().id("999999999").build())
                .build();
    }

    private MerchantOrderDTO setMerchantOrderDTO() {
        return MerchantOrderDTO.builder()
                .id(12345L)
                .externalReference("123456")
                .status("ok")
                .build();
    }

}

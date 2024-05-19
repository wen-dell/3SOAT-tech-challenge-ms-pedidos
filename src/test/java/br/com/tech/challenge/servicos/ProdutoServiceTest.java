package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.client.ProdutoApiClient;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ProdutoServiceTest {

    private final ProdutoService produtoService;

    @Mock
    private ProdutoApiClient produtoApiClient;

    ProdutoServiceTest() {
        MockitoAnnotations.openMocks(this);
        produtoService = new ProdutoService(produtoApiClient);
    }

    @DisplayName("Deve encontrar o produto pelo id")
    @Test
    void shouldFindProdutoById() {
        final Long id = 1L;

        when(produtoApiClient.findById(id)).thenReturn(ResponseEntity.ok(setProdutoDTO()));

        assertTrue(produtoService.findById(id).isPresent());
    }

    private ProdutoDTO setProdutoDTO() {
        return ProdutoDTO.builder()
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

}

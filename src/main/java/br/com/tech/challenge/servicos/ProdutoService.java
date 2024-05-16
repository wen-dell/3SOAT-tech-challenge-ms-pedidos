package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.client.ProdutoApiClient;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {

    private final ProdutoApiClient produtoApiClient;

    public Optional<ProdutoDTO> findById(Long id) {
        log.info("Buscando por id {}", id);
        ProdutoDTO produtoDTO = produtoApiClient.findById(id).getBody();
        return Optional.ofNullable(produtoDTO);
    }

}

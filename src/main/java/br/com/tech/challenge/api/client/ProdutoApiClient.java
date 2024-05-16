package br.com.tech.challenge.api.client;

import br.com.tech.challenge.domain.dto.ProdutoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "produtosapi", url = "http://localhost:8080/produtos")
public interface ProdutoApiClient {

    @GetMapping("/{id}")
    ResponseEntity<ProdutoDTO> findById(@PathVariable("id") final Long id);

}

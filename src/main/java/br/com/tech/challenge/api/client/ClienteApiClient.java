package br.com.tech.challenge.api.client;

import br.com.tech.challenge.domain.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "clienteapi", url = "http://localhost:8080/clientes")
public interface ClienteApiClient {

    @GetMapping
    ResponseEntity<Page<ClienteDTO>> list(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "pagina", defaultValue = "0") int pagina,
            @RequestParam(name = "tamanho", defaultValue = "10") int tamanho
    );

}

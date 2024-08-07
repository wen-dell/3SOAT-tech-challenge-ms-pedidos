package br.com.tech.challenge.api.client;

import br.com.tech.challenge.api.client.factory.ClientHttpFactory;
import br.com.tech.challenge.api.client.factory.RetryTemplateFactory;
import br.com.tech.challenge.api.exception.MercadoPagoAPIException;
import br.com.tech.challenge.domain.dto.external.MercadoPagoRequestDTO;
import br.com.tech.challenge.domain.dto.external.MercadoPagoResponseDTO;
import br.com.tech.challenge.domain.dto.external.MerchantOrderDTO;
import br.com.tech.challenge.domain.enums.MercadoPagoAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoClient implements QRClient {

    private final ObjectMapper objectMapper;

    @Override
    public MercadoPagoResponseDTO generateQRCode(MercadoPagoRequestDTO dto) {
        log.info("Fazendo requisicao para API do Mercado Pago");
        RetryTemplate retryTemplate = RetryTemplateFactory.retryTemplate();
        RestTemplate restTemplate = new RestTemplate(ClientHttpFactory.clientHttpRequestFactory());

        try {
            ResponseEntity<MercadoPagoResponseDTO> responseEntity = retryTemplate
                    .execute(retryContext -> restTemplate.postForEntity(
                            MercadoPagoAPI.MERCADO_PAGO_URL.text().concat(MercadoPagoAPI.getQRCodeUrl()),
                            httpEntity(dto),
                            MercadoPagoResponseDTO.class
                    ));

            return responseEntity.getBody();
        } catch (RestClientException | JsonProcessingException exception) {
            throw new MercadoPagoAPIException("Ocorreu um erro ao gerar QR Code: " + exception.getMessage());
        }
    }

    public MerchantOrderDTO getMerchantOrder(String id) {
        log.info("Fazendo requisicao para API do Mercado Pago [Merchant Order]");
        RetryTemplate retryTemplate = RetryTemplateFactory.retryTemplate();
        RestTemplate restTemplate = new RestTemplate(ClientHttpFactory.clientHttpRequestFactory());

        HttpHeaders headers = getAuthorizationHeader();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<MerchantOrderDTO> responseEntity = retryTemplate
                    .execute(retryContext -> restTemplate.exchange(
                            MercadoPagoAPI.MERCADO_PAGO_URL.text().concat(MercadoPagoAPI.getMerchantOrderUrl(id)),
                            HttpMethod.GET,
                            httpEntity,
                            MerchantOrderDTO.class
                    ));

            return responseEntity.getBody();
        } catch (RestClientException exception) {
            throw new MercadoPagoAPIException("Ocorreu um erro ao gerar QR Code: " + exception.getMessage());
        }
    }

    private HttpHeaders getAuthorizationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", MercadoPagoAPI.ACCESS_TOKEN.text());
        return headers;
    }

    private HttpEntity<String> httpEntity(MercadoPagoRequestDTO dto) throws JsonProcessingException {
        log.info("Adicionando header Authorization a chamada do Mercado Pago");
        HttpHeaders headers = getAuthorizationHeader();

        return new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
    }

}

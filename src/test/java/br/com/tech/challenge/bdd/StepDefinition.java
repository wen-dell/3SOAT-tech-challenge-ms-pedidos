package br.com.tech.challenge.bdd;

import br.com.tech.challenge.domain.dto.PagamentoDTO;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpStatus.OK;

public class StepDefinition {

    private Response response;
    private final String ENDPOINT_API_PAGAMENTOS = "http://localhost:8082/pagamentos/pedido/100/checkout";

    @Quando("realizar um checkout")
    public PagamentoDTO realizarUmCheckout() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(ENDPOINT_API_PAGAMENTOS);
        return response.then()
                .extract().as(PagamentoDTO.class);
    }

    @Então("o pagamento muda de status e entra em preparação")
    public void oCheckoutERealizadoComSucesso() {
        response.then()
                .statusCode(OK.value());
    }

    @E("retornado o pagamento")
    public void retornandoPagamento() {
        response.body().as(PagamentoDTO.class);
    }

}

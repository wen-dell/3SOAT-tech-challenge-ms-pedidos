# language: pt

Funcionalidade: API Pagamento

  Cenário: Realizar um checkout
    Quando realizar um checkout
    Então o pagamento muda de status e entra em preparação
    E retornado o pagamento
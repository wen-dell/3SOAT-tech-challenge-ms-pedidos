package br.com.tech.challenge.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MercadoPagoAPI {

    ACCESS_TOKEN("Bearer TEST-6621802098599609-102118-84e7d61f11ef646bbaf89a78e1bb2631-700064145"),

    MERCADO_PAGO_URL("https://api.mercadopago.com"),

    CAIXA_PAGAMENTO_ID("CAIXA003"),

    USER_ID("700064145");

    final String text;

    public String text() {
        return text;
    }

    public static String getQRCodeUrl() {
        return String.format("/instore/orders/qr/seller/collectors/%s/pos/%s/qrs",
                MercadoPagoAPI.USER_ID.text(), MercadoPagoAPI.CAIXA_PAGAMENTO_ID.text()
        );
    }

    public static String getMerchantOrderUrl(String id) {
        return String.format("/merchant_orders/%s", id);
    }

}

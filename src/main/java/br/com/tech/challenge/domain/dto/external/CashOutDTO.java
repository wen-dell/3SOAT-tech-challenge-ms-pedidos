package br.com.tech.challenge.domain.dto.external;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashOutDTO {

    private BigDecimal amount;

}

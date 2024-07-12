package br.com.tech.challenge.domain.dto.external;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemDTO {

        private String category;

        private String title;

        private String description;

        private BigDecimal unitPrice;

        private Long quantity;

        private String unitMeasure;

        private BigDecimal totalAmount;

}

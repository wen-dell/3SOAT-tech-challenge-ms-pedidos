package br.com.tech.challenge.domain.dto.external;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventDTO {

    private Long id;

    private boolean liveMode;

    private String type;

    private String dateCreated;

    private Long userId;

    private String apiVersion;

    private String action;

    private DataDTO data;

}

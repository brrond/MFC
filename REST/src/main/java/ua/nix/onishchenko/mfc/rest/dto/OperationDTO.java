package ua.nix.onishchenko.mfc.rest.dto;

import lombok.*;
import ua.nix.onishchenko.mfc.rest.entity.Operation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OperationDTO {
    private UUID id;
    private String operationType = "";
    private BigDecimal sum = BigDecimal.ZERO;
    private Instant creation = Instant.now();

    public OperationDTO(Operation operation) {
        id = operation.getId();
        operationType = operation.getType().getTitle();
        sum = operation.getSum();
        creation = operation.getCreation();
    }
}

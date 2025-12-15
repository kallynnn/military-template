package ua.edu.viti.military.dto.request;

import lombok.Data;
import ua.edu.viti.military.entity.MovementType;

@Data
public class MovementRequestDTO {
    private Long itemId;
    private MovementType type;
    private Integer quantity;
    private String recipientName;
    private String recipientUnit;
    private String notes;
}
package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import ua.edu.viti.military.entity.HazardClass;

import java.time.LocalDate;

@Data
public class SupplyItemCreateDTO {

    @NotBlank(message = "Назва матеріалу обов'язкова")
    @Size(max = 200, message = "Назва надто довга")
    private String name;

    @NotBlank(message = "Номер партії обов'язковий")
    @Size(max = 50)
    private String batchNumber;

    @NotNull(message = "ID категорії обов'язковий")
    private Long categoryId;

    @NotNull(message = "Кількість обов'язкова")
    @Positive(message = "Кількість має бути більше 0")
    private Integer quantity;

    @NotBlank(message = "Одиниця виміру обов'язкова")
    private String unit = "шт"; // значення за замовчуванням

    @Future(message = "Термін придатності має бути в майбутньому")
    private LocalDate expirationDate;

    @NotNull(message = "Клас небезпеки обов'язковий")
    private HazardClass hazardClass;

    private String storageConditions;

    private Long warehouseId; // Може бути null
}
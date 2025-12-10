package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.SupplyItemCreateDTO;
import ua.edu.viti.military.dto.response.SupplyItemResponseDTO;
import ua.edu.viti.military.service.SupplyItemService;

import java.util.List;

@RestController
@RequestMapping("/api/supply-items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supply Items", description = "Управління матеріалами")
public class SupplyItemController {

    private final SupplyItemService itemService;

    @PostMapping
    @Operation(summary = "Створити новий матеріал")
    public ResponseEntity<SupplyItemResponseDTO> create(@Valid @RequestBody SupplyItemCreateDTO dto) {
        log.info("REST request to create item: {}", dto.getName());
        SupplyItemResponseDTO created = itemService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Отримати всі матеріали")
    public ResponseEntity<List<SupplyItemResponseDTO>> getAll() {
        return ResponseEntity.ok(itemService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати матеріал за ID")
    public ResponseEntity<SupplyItemResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getById(id));
    }
}
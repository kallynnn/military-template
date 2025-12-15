package ua.edu.viti.military.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.MovementRequestDTO;
import ua.edu.viti.military.dto.response.MovementResponseDTO;
import ua.edu.viti.military.service.SupplyMovementService;

import java.util.List;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
public class SupplyMovementController {

    private final SupplyMovementService service;

    // Створити (видати)
    @PostMapping("/issue")
    public ResponseEntity<MovementResponseDTO> issueItem(@RequestBody MovementRequestDTO dto) {
        return ResponseEntity.ok(service.issueItem(dto));
    }

    // Отримати історію
    @GetMapping("/history/{itemId}")
    public ResponseEntity<List<MovementResponseDTO>> getHistory(@PathVariable Long itemId) {
        return ResponseEntity.ok(service.getHistory(itemId));
    }

    // Оновити (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<MovementResponseDTO> updateMovement(@PathVariable Long id, 
                                                              @RequestBody MovementRequestDTO dto) {
        return ResponseEntity.ok(service.updateMovement(id, dto));
    }

    // Видалити (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable Long id) {
        service.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }
}
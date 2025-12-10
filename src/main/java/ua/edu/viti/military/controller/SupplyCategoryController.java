package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.entity.SupplyCategory;
import ua.edu.viti.military.service.SupplyCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/supply-categories")
@RequiredArgsConstructor
@Tag(name = "Supply Categories", description = "Управління категоріями")
public class SupplyCategoryController {

    private final SupplyCategoryService categoryService;

    @PostMapping
    @Operation(summary = "Створити категорію (Тестовий метод)")
    public ResponseEntity<SupplyCategory> create(@RequestParam String name, 
                                                 @RequestParam String code,
                                                 @RequestParam(required = false) String description) {
        return ResponseEntity.ok(categoryService.create(name, code, description));
    }

    @GetMapping
    @Operation(summary = "Отримати всі категорії")
    public ResponseEntity<List<SupplyCategory>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}   
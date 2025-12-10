package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.SupplyItemCreateDTO;
import ua.edu.viti.military.dto.response.SupplyItemResponseDTO;
import ua.edu.viti.military.entity.ItemStatus;
import ua.edu.viti.military.entity.SupplyCategory;
import ua.edu.viti.military.entity.SupplyItem;
import ua.edu.viti.military.entity.Warehouse;
import ua.edu.viti.military.exception.DuplicateResourceException; // Треба створити цей клас
import ua.edu.viti.military.exception.ResourceNotFoundException; // Треба створити цей клас
import ua.edu.viti.military.repository.SupplyCategoryRepository;
import ua.edu.viti.military.repository.SupplyItemRepository;
import ua.edu.viti.military.repository.WarehouseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Автоматично створює конструктор для final полів
@Slf4j // Додає логування
@Transactional(readOnly = true) // За замовчуванням транзакції тільки на читання
public class SupplyItemService {

    private final SupplyItemRepository itemRepository;
    private final SupplyCategoryRepository categoryRepository;
    private final WarehouseRepository warehouseRepository;

    // СТВОРЕННЯ (CREATE)
    @Transactional // Тут ми пишемо в базу, тому readOnly = false
    public SupplyItemResponseDTO create(SupplyItemCreateDTO dto) {
        log.info("Creating new supply item: {}", dto.getName());

        // 1. Перевірка унікальності партії
        if (itemRepository.existsByBatchNumber(dto.getBatchNumber())) {
            throw new DuplicateResourceException("Партія з номером " + dto.getBatchNumber() + " вже існує");
        }

        // 2. Пошук категорії
        SupplyCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Категорію не знайдено"));

        // 3. Пошук складу (якщо вказано)
        Warehouse warehouse = null;
        if (dto.getWarehouseId() != null) {
            warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Склад не знайдено"));
        }

        // 4. Створення Entity з DTO
        SupplyItem item = new SupplyItem();
        item.setName(dto.getName());
        item.setBatchNumber(dto.getBatchNumber());
        item.setCategory(category);
        item.setQuantity(dto.getQuantity());
        item.setUnit(dto.getUnit());
        item.setExpirationDate(dto.getExpirationDate());
        item.setHazardClass(dto.getHazardClass());
        item.setStorageConditions(dto.getStorageConditions());
        item.setWarehouse(warehouse);
        item.setStatus(ItemStatus.IN_STOCK); // Статус за замовчуванням

        // 5. Збереження
        SupplyItem savedItem = itemRepository.save(item);
        
        return toResponseDTO(savedItem);
    }

    // ОТРИМАННЯ ВСІХ (READ ALL)
    public List<SupplyItemResponseDTO> getAll() {
        return itemRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ОТРИМАННЯ ОДНОГО (READ ONE)
    public SupplyItemResponseDTO getById(Long id) {
        SupplyItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Матеріал з ID " + id + " не знайдено"));
        return toResponseDTO(item);
    }

    // ДОПОМІЖНИЙ МЕТОД (Entity -> DTO)
    private SupplyItemResponseDTO toResponseDTO(SupplyItem item) {
        SupplyItemResponseDTO dto = new SupplyItemResponseDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setBatchNumber(item.getBatchNumber());
        
        if (item.getCategory() != null) {
            dto.setCategoryId(item.getCategory().getId());
            dto.setCategoryName(item.getCategory().getName());
        }

        dto.setQuantity(item.getQuantity());
        dto.setUnit(item.getUnit());
        dto.setExpirationDate(item.getExpirationDate());
        dto.setHazardClass(item.getHazardClass());
        dto.setStorageConditions(item.getStorageConditions());
        
        if (item.getWarehouse() != null) {
            dto.setWarehouseId(item.getWarehouse().getId());
            dto.setWarehouseName(item.getWarehouse().getName());
        }

        dto.setStatus(item.getStatus());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }
}
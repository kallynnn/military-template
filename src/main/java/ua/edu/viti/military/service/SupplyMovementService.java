package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.MovementRequestDTO;
import ua.edu.viti.military.dto.response.MovementResponseDTO;
import ua.edu.viti.military.entity.*;
import ua.edu.viti.military.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplyMovementService {

    private final SupplyMovementRepository movementRepository;
    private final SupplyItemRepository itemRepository;

    // --- МЕТОД 1: ВИДАЧА ---
    @Transactional
    public MovementResponseDTO issueItem(MovementRequestDTO dto) {
        log.info("Спроба видачі матеріалу ID: {}", dto.getItemId());

        SupplyItem item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Матеріал не знайдено"));

        if (item.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Недостатньо матеріалів на складі!");
        }

        item.setQuantity(item.getQuantity() - dto.getQuantity());
        itemRepository.save(item);

        SupplyMovement movement = new SupplyMovement();
        movement.setItem(item);
        movement.setType(MovementType.ISSUE);
        movement.setQuantity(dto.getQuantity());
        movement.setRecipientName(dto.getRecipientName());
        movement.setRecipientUnit(dto.getRecipientUnit());
        movement.setNotes(dto.getNotes());
        movement.setPerformedBy("System Admin");

        SupplyMovement saved = movementRepository.save(movement);
        return mapToResponse(saved);
    }

    // --- МЕТОД 2: ІСТОРІЯ ---
    public List<MovementResponseDTO> getHistory(Long itemId) {
        return movementRepository.findByItemIdOrderByPerformedAtDesc(itemId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // --- МЕТОД 3: РЕДАГУВАННЯ (PUT) ---
    @Transactional
    public MovementResponseDTO updateMovement(Long id, MovementRequestDTO dto) {
        log.info("Редагування руху ID: {}", id);

        SupplyMovement movement = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запис руху не знайдено"));

        SupplyItem item = movement.getItem();

        // Якщо змінилася кількість, коригуємо склад
        if (!movement.getQuantity().equals(dto.getQuantity())) {
            int difference = dto.getQuantity() - movement.getQuantity();

            if (movement.getType() == MovementType.ISSUE) {
                // Якщо видали більше -> на складі меншає
                if (difference > 0 && item.getQuantity() < difference) {
                    throw new RuntimeException("Неможливо збільшити кількість видачі: недостатньо на складі!");
                }
                item.setQuantity(item.getQuantity() - difference);
            }
        }

        movement.setQuantity(dto.getQuantity());
        movement.setRecipientName(dto.getRecipientName());
        movement.setRecipientUnit(dto.getRecipientUnit());
        movement.setNotes(dto.getNotes());

        itemRepository.save(item);
        SupplyMovement updated = movementRepository.save(movement);
        return mapToResponse(updated);
    }

    // --- МЕТОД 4: ВИДАЛЕННЯ (DELETE) ---
    @Transactional
    public void deleteMovement(Long id) {
        log.info("Видалення руху ID: {}", id);

        SupplyMovement movement = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запис руху не знайдено"));

        SupplyItem item = movement.getItem();

        // Повертаємо товари на склад
        if (movement.getType() == MovementType.ISSUE) {
            item.setQuantity(item.getQuantity() + movement.getQuantity());
        }

        itemRepository.save(item);
        movementRepository.delete(movement);
    }

    // --- ДОПОМІЖНИЙ МЕТОД ---
    private MovementResponseDTO mapToResponse(SupplyMovement movement) {
        MovementResponseDTO dto = new MovementResponseDTO();
        dto.setId(movement.getId());
        dto.setItemId(movement.getItem().getId());
        dto.setItemName(movement.getItem().getName());
        dto.setType(movement.getType());
        dto.setQuantity(movement.getQuantity());
        dto.setRecipientName(movement.getRecipientName());
        dto.setRecipientUnit(movement.getRecipientUnit());
        dto.setNotes(movement.getNotes());
        dto.setPerformedAt(movement.getPerformedAt());
        return dto;
    }
}
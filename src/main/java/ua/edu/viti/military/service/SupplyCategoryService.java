package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.entity.SupplyCategory;
import ua.edu.viti.military.repository.SupplyCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplyCategoryService {

    private final SupplyCategoryRepository categoryRepository;

    @Transactional
    public SupplyCategory create(String name, String code, String description) {
        SupplyCategory category = new SupplyCategory();
        category.setName(name);
        category.setCode(code);
        category.setDescription(description);
        category.setRequiresColdStorage(false);
        return categoryRepository.save(category);
    }

    public List<SupplyCategory> getAll() {
        return categoryRepository.findAll();
    }
}
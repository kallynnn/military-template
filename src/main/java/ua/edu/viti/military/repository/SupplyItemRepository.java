package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.HazardClass;
import ua.edu.viti.military.entity.ItemStatus;
import ua.edu.viti.military.entity.SupplyItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {

    // 1. Пошук по унікальному ідентифікатору (партія)
    Optional<SupplyItem> findByBatchNumber(String batchNumber);
    boolean existsByBatchNumber(String batchNumber);

    // 2. Фільтрація
    List<SupplyItem> findByStatus(ItemStatus status);
    List<SupplyItem> findByCategoryId(Long categoryId);
    
    // Комбінований фільтр
    List<SupplyItem> findByStatusAndCategoryId(ItemStatus status, Long categoryId);

    // 3. Специфічні для Варіанту А
    // Знайти прострочені або ті, що скоро зіпсуються
    List<SupplyItem> findByExpirationDateBefore(LocalDate date);

    // Знайти небезпечні матеріали
    List<SupplyItem> findByHazardClass(HazardClass hazardClass);

    // 4. Оптимізований запит з JOIN FETCH (вимога завдання)
    // Завантажує Item разом з Category одним запитом, щоб не було N+1 проблеми
    @Query("SELECT i FROM SupplyItem i JOIN FETCH i.category WHERE i.warehouse.id = :warehouseId")
    List<SupplyItem> findAllByWarehouseIdWithCategory(@Param("warehouseId") Long warehouseId);
}
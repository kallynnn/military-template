package ua.edu.viti.military.exception; // <--- Додайте це

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Це дасть помилку 409 Conflict
public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
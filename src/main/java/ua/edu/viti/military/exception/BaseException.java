package ua.edu.viti.military.exception; // <--- Цього рядка у вас не вистачає!

public abstract class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }
}
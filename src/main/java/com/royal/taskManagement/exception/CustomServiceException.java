package com.royal.taskManagement.exception;

/**
 * Кастомное исключение для обработки ошибок в сервисном слое приложения.
 * Это исключение расширяет {@link RuntimeException} и позволяет передавать
 * специфические сообщения об ошибках и их причины в виде {@link Throwable}.
 */
public class CustomServiceException extends RuntimeException {

    /**
     * Конструктор, который создает исключение с заданным сообщением.
     *
     * @param message сообщение, которое описывает ошибку.
     */
    public CustomServiceException(String message) {
        super(message);
    }

    /**
     * Конструктор, который создает исключение с заданным сообщением и причиной.
     *
     * @param message сообщение, которое описывает ошибку.
     * @param cause   причина возникновения исключения.
     */
    public CustomServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

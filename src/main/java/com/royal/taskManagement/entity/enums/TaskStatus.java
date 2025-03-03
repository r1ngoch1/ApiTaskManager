package com.royal.taskManagement.entity.enums;

/**
 * Перечисление для представления статусов задачи.
 * Каждому статусу задачи соответствует строковое описание для отображения.
 */
public enum TaskStatus {

    PENDING("в ожидании"),

    IN_PROGRESS("в процессе"),

    COMPLETED("завершено");

    /**
     * Строковое описание статуса для отображения.
     */
    private final String displayName;

    /**
     * Конструктор для задания отображаемого имени статуса.
     *
     * @param displayName строковое описание статуса задачи.
     */
    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Получить строковое описание статуса для отображения.
     *
     * @return строковое описание статуса.
     */
    public String getDisplayName() {
        return displayName;
    }
}

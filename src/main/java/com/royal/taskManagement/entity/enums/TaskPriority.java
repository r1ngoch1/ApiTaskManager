package com.royal.taskManagement.entity.enums;

/**
 * Перечисление для представления приоритетов задачи.
 * Каждому приоритету задачи соответствует строковое описание для отображения.
 */
public enum TaskPriority {

    LOW("низкий"),

    MEDIUM("средний"),

    HIGH("высокий");

    /**
     * Строковое описание приоритета для отображения.
     */
    private final String displayName;

    /**
     * Конструктор для задания отображаемого имени приоритета.
     *
     * @param displayName строковое описание приоритета задачи.
     */
    TaskPriority(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Получить строковое описание приоритета для отображения.
     *
     * @return строковое описание приоритета.
     */
    public String getDisplayName() {
        return displayName;
    }
}

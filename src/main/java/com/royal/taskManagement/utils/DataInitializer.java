package com.royal.taskManagement.utils;

import com.royal.taskManagement.entity.Role;
import com.royal.taskManagement.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Класс инициализирует базовые данные (роли) при запуске приложения.
 * Реализует {@link CommandLineRunner}, чтобы автоматически создавать роли "ADMIN" и "USER",
 * если они не существуют в базе данных.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    /**
     * Конструктор инициализатора данных.
     *
     * @param roleRepository Репозиторий для работы с ролями.
     */
    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Метод выполняется при старте приложения.
     * Проверяет наличие ролей "ADMIN" и "USER" в базе данных. Если они не существуют, создает их.
     *
     * @param args аргументы командной строки, передаваемые при запуске.
     * @throws Exception если возникла ошибка при сохранении ролей.
     */
    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("USER") == null) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
    }
}

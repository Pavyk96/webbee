import service.LogService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String inputDir = "log_input";    // путь к директории с входными .log файлами
        String outputDir = "log_output";  // путь, куда сохранить файлы пользователей

        LogService logService = new LogService(inputDir, outputDir);
        try {
            logService.processAllLogs();
            System.out.println("Логи обработаны успешно.");
        } catch (IOException e) {
            System.err.println("Ошибка обработки логов: " + e.getMessage());
        }
    }
}

package writer;

import model.LogEntry;
import model.UserLogFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Создаёт .log-файл по данным из UserLogFile в указанной директории.
 */
public class UserLogWriter {

    /**
     * Сохраняет логи и финальный баланс в .log-файл в указанной директории.
     *
     * @param userLog данные пользователя
     * @param outputDirPath путь к директории
     * @throws IOException при ошибке записи
     */
    public static void writeUserLogFile(UserLogFile userLog, String outputDirPath) throws IOException {
        String filename = userLog.getUser() + ".log";
        File file = new File(outputDirPath, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            List<LogEntry> logs = userLog.getLogs();

            for (LogEntry log : logs) {
                writer.write(log.toLogLine());
                writer.newLine();
            }

            writer.write(userLog.getFinalBalanceLogLine());
            writer.newLine();
        }
    }
}

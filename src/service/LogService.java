package service;

import aggregator.UserLogAggregator;
import model.LogEntry;
import model.UserLogFile;
import parser.LogParser;
import processor.LogProcessor;
import reader.LogFileReader;
import writer.UserLogWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Сервисный класс для обработки логов пользователей.
 * <p>
 * Использует модули:
 * <ul>
 *     <li>{@link LogFileReader} — для считывания файлов логов</li>
 *     <li>{@link LogParser} — для парсинга строк логов в {@link LogEntry}</li>
 *     <li>{@link UserLogAggregator} — для агрегации логов по пользователям</li>
 *     <li>{@link LogProcessor} — для сортировки и подсчёта баланса</li>
 *     <li>{@link UserLogWriter} — для записи логов по каждому пользователю в файл</li>
 * </ul>
 */
public class LogService {

    private final LogFileReader logFileReader;
    private final LogParser logParser;
    private final String outputDir;

    public LogService(String inputDir, String outputDir) {
        this.logFileReader = new LogFileReader(inputDir);
        this.logParser = new LogParser();
        this.outputDir = outputDir;
    }

    /**
     * Обрабатывает все лог-файлы из входной директории и записывает
     * агрегированные результаты по каждому пользователю в выходную директорию.
     *
     * @throws IOException если возникает ошибка при чтении или записи файлов
     */
    public void processAllLogs() throws IOException {
        List<Path> logFiles = logFileReader.getAllLogFiles();

        UserLogAggregator aggregator = new UserLogAggregator();

        for (Path logFile : logFiles) {
            List<String> lines = logFileReader.readLogFile(logFile);

            for (String line : lines) {
                try {
                    LogEntry entry = logParser.parse(line);
                    aggregator.add(entry);
                } catch (IllegalArgumentException e) {
                    System.err.println("Ошибка парсинга строки: " + line + " | " + e.getMessage());
                }
            }
        }

        Map<String, List<LogEntry>> userLogsMap = aggregator.getUserLogs();

        for (Map.Entry<String, List<LogEntry>> entry : userLogsMap.entrySet()) {
            String user = entry.getKey();
            List<LogEntry> logs = entry.getValue();

            UserLogFile processed = LogProcessor.processLogs(logs, user);
            UserLogWriter.writeUserLogFile(processed, outputDir);
        }
    }
}


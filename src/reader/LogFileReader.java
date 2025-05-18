package reader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс для чтения лог-файлов из директории input_log.
 */
public class LogFileReader {

    private final Path inputDir;

    /**
     * @param inputDirectoryName имя директории с логами
     */
    public LogFileReader(String inputDirectoryName) {
        this.inputDir = Paths.get(inputDirectoryName);
    }

    /**
     * Возвращает список всех .log файлов в директории.
     *
     * @return список путей к лог-файлам
     * @throws IOException если директория не существует или ошибка чтения
     */
    public List<Path> getAllLogFiles() throws IOException {
        if (!Files.exists(inputDir) || !Files.isDirectory(inputDir)) {
            throw new IOException("Директория " + inputDir + " не существует или не является директорией.");
        }

        try (Stream<Path> paths = Files.list(inputDir)) {
            return paths
                    .filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".log"))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Читает все строки из указанного лог-файла.
     *
     * @param logFile путь к лог-файлу
     * @return список строк файла
     * @throws IOException если файл не найден или не удаётся прочитать
     */
    public List<String> readLogFile(Path logFile) throws IOException {
        return Files.readAllLines(logFile);
    }

    /**
     * Читает все строки из всех файлов.
     *
     * @return список строк файлов
     * @throws IOException если файл не найден или не удаётся прочитать
     */
    public List<String> readAllLinesFromAllFiles() throws IOException {
        List<Path> logFiles = getAllLogFiles();
        List<String> allLines = new ArrayList<>();
        for (Path path : logFiles) {
            allLines.addAll(readLogFile(path));
        }
        return allLines;
    }
}




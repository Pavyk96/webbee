package parser;

import model.LogEntry;
import model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Парсер логов
 */
public class LogParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * парсит строку лога, в сущность
     *
     * @param line строка лога
     * @return сущность Лога
     */
    public LogEntry parse(String line) {
        String originalLine = line;
        line = line.trim();
        int endOfTimestamp = line.indexOf("]");
        if (endOfTimestamp == -1 || !line.startsWith("[")) {
            throw new IllegalArgumentException("Некорректная строка: " + originalLine);
        }

        String timestampStr = line.substring(1, endOfTimestamp);
        LocalDateTime timestamp = LocalDateTime.parse(timestampStr, FORMATTER);

        String content = line.substring(endOfTimestamp + 1).trim();
        String[] parts = content.split(" ");

        if (parts.length < 3) {
            throw new IllegalArgumentException("Недостаточно данных в строке: " + originalLine);
        }

        String actorUser = parts[0];
        String action = parts[1];

        return switch (action) {
            case "transferred" -> {
                if (parts.length < 5 || !"to".equals(parts[3]))
                    throw new IllegalArgumentException("Неверный формат перевода: " + originalLine);
                BigDecimal amount = new BigDecimal(parts[2]);
                String targetUser = parts[4];
                yield new LogEntry(timestamp, actorUser, OperationType.TRANSFERRED, amount, targetUser, originalLine);
            }
            case "withdrew" -> {
                BigDecimal amount = new BigDecimal(parts[2]);
                yield new LogEntry(timestamp, actorUser, OperationType.WITHDREW, amount, null, originalLine);
            }
            case "balance" -> {
                if (!"inquiry".equals(parts[2]))
                    throw new IllegalArgumentException("Неверный формат запроса баланса: " + originalLine);
                BigDecimal amount = new BigDecimal(parts[3]);
                yield new LogEntry(timestamp, actorUser, OperationType.BALANCE_INQUIRY, amount, null, originalLine);
            }
            default -> throw new IllegalArgumentException("Неизвестная операция: " + originalLine);
        };
    }
}


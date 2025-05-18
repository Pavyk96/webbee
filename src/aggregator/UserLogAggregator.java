package aggregator;

import model.LogEntry;

import java.util.List;
import java.util.Map;
import model.OperationType;
import java.util.*;

/**
 * Агрегирует лог-записи по пользователям.
 * При передаче добавляет как отправителя, так и получателя.
 */
public class UserLogAggregator {
    private final Map<String, List<LogEntry>> userLogs = new HashMap<>();

    /**
     * Добавляет лог-запись к пользователю.
     * Если это перевод, добавляет запись получателю.
     *
     * @param entry лог-запись
     */
    public void add(LogEntry entry) {
        addEntry(entry.actorUser(), entry);

        if (entry.type() == OperationType.TRANSFERRED && entry.targetUser() != null) {
            LogEntry receivedEntry = new LogEntry(
                    entry.timestamp(),
                    entry.targetUser(),
                    OperationType.RECEIVED,
                    entry.amount(),
                    entry.actorUser(),
                    entry.originalLine()
            );
            addEntry(entry.targetUser(), receivedEntry);
        }
    }

    private void addEntry(String user, LogEntry entry) {
        userLogs.computeIfAbsent(user, u -> new ArrayList<>()).add(entry);
    }

    /**
     * Возвращает карту логов, сгруппированных по пользователям.
     *
     * @return отображение: пользователь → список логов
     */
    public Map<String, List<LogEntry>> getUserLogs() {
        return userLogs;
    }
}




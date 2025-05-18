package processor;

import model.LogEntry;
import model.UserLogFile;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Обработчик логов конкретного пользователя
 */
public class LogProcessor {

    /**
     * Обрабатывает список логов конкретного пользователя: сортирует по времени и считает баланс.
     *
     * @param logs список операций (предполагается, что все от нужного пользователя)
     * @param user имя пользователя
     * @return объект с пользователем, отсортированными логами и его балансом
     */
    public static UserLogFile processLogs(List<LogEntry> logs, String user) {
        List<LogEntry> sortedLogs = logs.stream()
                .sorted(Comparator.comparing(LogEntry::timestamp))
                .collect(Collectors.toList());

        BigDecimal balance = BigDecimal.ZERO;

        for (LogEntry log : sortedLogs) {
            switch (log.type()) {
                case RECEIVED -> balance = balance.add(log.amount());
                case TRANSFERRED, WITHDREW -> balance = balance.subtract(log.amount());
                case BALANCE_INQUIRY -> {
                    balance = log.amount();
                }
            }
        }

        return new UserLogFile(user, sortedLogs, balance);
    }
}


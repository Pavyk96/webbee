package model;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Представляет логи пользователя с итоговым балансом.
 */
public class UserLogFile {
    private String user;
    private List<LogEntry> logs;
    private BigDecimal money;

    public UserLogFile(String user, List<LogEntry> logs, BigDecimal money) {
        this.user = user;
        this.logs = logs;
        this.money = money;
    }

    public String getUser() {
        return user;
    }

    public List<LogEntry> getLogs() {
        return logs;
    }

    public BigDecimal getMoney() {
        return money;
    }

    /**
     * Формирует строку для вывода финального баланса пользователя.
     *
     * @return строка с финальным балансом
     */
    public String getFinalBalanceLogLine() {
        String ts = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(java.time.LocalDateTime.now());
        return String.format("[%s] %s final balance %s",
                ts,
                user,
                money.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
    }
}


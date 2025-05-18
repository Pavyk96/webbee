package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Запись лога с деталями операции пользователя.
 *
 * @param timestamp    дата и время операции
 * @param actorUser    пользователь, инициировавший операцию
 * @param type         тип операции
 * @param amount       сумма операции
 * @param targetUser   получатель (если применимо)
 * @param originalLine оригинальная строка лога
 */
public record LogEntry(
        LocalDateTime timestamp,
        String actorUser,
        OperationType type,
        BigDecimal amount,
        String targetUser,
        String originalLine
) {
    /**
     * Преобразует лог-запись в строку для вывода в файл.
     *
     * @return строковое представление лог-записи
     */
    public String toLogLine() {
        String timestampStr = "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "]";
        return switch (type) {
            case BALANCE_INQUIRY -> String.format("%s %s balance inquiry %s", timestampStr, actorUser, format(amount));
            case TRANSFERRED     -> String.format("%s %s transferred %s to %s", timestampStr, actorUser, format(amount), targetUser);
            case RECEIVED        -> String.format("%s %s recived  %s from %s", timestampStr, actorUser, format(amount), targetUser);
            case WITHDREW        -> String.format("%s %s withdrew %s", timestampStr, actorUser, format(amount));
        };
    }

    private String format(BigDecimal amount) {
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }
}

package util;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateLocalDateConverter {
    public static LocalDateTime convertDateToLocalDate(Timestamp date) {
        return new Date(date.getTime()).toLocalDate().atTime(LocalTime.from(LocalDateTime.now()));
    }

    public static Timestamp convertLocalDateToDate(LocalDateTime localDate) {
        return Timestamp.valueOf(localDate);
    }
}
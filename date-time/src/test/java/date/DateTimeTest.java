package date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Locale;

import org.junit.jupiter.api.Test;

public class DateTimeTest {
    @Test
    void LocalDateTest() {
        LocalDate currentLocalDate = LocalDate.now();
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        Instant currentInstant = Instant.now();
        LocalTime currentLocalTime = LocalTime.now();
        System.out.printf("Current local date is %s in ISO format\n", currentLocalDate);
        System.out.printf("Current local time is %s in ISO format\n", currentLocalTime);
        System.out.printf("Current local date and time is %s in ISO format\n", currentLocalDateTime);
        System.out.printf("Current zoned date and time is %s in ISO format\n", currentZonedDateTime);
        System.out.printf("Current instant is %s in ISO format\n", currentInstant);
        System.out.printf("Current local date is %s in dd/month/yyyy\n",
                currentLocalDate.format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy", Locale.forLanguageTag("en"))));
    }

    @Test
    void nextFriday13Test() {
        LocalDate current = LocalDate.of(2024, 8, 11);
        LocalDate expected = LocalDate.of(2024, 9, 13);
        TemporalAdjuster adjuster = new NextFriday13();
        assertEquals(expected, current.with(new NextFriday13()));
        assertThrows(RuntimeException.class, () -> LocalTime.now().with(adjuster));
    }

      @Test
    void pastTemporalDateProximityTest() {
        LocalDate date10Jan = LocalDate.of(2024, 1, 10);
        LocalDate date15Mar = LocalDate.of(2024, 3, 15);
        LocalDate date20Apr = LocalDate.of(2024, 4, 20);
        LocalDate date25May = LocalDate.of(2024, 5, 25);
        LocalDate date30Jun = LocalDate.of(2024, 6, 30);

        PastTemporalDateProximity adjuster = new PastTemporalDateProximity(new Temporal[]{
            date20Apr, date25May, date10Jan
        });

        assertNull(date10Jan.with(adjuster));
        assertEquals(date10Jan, date15Mar.with(adjuster));
        assertEquals(date10Jan, date20Apr.with(adjuster));
        assertEquals(date20Apr, date25May.with(adjuster));
        assertEquals(date25May, date30Jun.with(adjuster));

        assertEquals(MinguoDate.from(date10Jan), MinguoDate.from(date20Apr).with(adjuster));
        assertEquals(JapaneseDate.from(date10Jan), JapaneseDate.from(date20Apr).with(adjuster));

        assertThrows(RuntimeException.class, () -> LocalTime.now().with(adjuster));
    }

}
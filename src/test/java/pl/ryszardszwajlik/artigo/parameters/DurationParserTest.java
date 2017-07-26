package pl.ryszardszwajlik.artigo.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.security.InvalidParameterException;
import java.time.Duration;
import org.junit.Test;

public class DurationParserTest
{
    @Test
    public void shouldParseMilliseconds()
    {
        // given
        String durationAsString = "1ms";

        // when
        Duration parsedDuration = new DurationParser().parse(durationAsString);

        // then
        assertThat(parsedDuration.getNano()).isEqualTo(1000000);
        assertThat(parsedDuration.getSeconds()).isEqualTo(0);
    }

    @Test
    public void shouldParseSeconds()
    {
        // given
        String durationAsString = "1s";

        // when
        Duration parsedDuration = new DurationParser().parse(durationAsString);

        // then
        assertThat(parsedDuration.getNano()).isEqualTo(0);
        assertThat(parsedDuration.getSeconds()).isEqualTo(1);
    }

    @Test
    public void shouldParseMinutes()
    {
        // given
        String durationAsString = "1m";

        // when
        Duration parsedDuration = new DurationParser().parse(durationAsString);

        // then
        assertThat(parsedDuration.getNano()).isEqualTo(0);
        assertThat(parsedDuration.getSeconds()).isEqualTo(60);
    }

    @Test
    public void shouldThrowExceptionWhenInvalidFormatGiven()
    {
        // given
        String durationAsString = "InvalidFormat";

        // when
        Throwable throwable = catchThrowable(() -> new DurationParser().parse(durationAsString));

        // then
        assertThat(throwable)
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("Invalid time format");
    }
}

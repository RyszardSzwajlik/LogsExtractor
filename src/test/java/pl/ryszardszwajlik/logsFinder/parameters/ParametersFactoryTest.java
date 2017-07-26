package pl.ryszardszwajlik.logsFinder.parameters;

import org.junit.Test;

import java.security.InvalidParameterException;
import java.time.Duration;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

public class ParametersFactoryTest
{
    private DurationParser durationParser = mock(DurationParser.class);
    private ParametersFactory parametersFactory = new ParametersFactory(durationParser);

    @Test
    public void shouldThrowExceptionWhenInvalidParametersNumberGiven()
    {
        // given
        String[] args = { "-f" };

        // when
        Throwable thrownException = catchThrowable(() -> parametersFactory.fromArguments(asList(args)));

        // then
        assertThat(thrownException)
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("Invalid arguments given");
    }

    @Test
    public void shouldThrowExceptionWhenFileArgumentNotGiven()
    {
        // given
        String[] args = { "-t", "1s", "-d", "otherArgument" };

        // when
        Throwable thrownException = catchThrowable(() -> parametersFactory.fromArguments(asList(args)));

        // then
        assertThat(thrownException)
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("Invalid arguments given");
    }

    @Test
    public void shouldThrowExceptionWhenTimeArgumentNotGiven()
    {
        // given
        String[] args = { "-f", "someFileName", "-d", "otherArgument" };

        // when
        Throwable thrownException = catchThrowable(() -> parametersFactory.fromArguments(asList(args)));

        // then
        assertThat(thrownException)
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("Invalid arguments given");
    }

    @Test
    public void shouldCreateParametersWithFileAndTime()
    {
        // given
        String expectedFileName = "someFileName";
        String[] args = { "-f", expectedFileName, "-t", "1s" };
        Duration duration = Duration.ofSeconds(1);
        given(durationParser.parse(eq("1s"))).willReturn(duration);

        // when
        Parameters parameters = parametersFactory.fromArguments(asList(args));

        // then
        assertThat(parameters.getFileName()).isEqualTo(expectedFileName);
        assertThat(parameters.getMinimalDurationTime()).isEqualTo(duration);
    }
}

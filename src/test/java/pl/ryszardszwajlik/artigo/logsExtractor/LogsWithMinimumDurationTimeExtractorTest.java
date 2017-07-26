package pl.ryszardszwajlik.artigo.logsExtractor;

import org.junit.Test;
import org.mockito.Mockito;
import pl.ryszardszwajlik.artigo.parameters.Parameters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Queue;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

public class LogsWithMinimumDurationTimeExtractorTest
{
    @Test
    public void shouldSelectOnlyLinesHigherOrEqualWithMinimumRequestTime() throws Exception
    {
        // given
        File tempFile = createTemporaryFileWithData(
                createServiceLogWithDates("2017-07-26T10:15:30.000", "2017-07-26T10:15:32.000", "2s"),
                createServiceLogWithDates("2017-07-26T10:15:30.000", "2017-07-26T10:15:31.000", "1s"),
                createServiceLogWithDates("2017-07-26T10:15:30.000", "2017-07-26T10:15:30.500", "500ms")
        );
        Parameters parameters = Mockito.mock(Parameters.class);
        when(parameters.getFileName()).thenReturn(tempFile.getAbsolutePath());
        int minimumRequestTime = 1;
        when(parameters.getMinimalDurationTime()).thenReturn(Duration.ofSeconds(minimumRequestTime));
        Queue<String> queue = new ArrayDeque<>();

        // when
        new LogsWithMinimumDurationTimeExtractor(parameters, queue).call();

        // then
        assertThat(queue).hasSize(2);
        for (String selectedLine : queue)
        {
            assertThat(selectedLine.contains("1s") || selectedLine.contains("2s")).isTrue();
        }
    }

    @Test
    public void shouldOmitUnsupportedDateFormat() throws Exception
    {
        // given
        File tempFile = createTemporaryFileWithData(
                createServiceLogWithDates("unsupported", "unsupported", "2s")
        );
        Parameters parameters = Mockito.mock(Parameters.class);
        when(parameters.getFileName()).thenReturn(tempFile.getAbsolutePath());
        int minimumRequestTime = 1;
        when(parameters.getMinimalDurationTime()).thenReturn(Duration.ofSeconds(minimumRequestTime));
        Queue<String> queue = new ArrayDeque<>();

        // when
        new LogsWithMinimumDurationTimeExtractor(parameters, queue).call();

        // then
        assertThat(queue).hasSize(0);
    }

    private File createTemporaryFileWithData(String... serviceLogLines) throws IOException
    {
        File testLogsFile = File.createTempFile("TestLogs", ".log");
        FileWriter fileWriter = new FileWriter(testLogsFile, true);
        try (BufferedWriter writer = new BufferedWriter(fileWriter))
        {
            for (String serviceLogLine : serviceLogLines)
            {
                writer.write(serviceLogLine + "\n");
            }
        }
        return testLogsFile;
    }

    private String createServiceLogWithDates(String startDate, String endDate, String description)
    {
        return "<service><diagnostics><traces start=\"" +
                startDate +
                "\" end=\"" +
                endDate +
                "\" /></diagnostics><description>" +
                description +
                "</description></service>";
    }
}
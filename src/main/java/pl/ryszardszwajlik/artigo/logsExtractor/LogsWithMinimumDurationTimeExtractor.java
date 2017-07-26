package pl.ryszardszwajlik.artigo.logsExtractor;

import pl.ryszardszwajlik.artigo.parameters.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LogsWithMinimumDurationTimeExtractor implements Callable<Void>
{
    public static final String EXIT_MESSAGE = "LogsFinderExitMessage" + System.currentTimeMillis();

    private static final String ANY_ISO_DATE_CHARACTERS = "((\\d|-|T|:|\\.)*)";
    private static final Pattern startTimePattern = Pattern.compile(".*start=\"" + ANY_ISO_DATE_CHARACTERS + "\".*");
    private static final Pattern endTimePattern = Pattern.compile(".*end=\"" + ANY_ISO_DATE_CHARACTERS + "\".*");

    private final Parameters parameters;
    private final Queue<String> queue;

    public LogsWithMinimumDurationTimeExtractor(Parameters parameters, Queue<String> queue)
    {
        this.parameters = parameters;
        this.queue = queue;
    }

    @Override
    public Void call()
    {
        try (Stream<String> stream = Files.lines(Paths.get(parameters.getFileName())))
        {
            stream.parallel()
                    .filter(this::hasMinimumDurationTime)
                    .forEach(queue::offer);
        }
        catch (IOException e)
        {
            System.out.println("Could not load file: " + parameters.getFileName());
        }
        finally
        {
            queue.offer(EXIT_MESSAGE);
        }
        return null;
    }

    private boolean hasMinimumDurationTime(String line)
    {
        Optional<LocalDateTime> startTime = getTime(startTimePattern, line);
        Optional<LocalDateTime> endTime = getTime(endTimePattern, line);
        if (startTime.isPresent() && endTime.isPresent())
        {
            Duration duration = Duration.between(startTime.get(), endTime.get());
            return duration.compareTo(parameters.getMinimalDurationTime()) >= 0;
        }
        return false;
    }

    private Optional<LocalDateTime> getTime(Pattern timePattern, String line)
    {
        Matcher timeMatcher = timePattern.matcher(line);
        if (timeMatcher.matches())
        {
            return Optional.of(LocalDateTime.parse(timeMatcher.group(1)));
        }
        return Optional.empty();
    }
}

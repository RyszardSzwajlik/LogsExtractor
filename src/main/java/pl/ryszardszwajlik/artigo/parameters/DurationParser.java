package pl.ryszardszwajlik.artigo.parameters;

import org.springframework.stereotype.Component;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class DurationParser
{
    private String TIME = "(\\d+)";
    private String UNIT = "(\\w{1,2})";
    private Pattern DURATION_PATTERN = Pattern.compile(TIME + UNIT);

    Duration parse(String durationAsString)
    {
        return parseDuration(durationAsString)
                .orElseThrow(() -> new InvalidParameterException("Invalid time format"));
    }

    private Optional<Duration> parseDuration(String durationAsString)
    {
        Matcher matcher = DURATION_PATTERN.matcher(durationAsString);
        if (matcher.matches())
        {
            int duration = Integer.parseInt(matcher.group(1));

            switch (matcher.group(2))
            {
                case "ms":
                    return Optional.of(Duration.ofMillis(duration));
                case "s":
                    return Optional.of(Duration.ofSeconds(duration));
                case "m":
                    return Optional.of(Duration.ofMinutes(duration));
            }
        }
        return Optional.empty();
    }
}

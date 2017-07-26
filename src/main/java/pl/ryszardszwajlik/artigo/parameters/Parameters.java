package pl.ryszardszwajlik.artigo.parameters;

import java.time.Duration;

public class Parameters
{
    private final String fileName;
    private final Duration minimalDurationTime;

    Parameters(String fileName, Duration minimalDurationTime)
    {
        this.fileName = fileName;
        this.minimalDurationTime = minimalDurationTime;
    }

    public String getFileName()
    {
        return fileName;
    }

    public Duration getMinimalDurationTime()
    {
        return minimalDurationTime;
    }
}

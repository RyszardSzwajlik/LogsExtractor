package pl.ryszardszwajlik.artigo.parameters;

import java.time.Duration;

public class Parameters
{
    private final String fileName;
    private final Duration minimalTime;

    Parameters(String fileName, Duration minimalTime)
    {
        this.fileName = fileName;
        this.minimalTime = minimalTime;
    }

    public String getFileName()
    {
        return fileName;
    }

    public Duration getMinimalTime()
    {
        return minimalTime;
    }
}

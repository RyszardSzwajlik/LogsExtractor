package pl.ryszardszwajlik.logsFinder.logsWriter;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class LogsWriter
{
    public void logUntilPoisonPill(BlockingQueue<String> queue, String poisonPill) throws InterruptedException
    {
        String line;
        while (!(line = queue.take()).equals(poisonPill))
        {
            System.out.println(line);
        }
    }
}

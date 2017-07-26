package pl.ryszardszwajlik.artigo.logsWriter;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class LogsWriter
{
    public void logUntilPoisonPill(BlockingQueue<String> queue, String poisonPill) throws InterruptedException
    {
        for (String line = ""; !line.equals(poisonPill); line = queue.take())
        {
            System.out.println(line);
        }
    }
}

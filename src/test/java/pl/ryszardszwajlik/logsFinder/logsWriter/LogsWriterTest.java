package pl.ryszardszwajlik.logsFinder.logsWriter;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LogsWriterTest
{
    private ByteArrayOutputStream outStream;

    @Before
    public void setUp() throws Exception
    {
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
    }

    @Test
    public void shouldWriteUntilPoisonPill() throws Exception
    {
        // given
        String poisonPill = "POISON_PILL";
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.put("one");
        queue.put("two");
        queue.put("three");
        queue.put(poisonPill);
        queue.put("four");

        // when
        new LogsWriter().logUntilPoisonPill(queue, poisonPill);

        // then
        assertThat(outStream.toString()).contains("one");
        assertThat(outStream.toString()).contains("two");
        assertThat(outStream.toString()).contains("three");
        assertThat(outStream.toString()).doesNotContain(poisonPill);
        assertThat(outStream.toString()).doesNotContain("four");
    }
}
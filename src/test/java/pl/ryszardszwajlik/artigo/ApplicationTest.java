package pl.ryszardszwajlik.artigo;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ApplicationTest
{
    private ByteArrayOutputStream outStream;

    @Before
    public void setUp() throws Exception
    {
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
    }

    @Test
    public void shouldLoadFileAndWrite() throws Exception
    {
        // given
        String fileName = ApplicationTest.class.getClassLoader().getResource("IntegrationTest.log").getFile().substring(1);

        // when
        Application.main(new String[]{"-f", fileName, "-t", "2s"});

        // then
        assertThat(outStream.toString()).contains("2s");
        assertThat(outStream.toString()).doesNotContain("1s");
        assertThat(outStream.toString()).doesNotContain("500ms");
    }

    @Test
    public void shouldEndExecutionWhenProblemWithLoadingFileDetected() throws Exception
    {
        // given
        String fileName = "invalidFileName";

        // when
        Application.main(new String[]{"-f", fileName, "-t", "2s"});

        // then
    }
}
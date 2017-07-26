package pl.ryszardszwajlik.artigo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import pl.ryszardszwajlik.artigo.logsExtractor.LogsWithMinimumDurationTimeExtractor;
import pl.ryszardszwajlik.artigo.logsWriter.LogsWriter;
import pl.ryszardszwajlik.artigo.parameters.Parameters;
import pl.ryszardszwajlik.artigo.parameters.ParametersFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Arrays.asList;

@Component
public class Application
{
    private final ParametersFactory parametersFactory;
    private final LogsWriter logsWriter;

    @Autowired
    public Application(ParametersFactory parametersFactory, LogsWriter logsWriter)
    {
        this.parametersFactory = parametersFactory;
        this.logsWriter = logsWriter;
    }

    public static void main(String[] args)
    {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class.getPackage().getName());
        Application application = applicationContext.getBean(Application.class);
        application.run(args);
    }

    private void run(String[] args)
    {
        Parameters parameters = parametersFactory.fromArguments(asList(args));
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        ExecutorService produceLogsThread = produceLogs(parameters, queue);
//        logsWriter.logUntilExecutorWorks(queue, produceLogsThread);
    }

    private ExecutorService produceLogs(Parameters parameters, BlockingQueue<String> queue)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new LogsWithMinimumDurationTimeExtractor(parameters, queue));
        executor.shutdown();
        return executor;
    }
}

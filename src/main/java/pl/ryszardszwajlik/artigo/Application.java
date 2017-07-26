package pl.ryszardszwajlik.artigo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import pl.ryszardszwajlik.artigo.parameters.Parameters;
import pl.ryszardszwajlik.artigo.parameters.ParametersFactory;

import static java.util.Arrays.asList;

@Component
public class Application
{
    private final ParametersFactory parametersFactory;

    @Autowired
    public Application(ParametersFactory parametersFactory)
    {
        this.parametersFactory = parametersFactory;
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
    }
}

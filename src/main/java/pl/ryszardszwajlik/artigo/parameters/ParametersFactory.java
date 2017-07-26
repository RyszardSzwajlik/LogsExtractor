package pl.ryszardszwajlik.artigo.parameters;

import static pl.ryszardszwajlik.artigo.parameters.ParameterTypes.FILENAME;
import static pl.ryszardszwajlik.artigo.parameters.ParameterTypes.TIME;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ParametersFactory
{
    private DurationParser durationParser;

    @Autowired
    public ParametersFactory(DurationParser durationParser)
    {
        this.durationParser = durationParser;
    }

    public Parameters fromArguments(List<String> arguments)
    {
        validateArguments(arguments);

        Map<String, String> argumentsMap = createArgumentsMap(arguments);
        String fileName = argumentsMap.get(FILENAME.getParameterWithParameterPrefix());
        String durationAsString = argumentsMap.get(TIME.getParameterWithParameterPrefix());
        return new Parameters(fileName, durationParser.parse(durationAsString));
    }

    private Map<String, String> createArgumentsMap(List<String> arguments)
    {
        Map<String, String> argumentsMap = new HashMap<>();
        Iterator<String> argumentsIterator = arguments.iterator();
        while (argumentsIterator.hasNext())
        {
            String argument = argumentsIterator.next();
            String argumentValue = argumentsIterator.next();
            argumentsMap.put(argument, argumentValue);
        }
        return argumentsMap;
    }

    private void validateArguments(List<String> arguments)
    {
        Set<String> argumentsAsHashSet = new HashSet<>(arguments);
        boolean hasFourArguments = argumentsAsHashSet.size() < 4;
        boolean hasFileNameArgument = argumentsAsHashSet.contains(FILENAME.getParameterWithParameterPrefix());
        boolean hasTimeArgument = argumentsAsHashSet.contains(TIME.getParameterWithParameterPrefix());

        if (hasFourArguments || !hasFileNameArgument || !hasTimeArgument)
        {
            throw new InvalidParameterException("Invalid arguments given");
        }
    }
}

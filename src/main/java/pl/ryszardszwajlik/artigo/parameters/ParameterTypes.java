package pl.ryszardszwajlik.artigo.parameters;

enum ParameterTypes
{
    FILENAME("f"), TIME("t");

    private static final char parameterPrefix = '-';

    private final String parameter;
    private String parameterWithPrefix;

    ParameterTypes(String parameter)
    {
        this.parameter = parameter;
    }

    public String getParameterWithParameterPrefix()
    {
        if (parameterWithPrefix == null)
        {
            parameterWithPrefix = parameterPrefix + parameter;
        }
        return parameterWithPrefix;
    }
}

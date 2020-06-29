package pwcg.gui;

public class ScreenIdentifierOverride
{
    
    private ScreenIdentifier screenIdentifier;
    private String configuredImageName;

    public ScreenIdentifierOverride(ScreenIdentifier screenIdentifier)
    {
        this.screenIdentifier = screenIdentifier;
        this.configuredImageName = "";
    }

    public ScreenIdentifier getScreenIdentifier()
    {
        return screenIdentifier;
    }

    public void setScreenIdentifier(ScreenIdentifier screenIdentifier)
    {
        this.screenIdentifier = screenIdentifier;
    }

    public String getConfiguredImageName()
    {
        return configuredImageName;
    }

    public void setConfiguredImageName(String configuredImageName)
    {
        this.configuredImageName = configuredImageName;
    }
}

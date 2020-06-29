package pwcg.gui;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.ScreenIdentifierOverrideIOJson;
import pwcg.core.exception.PWCGException;

public class ScreenIdentifierOverrideManager
{
    private ScreenIdentifierOverrideSet configuredOverrides = new ScreenIdentifierOverrideSet();
    
    private static ScreenIdentifierOverrideManager instance = new ScreenIdentifierOverrideManager();
    
    private ScreenIdentifierOverrideManager()
    {
    }
    
    public static ScreenIdentifierOverrideManager getInstance()
    {
        if (instance.configuredOverrides.getConfiguredOverrides().isEmpty())
        {
            try
            {
                instance.readScreenIdentifierOverrides();
            }
            catch (PWCGException e)
            {
                e.printStackTrace();
            }
        }
        return instance;
    }
    
    public boolean hasOverride(ScreenIdentifier screenIdentifier)
    {
        return configuredOverrides.getConfiguredOverrides().containsKey(screenIdentifier);
    }
    
    public String getOverride(ScreenIdentifier screenIdentifier)
    {
        return configuredOverrides.getConfiguredOverrides().get(screenIdentifier);
    }
    
    public void addOverride(ScreenIdentifierOverride screenIdentifierOverride)
    {
        configuredOverrides.getConfiguredOverrides().put(screenIdentifierOverride.getScreenIdentifier(),screenIdentifierOverride.getConfiguredImageName());
    }

    public void writeScreenIdentifierOverrides() throws PWCGException
    {
        String pwcgInputConfigDir = PWCGContext.getInstance().getDirectoryManager().getPwcgConfigurationDir();
        ScreenIdentifierOverrideIOJson.writeJson(pwcgInputConfigDir, "ScreenImageOverrides.json", configuredOverrides);
    }
    
    private void readScreenIdentifierOverrides() throws PWCGException
    {
        String pwcgInputConfigDir = PWCGContext.getInstance().getDirectoryManager().getPwcgConfigurationDir();
        configuredOverrides = ScreenIdentifierOverrideIOJson.readJson(pwcgInputConfigDir, "ScreenImageOverrides.json");
    }
}

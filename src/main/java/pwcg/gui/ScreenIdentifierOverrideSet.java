package pwcg.gui;

import java.util.HashMap;
import java.util.Map;

public class ScreenIdentifierOverrideSet
{
    private Map<ScreenIdentifier, String> configuredOverrides = new HashMap<>();

    public Map<ScreenIdentifier, String> getConfiguredOverrides()
    {
        return configuredOverrides;
    }

    public void setConfiguredOverrides(Map<ScreenIdentifier, String> configuredOverrides)
    {
        this.configuredOverrides = configuredOverrides;
    }
}

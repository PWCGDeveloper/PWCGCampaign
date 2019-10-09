package pwcg.core.config;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class ConfigManagerGlobal extends ConfigManager
{
    static protected ConfigManagerGlobal instance = null;
	public ConfigManagerGlobal ()
	{
        super(PWCGContext.getInstance().getDirectoryManager().getPwcgUserDir());
	}

	public static ConfigManagerGlobal getInstance() throws PWCGException 
	{
		if (instance == null)
		{
		    instance = new ConfigManagerGlobal();	
		    instance.initialize();
		}

		return instance;
	}

	public void initialize() throws PWCGException 
	{
	    defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetGUI, ConfigSetGUI.initialize());
	    defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetUserPref, ConfigSetUserPref.initialize());

        readConfig();
	}
}

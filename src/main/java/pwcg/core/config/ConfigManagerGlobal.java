package pwcg.core.config;

import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.core.exception.PWCGException;

public class ConfigManagerGlobal extends ConfigManager
{
    static protected ConfigManagerGlobal instance = null;
	public ConfigManagerGlobal ()
	{
        super(PWCGDirectoryUserManager.getInstance().getPwcgCoopDir());
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

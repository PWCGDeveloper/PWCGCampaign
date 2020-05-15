package pwcg.core.config;

public class ConfigSetSimple
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetSimple);
		
        configSet.addConfigItem(ConfigItemKeys.SimpleConfigAirKey, new ConfigItem(ConfigSimple.CONFIG_LEVEL_LOW));
        configSet.addConfigItem(ConfigItemKeys.SimpleConfigGroundKey, new ConfigItem(ConfigSimple.CONFIG_LEVEL_MED));
        configSet.addConfigItem(ConfigItemKeys.SimpleConfigAAKey, new ConfigItem(ConfigSimple.CONFIG_LEVEL_MED));
        
		return configSet;
	}
}

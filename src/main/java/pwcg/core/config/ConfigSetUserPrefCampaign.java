package pwcg.core.config;

public class ConfigSetUserPrefCampaign
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetUserPrefCampaign);

		configSet.addConfigItem(ConfigItemKeys.MovingFrontKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.AARMethodKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.PilotInjuryKey, new ConfigItem("2"));
        configSet.addConfigItem(ConfigItemKeys.AiStupidityDeathOddsKey, new ConfigItem("30"));
        configSet.addConfigItem(ConfigItemKeys.MissionsCreditedKey, new ConfigItem("1"));    
        configSet.addConfigItem(ConfigItemKeys.ShowAllFlightsInBreifingKey, new ConfigItem("0"));
		return configSet;    
	}
}

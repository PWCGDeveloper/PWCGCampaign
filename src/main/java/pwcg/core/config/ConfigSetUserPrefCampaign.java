package pwcg.core.config;

public class ConfigSetUserPrefCampaign
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetUserPrefCampaign);

        configSet.addConfigItem(ConfigItemKeys.CrewMemberInjuryKey, new ConfigItem("2"));
        configSet.addConfigItem(ConfigItemKeys.DetailedVictoryDescriptionKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.MissionsCreditedKey, new ConfigItem("1"));    
        configSet.addConfigItem(ConfigItemKeys.ShowAllUnitsInBreifingKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.RemoveNonHistoricalSquadronsKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.SaveStupidAiKey, new ConfigItem("0"));
		return configSet;    
	}
}

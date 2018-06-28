package pwcg.core.config;

public class ConfigSetCampaign
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetCampaign);

		configSet.addConfigItem(ConfigItemKeys.NewPilotRankOddsLowestKey, new ConfigItem("15"));		//Odds that a new pilot will be lowest possible rank
		configSet.addConfigItem(ConfigItemKeys.NewPilotRankOddsLowKey, new ConfigItem("25"));		//Odds that a new pilot will be the second lowest or lowest possible rank
		configSet.addConfigItem(ConfigItemKeys.BaseOddsOfDeathKey, new ConfigItem("30"));				// Base chance of death for new pilots
		
		return configSet;
	}
}

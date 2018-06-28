package pwcg.core.config;

public class ConfigSetMissionAI
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetMissionAi);

		configSet.addConfigItem(ConfigItemKeys.FighterAISkillAdjustmentKey, new ConfigItem("0"));
		configSet.addConfigItem(ConfigItemKeys.BomberAISkillAdjustmentKey, new ConfigItem("0"));

		return configSet;		
	}
}

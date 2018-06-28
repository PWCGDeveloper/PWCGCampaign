package pwcg.core.config;

public class ConfigSetMissionSpacing
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetMissionSpacing);

		configSet.addConfigItem(ConfigItemKeys.MissionSpacing01MinKey, new ConfigItem("4"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing01MaxAdditionalKey, new ConfigItem("12"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing02MinKey, new ConfigItem("4"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing02MaxAdditionalKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing03MinKey, new ConfigItem("3"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing03MaxAdditionalKey, new ConfigItem("8"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing04MinKey, new ConfigItem("1"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing04MaxAdditionalKey, new ConfigItem("4"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing05MinKey, new ConfigItem("1"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing05MaxAdditionalKey, new ConfigItem("3"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing06MinKey, new ConfigItem("1"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing06MaxAdditionalKey, new ConfigItem("3"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing07MinKey, new ConfigItem("1"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing07MaxAdditionalKey, new ConfigItem("3"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing08MinKey, new ConfigItem("1"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing08MaxAdditionalKey, new ConfigItem("3"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing09MinKey, new ConfigItem("1"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing09MaxAdditionalKey, new ConfigItem("3"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing10MinKey, new ConfigItem("1"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing10MaxAdditionalKey, new ConfigItem("4"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing11MinKey, new ConfigItem("3"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing11MaxAdditionalKey, new ConfigItem("7"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing12MinKey, new ConfigItem("4"));
		configSet.addConfigItem(ConfigItemKeys.MissionSpacing12MaxAdditionalKey, new ConfigItem("10"));
		return configSet;
	}
}

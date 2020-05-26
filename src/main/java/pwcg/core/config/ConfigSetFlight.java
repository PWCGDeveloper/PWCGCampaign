package pwcg.core.config;

public class ConfigSetFlight
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetFlight);

        configSet.addConfigItem(ConfigItemKeys.TakeoffTimeKey, new ConfigItem("30"));
		configSet.addConfigItem(ConfigItemKeys.InitialWaypointDistanceKey, new ConfigItem("1000"));
		configSet.addConfigItem(ConfigItemKeys.ApproachWaypointAltitudeKey, new ConfigItem("125"));
		configSet.addConfigItem(ConfigItemKeys.RandomAdditionalAltitudeKey, new ConfigItem("1500"));
		configSet.addConfigItem(ConfigItemKeys.LandingDistanceKey, new ConfigItem("400"));

		configSet.addConfigItem(ConfigItemKeys.TakeoffWaypointDistanceKey, new ConfigItem("3000"));
		configSet.addConfigItem(ConfigItemKeys.TakeoffWaypointAltitudeKey, new ConfigItem("500"));

		configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod1Key, new ConfigItem("3000"));
		configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod2Key, new ConfigItem("4000"));
        configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod3Key, new ConfigItem("5000"));
        configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod4Key, new ConfigItem("5000"));
        configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod5Key, new ConfigItem("5000"));
        configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod6Key, new ConfigItem("5000"));
        
        return configSet;
	}
}

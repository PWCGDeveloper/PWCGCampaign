package pwcg.core.config;

public class ConfigSetAircraftNumbers
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetAircraftNumbers);

		configSet.addConfigItem(ConfigItemKeys.GroundAttackMinimumKey, new ConfigItem("2"));  		// Minimum number of aircraft";
		configSet.addConfigItem(ConfigItemKeys.GroundAttackAdditionalKey, new ConfigItem("1"));		// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.PatrolMinimumKey, new ConfigItem("2"));				// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.PatrolAdditionalKey, new ConfigItem("2"));			// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.BombingMinimumKey, new ConfigItem("2"));				// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.BombingAdditionalKey, new ConfigItem("2"));			// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.TransportMinimumKey, new ConfigItem("1"));				// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.TransportAdditionalKey, new ConfigItem("2"));			// Maximum additional aircraft
		
		configSet.addConfigItem(ConfigItemKeys.GroundAttackAltitudeKey, new ConfigItem("150"));
		configSet.addConfigItem(ConfigItemKeys.PatrolDistanceBaseKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.PatrolDistanceRandomKey, new ConfigItem("10"));	
		
		return configSet;
	}
}

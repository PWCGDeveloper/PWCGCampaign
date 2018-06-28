package pwcg.core.config;

public class ConfigSetAircraftNumbers
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetAircraftNumbers);

		configSet.addConfigItem(ConfigItemKeys.OffensivePatrolMinimumKey, new ConfigItem("2"));		// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.OffensivePatrolAdditionalKey, new ConfigItem("2"));	// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.InterceptMinimumKey, new ConfigItem("2"));			// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.InterceptAdditionalKey, new ConfigItem("1"));		// Maximum additional aircraft
		
		configSet.addConfigItem(ConfigItemKeys.BalloonBustMinimumKey, new ConfigItem("2"));			// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.BalloonBustAdditionalKey, new ConfigItem("2"));		// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.BalloonDefenseMinimumKey, new ConfigItem("2"));		
		configSet.addConfigItem(ConfigItemKeys.BalloonDefenseAdditionalKey, new ConfigItem("2"));
		
		configSet.addConfigItem(ConfigItemKeys.GroundAttackMinimumKey, new ConfigItem("2"));  		// Minimum number of aircraft";
		configSet.addConfigItem(ConfigItemKeys.GroundAttackAdditionalKey, new ConfigItem("1"));		// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.PatrolMinimumKey, new ConfigItem("2"));				// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.PatrolAdditionalKey, new ConfigItem("2"));			// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.ArtillerySpotMinimumKey, new ConfigItem("1"));		// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.ArtillerySpotAdditionalKey, new ConfigItem("2"));	// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.BombingMinimumKey, new ConfigItem("2"));				// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.BombingAdditionalKey, new ConfigItem("2"));			// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.ReconMinimumKey, new ConfigItem("1"));				// Minimum number of aircraft
		configSet.addConfigItem(ConfigItemKeys.ReconAdditionalKey, new ConfigItem("2"));			// Maximum additional aircraft
		configSet.addConfigItem(ConfigItemKeys.ScrambleMinimumKey, new ConfigItem("1"));		
		configSet.addConfigItem(ConfigItemKeys.ScrambleAdditionalKey, new ConfigItem("2"));		
		
		configSet.addConfigItem(ConfigItemKeys.GroundAttackIngressDistanceKey, new ConfigItem("12000"));
		configSet.addConfigItem(ConfigItemKeys.GroundAttackAltitudeKey, new ConfigItem("150"));
		configSet.addConfigItem(ConfigItemKeys.PatrolDistanceBaseKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.PatrolDistanceRandomKey, new ConfigItem("10"));	
		
		return configSet;
	}
}

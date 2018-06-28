package pwcg.core.config;

public class ConfigSetGroundObjects
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetGroundObjects);

		configSet.addConfigItem(ConfigItemKeys.WindsockDistanceKey, new ConfigItem("30")); 			// Distance in meters wind sock is placed from flight
		configSet.addConfigItem(ConfigItemKeys.KeepAirfieldSpreadKey, new ConfigItem("10000"));		// Distance from waypoint box to keep an airfield
		configSet.addConfigItem(ConfigItemKeys.KeepAAASpreadKey, new ConfigItem("10000"));			// Distance from waypoint box to keep AAA
		configSet.addConfigItem(ConfigItemKeys.KeepGroupSpreadKey, new ConfigItem("50000"));			// Distance from waypoint box to keep a group (city, new ConfigItem("large airfield)
		configSet.addConfigItem(ConfigItemKeys.AirfieldInclusionRadiusKey, new ConfigItem("50000")); // Distance from which to draw flights from
		configSet.addConfigItem(ConfigItemKeys.MaxGroundTargetDistanceKey, new ConfigItem("10000")); // Max flying distance for most tactical ground targets.
		configSet.addConfigItem(ConfigItemKeys.RandomShipsKey, new ConfigItem("4"));
		configSet.addConfigItem(ConfigItemKeys.RandomTrainsKey, new ConfigItem("3"));
		return configSet;
	}
}

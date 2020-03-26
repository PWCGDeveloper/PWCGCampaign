package pwcg.core.config;

public class ConfigSetBomberMissionTypes
{
	public static ConfigSet initialize()
	{
        ConfigSet configSet = new ConfigSet();
        configSet.setConfigSetName(ConfigSetKeys.ConfigSetBomberMission);

        configSet.addConfigItem(ConfigItemKeys.AxisBombingMissionKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisLowAltBombingMissionKey, new ConfigItem("10"));
        
        configSet.addConfigItem(ConfigItemKeys.AlliedBombingMissionKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedLowAltBombingMissionKey, new ConfigItem("10"));
        
        return configSet;
	}
}

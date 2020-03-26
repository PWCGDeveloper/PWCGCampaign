package pwcg.core.config;

public class ConfigSetFighterMissionTypes
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetFighterMission);

		configSet.addConfigItem(ConfigItemKeys.AxisOffensiveMissionKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.AxisInterceptMissionKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.AxisEscortMissionKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.AxisBalloonBustMissionKey, new ConfigItem("5"));
		configSet.addConfigItem(ConfigItemKeys.AxisBalloonDefenseMissionKey, new ConfigItem("5"));
		configSet.addConfigItem(ConfigItemKeys.AxisPatrolMissionKey, new ConfigItem("40"));
		configSet.addConfigItem(ConfigItemKeys.AxisLowAltPatrolMissionKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.AxisLowAltCapMissionKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisBombingMissionKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisLowAltBombingMissionKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisContactPatrolKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisReconKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTransportKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisCargoDropKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisParachuteDropKey, new ConfigItem("10"));
	    
		configSet.addConfigItem(ConfigItemKeys.AlliedOffensiveMissionKey, new ConfigItem("20"));
		configSet.addConfigItem(ConfigItemKeys.AlliedInterceptMissionKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.AlliedEscortMissionKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.AlliedBalloonBustMissionKey, new ConfigItem("5"));
		configSet.addConfigItem(ConfigItemKeys.AlliedBalloonDefenseMissionKey, new ConfigItem("5"));
		configSet.addConfigItem(ConfigItemKeys.AlliedPatrolMissionKey, new ConfigItem("40"));
		configSet.addConfigItem(ConfigItemKeys.AlliedLowAltPatrolMissionKey, new ConfigItem("10"));
		configSet.addConfigItem(ConfigItemKeys.AlliedLowAltCapMissionKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedBombingMissionKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedLowAltBombingMissionKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedContactPatrolKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedReconKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTransportKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedCargoDropKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedParachuteDropKey, new ConfigItem("10"));
		
		return configSet;
	}
}

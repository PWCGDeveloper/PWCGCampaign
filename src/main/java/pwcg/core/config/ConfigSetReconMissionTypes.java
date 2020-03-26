package pwcg.core.config;

public class ConfigSetReconMissionTypes
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetReconMission);

        configSet.addConfigItem(ConfigItemKeys.AxisContactPatrolKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisReconKey, new ConfigItem("10"));
	    
        configSet.addConfigItem(ConfigItemKeys.AlliedContactPatrolKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedReconKey, new ConfigItem("10"));
		
		return configSet;
	}
}

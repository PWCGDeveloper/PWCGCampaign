package pwcg.core.config;

public class ConfigSetTransportMissionTypes
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
        configSet.setConfigSetName(ConfigSetKeys.ConfigSetTransportMission);

        configSet.addConfigItem(ConfigItemKeys.AxisTransportKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisCargoDropKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisParachuteDropKey, new ConfigItem("10"));
	    
        configSet.addConfigItem(ConfigItemKeys.AlliedTransportKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedCargoDropKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedParachuteDropKey, new ConfigItem("10"));
		
		return configSet;
	}
}

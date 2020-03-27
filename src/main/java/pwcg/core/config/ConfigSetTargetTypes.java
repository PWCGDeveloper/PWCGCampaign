package pwcg.core.config;

public class ConfigSetTargetTypes
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetReconMission);

        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeAssaultKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeDefenseKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeTransportKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeTrainKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeAirfieldKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeShippingKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeDrifterKey, new ConfigItem("10"));
	    
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeAssaultKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeDefenseKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeTransportKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeTrainKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeAirfieldKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeShippingKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeDrifterKey, new ConfigItem("10"));
		
		return configSet;
	}
}

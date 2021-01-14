package pwcg.core.config;

public class ConfigSetTargetTypes
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetReconMission);

        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeArmorKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeInfantryKey, new ConfigItem("20"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeTransportKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeTrainKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeAirfieldKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeShippingKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeDrifterKey, new ConfigItem("10"));

        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeAirfieldKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeBridgeKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeRailKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeFactoryKey, new ConfigItem("4"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypePortKey, new ConfigItem("4"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeDepotKey, new ConfigItem("2"));
        configSet.addConfigItem(ConfigItemKeys.AlliedTargetTypeFuelDepotKey, new ConfigItem("2"));

        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeArmorKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeInfantryKey, new ConfigItem("20"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeTransportKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeTrainKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeAirfieldKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeShippingKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeDrifterKey, new ConfigItem("10"));

        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeAirfieldKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeBridgeKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeRailKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeFactoryKey, new ConfigItem("4"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypePortKey, new ConfigItem("4"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeDepotKey, new ConfigItem("2"));
        configSet.addConfigItem(ConfigItemKeys.AxisTargetTypeFuelDepotKey, new ConfigItem("2"));
		
		return configSet;
	}
}

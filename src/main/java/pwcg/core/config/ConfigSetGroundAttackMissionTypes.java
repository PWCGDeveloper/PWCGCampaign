package pwcg.core.config;

public class ConfigSetGroundAttackMissionTypes
{
    public static ConfigSet initialize()
    {
        ConfigSet configSet = new ConfigSet();
        configSet.setConfigSetName(ConfigSetKeys.ConfigSetGroundAttackMission);

        configSet.addConfigItem(ConfigItemKeys.AlliedGroundAttackKey, new ConfigItem("65"));
        configSet.addConfigItem(ConfigItemKeys.AlliedGroundFreeHuntKey, new ConfigItem("35"));
        
        configSet.addConfigItem(ConfigItemKeys.AxisGroundAttackKey, new ConfigItem("65"));
        configSet.addConfigItem(ConfigItemKeys.AxisGroundFreeHuntKey, new ConfigItem("35"));
        
        return configSet;
    }
}

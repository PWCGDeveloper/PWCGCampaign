package pwcg.core.config;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class ConfigSimple
{
    public static String CONFIG_LEVEL_LOW = "Low";
    public static String CONFIG_LEVEL_MED = "Med";
    public static String CONFIG_LEVEL_HIGH = "High";

    private Campaign campaign = null;
    
    public ConfigSimple (Campaign campaign)
    {
    	this.campaign = campaign;
    }

    public void setAirLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigAirKey, ConfigSimple.CONFIG_LEVEL_LOW);
        setParamForSimpleConfigChange(ConfigItemKeys.AlliedPlanesToSpawnMaxKey, "6");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisPlanesToSpawnMaxKey, "8");

        setParamForSimpleConfigChange(ConfigItemKeys.AlliedFlightsToKeepKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisFlightsToKeepKey, "2");

        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackAdditionalKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportMinimumKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportAdditionalKey, "2");
    }

    public void setGroundLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_LOW);
        setParamForSimpleConfigChange(ConfigItemKeys.UseAirfieldMGsKey, "0");
        setParamForSimpleConfigChange(ConfigItemKeys.UseAmbientGroundUnitsKey, "0");
        setParamForSimpleConfigChange(ConfigItemKeys.MGSpacingKey, "5000");
    }

    public void setAirMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigAirKey, ConfigSimple.CONFIG_LEVEL_MED);
        setParamForSimpleConfigChange(ConfigItemKeys.AlliedPlanesToSpawnMaxKey, "8");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisPlanesToSpawnMaxKey, "12");

        setParamForSimpleConfigChange(ConfigItemKeys.AlliedFlightsToKeepKey, "5");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisFlightsToKeepKey, "3");

        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingAdditionalKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportAdditionalKey, "2");
    }

    public void setGroundMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_MED);
        setParamForSimpleConfigChange(ConfigItemKeys.UseAirfieldMGsKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.UseAmbientGroundUnitsKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.MGSpacingKey, "3000");
    }

    public void setAirHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigAirKey, ConfigSimple.CONFIG_LEVEL_HIGH);
        setParamForSimpleConfigChange(ConfigItemKeys.AlliedPlanesToSpawnMaxKey, "16");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisPlanesToSpawnMaxKey, "20");
        
        setParamForSimpleConfigChange(ConfigItemKeys.AlliedFlightsToKeepKey, "8");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisFlightsToKeepKey, "6");

        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackAdditionalKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingAdditionalKey, "6");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportAdditionalKey, "4");
    }

    public void setGroundHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_HIGH);
        setParamForSimpleConfigChange(ConfigItemKeys.UseAirfieldMGsKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.UseAmbientGroundUnitsKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.MGSpacingKey, "2000");
    }

    public void setParamForSimpleConfigChange (String key, String value) throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        configManager.setParam(key, value);
    }
}

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

        setParamForSimpleConfigChange(ConfigItemKeys.AlliedFlightsToKeepKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisFlightsToKeepKey, "3");

        setParamForSimpleConfigChange(ConfigItemKeys.OffensivePatrolMinimumKey, "2"); 
        setParamForSimpleConfigChange(ConfigItemKeys.OffensivePatrolAdditionalKey, "2"); 
        setParamForSimpleConfigChange(ConfigItemKeys.InterceptMinimumKey, "2"); 
        setParamForSimpleConfigChange(ConfigItemKeys.InterceptAdditionalKey, "1"); 
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackAdditionalKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.ReconMinimumKey, "1"); 
        setParamForSimpleConfigChange(ConfigItemKeys.ReconAdditionalKey, "2"); 
        setParamForSimpleConfigChange(ConfigItemKeys.ScrambleMinimumKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.ScrambleAdditionalKey, "2");
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

        setParamForSimpleConfigChange(ConfigItemKeys.AlliedFlightsToKeepKey, "8");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisFlightsToKeepKey, "6");

        setParamForSimpleConfigChange(ConfigItemKeys.OffensivePatrolMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.OffensivePatrolAdditionalKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.InterceptMinimumKey, "2"); 
        setParamForSimpleConfigChange(ConfigItemKeys.InterceptAdditionalKey, "2"); 
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackMinimumKey, "2"); 
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolAdditionalKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingMinimumKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingAdditionalKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.ReconMinimumKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.ReconAdditionalKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.ScrambleMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.ScrambleAdditionalKey, "3");
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
        
        setParamForSimpleConfigChange(ConfigItemKeys.AlliedFlightsToKeepKey, "12");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisFlightsToKeepKey, "9");

        setParamForSimpleConfigChange(ConfigItemKeys.OffensivePatrolMinimumKey, "3"); 
        setParamForSimpleConfigChange(ConfigItemKeys.OffensivePatrolAdditionalKey, "4"); 
        setParamForSimpleConfigChange(ConfigItemKeys.InterceptMinimumKey, "3"); 
        setParamForSimpleConfigChange(ConfigItemKeys.InterceptAdditionalKey, "3"); 
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackMinimumKey, "2"); 
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackAdditionalKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolMinimumKey, "3"); 
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolAdditionalKey, "4"); 
        setParamForSimpleConfigChange(ConfigItemKeys.BombingMinimumKey, "4"); 
        setParamForSimpleConfigChange(ConfigItemKeys.BombingAdditionalKey, "4"); 
        setParamForSimpleConfigChange(ConfigItemKeys.ReconMinimumKey, "1"); 
        setParamForSimpleConfigChange(ConfigItemKeys.ReconAdditionalKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.ScrambleMinimumKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.ScrambleAdditionalKey, "3");
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

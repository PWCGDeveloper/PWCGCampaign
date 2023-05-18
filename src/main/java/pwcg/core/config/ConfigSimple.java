package pwcg.core.config;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class ConfigSimple
{
    public static String CONFIG_LEVEL_ULTRA_LOW = "Ultra Low";
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

        setParamForSimpleConfigChange(ConfigItemKeys.AlliedFlightsToKeepKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisFlightsToKeepKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.MaxVirtualEscortedFlightKey, "2");

        setParamForSimpleConfigChange(ConfigItemKeys.PatrolMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.StrategicBombingMinimumKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.StrategicBombingAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportMinimumKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportAdditionalKey, "2");
    }

    public void setAirMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigAirKey, ConfigSimple.CONFIG_LEVEL_MED);

        setParamForSimpleConfigChange(ConfigItemKeys.AlliedFlightsToKeepKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisFlightsToKeepKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.MaxVirtualEscortedFlightKey, "2");

        setParamForSimpleConfigChange(ConfigItemKeys.PatrolMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingMinimumKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingAdditionalKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.StrategicBombingMinimumKey, "6");
        setParamForSimpleConfigChange(ConfigItemKeys.StrategicBombingAdditionalKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackAdditionalKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportMinimumKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportAdditionalKey, "4");
    }

    public void setAirHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigAirKey, ConfigSimple.CONFIG_LEVEL_HIGH);
        
        setParamForSimpleConfigChange(ConfigItemKeys.AlliedFlightsToKeepKey, "6");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisFlightsToKeepKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.MaxVirtualEscortedFlightKey, "2");

        setParamForSimpleConfigChange(ConfigItemKeys.PatrolMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.PatrolAdditionalKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingMinimumKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.BombingAdditionalKey, "6");
        setParamForSimpleConfigChange(ConfigItemKeys.StrategicBombingMinimumKey, "8");
        setParamForSimpleConfigChange(ConfigItemKeys.StrategicBombingAdditionalKey, "4");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackMinimumKey, "2");
        setParamForSimpleConfigChange(ConfigItemKeys.GroundAttackAdditionalKey, "6");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportMinimumKey, "1");
        setParamForSimpleConfigChange(ConfigItemKeys.TransportAdditionalKey, "5");
    }

    public void setGroundUltraLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_ULTRA_LOW);
    }

    public void setGroundLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_LOW);
    }

    public void setGroundMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_MED);
    }

    public void setGroundHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_HIGH);
    }
    
    public void setAAUltraLow() throws PWCGException
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigAAKey, ConfigSimple.CONFIG_LEVEL_ULTRA_LOW);        
    }
    
    public void setAALow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigAAKey, ConfigSimple.CONFIG_LEVEL_LOW);
    }
    
    public void setAAMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigAAKey, ConfigSimple.CONFIG_LEVEL_MED);
    }
    
    public void setAAHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigAAKey, ConfigSimple.CONFIG_LEVEL_HIGH);
    }

    public void setCpuAllowanceHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigCpuAllowanceKey, ConfigSimple.CONFIG_LEVEL_HIGH);
    }
    public void setCpuAllowanceLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigCpuAllowanceKey, ConfigSimple.CONFIG_LEVEL_LOW);
    }
    
    public void setCpuAllowanceMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigCpuAllowanceKey, ConfigSimple.CONFIG_LEVEL_MED);
    }

    public void setStructureHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_HIGH);
    }
    public void setStructureLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_LOW);
    }
    
    public void setStructureMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_MED);
    }

    public void setParamForSimpleConfigChange (String key, String value) throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        configManager.setParam(key, value);
    }
}

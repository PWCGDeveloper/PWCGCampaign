package pwcg.gui.campaign.config;

import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.home.CampaignHomeContext;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.CommonUIActions;

public class CampaignConfigurationSimpleGUIController
{
    static public final String ACTION_SET_AIR_DENSITY = "Air Density";
    static public final String ACTION_SET_GROUND_DENSITY = "Ground Density";
    static public final String ACTION_SET_AA_DENSITY = "AA Density";
    static public final String ACTION_SET_CPU_ALOWANCE_DENSITY = "CPU Allowance";
    static public final String ACTION_SET_STRUCTURE_DENSITY = "Structure";
    
    static public final String CAMPAIGN_TYPE = "Campaign Type";
    
    public CampaignConfigurationSimpleGUIController()
    {
    }
    
	public void setSimpleConfig(String action)
	{
		try
		{
			if (action.contains(ACTION_SET_AIR_DENSITY))
			{
				setAirDensity(action);
			}
            else if (action.contains(ACTION_SET_GROUND_DENSITY))
            {
                setGroundDensity(action);
            }
            else if (action.contains(ACTION_SET_AA_DENSITY))
            {
                setAADensity(action);
            }           
            else if (action.contains(ACTION_SET_CPU_ALOWANCE_DENSITY))
            {
                setCpuAllowanceDensity(action);
            }           
            else if (action.contains(ACTION_SET_STRUCTURE_DENSITY))
            {
                setStructureDensity(action);
            }           
            else if (action.equalsIgnoreCase(CommonUIActions.ACTION_ACCEPT))
			{
				acceptSimpleConfigChanges();
			}
			else if (action.equalsIgnoreCase(CommonUIActions.ACTION_RESET))
			{
				resetConfigurationToDefault();
			}
			if (action.equalsIgnoreCase(CommonUIActions.ACTION_CANCEL))
			{
				cancelSimpleConfigChanges();
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void setAirDensity(String action) throws PWCGException
    {
        ConfigSimple configSetSimpleConfig = new ConfigSimple(CampaignHomeContext.getCampaign());
        if (action.contains(ConfigSimple.CONFIG_LEVEL_LOW))
        {					
        	configSetSimpleConfig.setAirLow();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_MED))
        {
            configSetSimpleConfig.setAirMed();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            configSetSimpleConfig.setAirHigh();
        }
    }

    private void setGroundDensity(String action) throws PWCGException
    {
        ConfigSimple configSetSimpleConfig = new ConfigSimple(CampaignHomeContext.getCampaign());
        if (action.contains(ConfigSimple.CONFIG_LEVEL_ULTRA_LOW))
        {
            configSetSimpleConfig.setGroundUltraLow();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            configSetSimpleConfig.setGroundLow();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_MED))
        {
            configSetSimpleConfig.setGroundMed();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            configSetSimpleConfig.setGroundHigh();
        }
    }

    private void setAADensity(String action) throws PWCGException
    {
        ConfigSimple configSetSimpleConfig = new ConfigSimple(CampaignHomeContext.getCampaign());
        if (action.contains(ConfigSimple.CONFIG_LEVEL_ULTRA_LOW))
        {
            configSetSimpleConfig.setAAUltraLow();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            configSetSimpleConfig.setAALow();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_MED))
        {
            configSetSimpleConfig.setAAMed();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            configSetSimpleConfig.setAAHigh();
        }
    }

    private void setCpuAllowanceDensity(String action) throws PWCGException
    {
        ConfigSimple configSetSimpleConfig = new ConfigSimple(CampaignHomeContext.getCampaign());
        if (action.contains(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            configSetSimpleConfig.setCpuAllowanceLow();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_MED))
        {
            configSetSimpleConfig.setCpuAllowanceMed();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            configSetSimpleConfig.setCpuAllowanceHigh();
        }
    }

    private void setStructureDensity(String action) throws PWCGException
    {
        ConfigSimple configSetSimpleConfig = new ConfigSimple(CampaignHomeContext.getCampaign());
        if (action.contains(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            configSetSimpleConfig.setStructureLow();;
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_MED))
        {
            configSetSimpleConfig.setStructureMed();;
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            configSetSimpleConfig.setStructureHigh();;
        }
    }

    private void acceptSimpleConfigChanges() throws PWCGException
    {
        CampaignHomeContext.getCampaign().getCampaignConfigManager().write();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void resetConfigurationToDefault() throws PWCGException
    {
        CampaignHomeContext.getCampaign().getCampaignConfigManager().reset();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void cancelSimpleConfigChanges() throws PWCGException
    {
        CampaignHomeContext.getCampaign().getCampaignConfigManager().readConfig();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

}


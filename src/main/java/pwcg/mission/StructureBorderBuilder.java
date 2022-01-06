package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;

public class StructureBorderBuilder 
{
    private Campaign campaign;
    private CoordinateBox missionBorders;
	
	public StructureBorderBuilder(Campaign campaign, CoordinateBox missionBorders)
	{
        this.campaign = campaign;
        this.missionBorders = missionBorders;
	}

    public CoordinateBox buildBorderForMission() throws PWCGException
    {
        return applyStructureExpansion(missionBorders);
    }

    private CoordinateBox applyStructureExpansion(CoordinateBox structureBorders) throws PWCGException
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentCpuAllowanceSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigStructuresKey);
        if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            return applySpreadToBox(structureBorders, 3);
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            return applySpreadToBox(structureBorders, 2);
        }
        else
        {
            return applySpreadToBox(structureBorders, 1);
        }
    }

    private CoordinateBox applySpreadToBox(CoordinateBox box, int spreadMultiplier) throws PWCGException
    {
        ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey) * spreadMultiplier;

        CoordinateBox structureBorders = CoordinateBox.copy(box);
        structureBorders.expandBox(keepGroupSpread);
        return structureBorders;
    }
}

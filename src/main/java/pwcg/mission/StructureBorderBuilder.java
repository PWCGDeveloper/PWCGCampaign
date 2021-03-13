package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;

public class StructureBorderBuilder 
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    private CoordinateBox missionBorders;
	
	public StructureBorderBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers, CoordinateBox missionBorders)
	{
        this.participatingPlayers = participatingPlayers;
        this.campaign = campaign;
        this.missionBorders = missionBorders;
	}

    public CoordinateBox getBordersForStructures() throws PWCGException
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentCpuAllowanceSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigStructuresKey);
        if   (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            return getLowStructureKeepBox();
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            return getMediumStructureKeepBox();
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            return getHighStructureKeepBox();
        }
    
        return getLowStructureKeepBox();
    }

    private CoordinateBox getLowStructureKeepBox() throws PWCGException
    {
        int spreadMultiplier = 1;
        CoordinateBox structureBorders = applySpreadToBox(missionBorders, spreadMultiplier);
        return structureBorders;
    }

    private CoordinateBox getMediumStructureKeepBox() throws PWCGException
    {
        CoordinateBox structureBorders = expandMissionBordersForPlayerFields();
        
        int spreadMultiplier = 2;
        structureBorders = applySpreadToBox(structureBorders, spreadMultiplier);

        return structureBorders;
    }

    private CoordinateBox getHighStructureKeepBox() throws PWCGException
    {
        CoordinateBox structureBorders = expandMissionBordersForPlayerFields();
        
        int spreadMultiplier = 3;
        structureBorders = applySpreadToBox(structureBorders, spreadMultiplier);

        return structureBorders;
    }

    private CoordinateBox expandMissionBordersForPlayerFields() throws PWCGException
    {
        CoordinateBox structureBorders = CoordinateBox.copy(missionBorders);
        
        List<Coordinate> airfieldCoordinates = new ArrayList<>();
        for (SquadronMember player : participatingPlayers.getAllParticipatingPlayers())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            Airfield airfield = squadron.determineCurrentAirfieldAnyMap(campaign.getDate());
            airfieldCoordinates.add(airfield.getPosition());
        }
        
        structureBorders.expandBoxCornersFromCoordinates(airfieldCoordinates);
        return structureBorders;
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

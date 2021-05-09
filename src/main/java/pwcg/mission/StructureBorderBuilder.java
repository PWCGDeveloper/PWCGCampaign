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
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;

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

    public CoordinateBox getBordersForStructuresConsideringFlights(List<IFlight> playerFlights) throws PWCGException
    {
        CoordinateBox structureBorders = expandMissionBordersForPlayerFields();
        structureBorders = expandMissionBordersForPlayerFlightRoutes(structureBorders, playerFlights);
        return applyStructureExpansion(structureBorders);
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

    private CoordinateBox expandMissionBordersForPlayerFlightRoutes(CoordinateBox structureBorders, List<IFlight> playerFlights) throws PWCGException
    {        
        List<Coordinate> flightPointCoordinates = new ArrayList<>();
        for (IFlight playerFlight : playerFlights)
        {
            for (MissionPoint missionPoint : playerFlight.getWaypointPackage().getFlightMissionPoints())
            {
                flightPointCoordinates.add(missionPoint.getPosition());
            }
        }
        
        structureBorders.expandBoxCornersFromCoordinates(flightPointCoordinates);
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

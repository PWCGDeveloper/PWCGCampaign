package pwcg.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.FlightTypes;

public class MissionGenerator
{
    private Campaign campaign = null;

    public MissionGenerator(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Mission makeMission(MissionHumanParticipants participatingPlayers) throws PWCGException 
    {
        MissionProfile missionProfile = generateProfile(participatingPlayers);
        List<FlightTypes> playerFlightTypes = finalizePlayerFlightType(participatingPlayers, missionProfile);
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, missionProfile);
        return mission;
    }

    public Mission makeLoneWolfMission(MissionHumanParticipants participatingPlayers) throws PWCGException 
    {
        List<FlightTypes> playerFlightTypes = Arrays.asList(FlightTypes.LONE_WOLF);
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);
        return mission;
    }

    public Mission makeMissionFromFlightType(MissionHumanParticipants participatingPlayers, FlightTypes playerFlightType, MissionProfile missionProfile) throws PWCGException 
    {
        List<FlightTypes> playerFlightTypes = Arrays.asList(playerFlightType);
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, missionProfile);
        return mission;
    }

    private List<FlightTypes> finalizePlayerFlightType(MissionHumanParticipants participatingPlayers, MissionProfile missionProfile) throws PWCGException
    {
        List<FlightTypes> playerFlightTypes = new ArrayList<>();
        for (Integer squadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            PlayerFlightTypeBuilder playerFlightTypeBuilder = new PlayerFlightTypeBuilder(campaign);
            FlightTypes requestedFlightType = playerFlightTypeBuilder.determinePlayerFlightType(playerSquadron, participatingPlayers, missionProfile.isNightMission());
            playerFlightTypes.add(requestedFlightType);
        }
        return playerFlightTypes;
    }
    
    private MissionProfile generateProfile(MissionHumanParticipants participatingPlayers) throws PWCGException 
    {
        MissionProfileGenerator missionProfileGenerator = new MissionProfileGenerator(campaign, participatingPlayers);
        MissionProfile missionProfile = missionProfileGenerator.generateMissionProfile();
        return missionProfile;
    }

    private Mission buildMission(MissionHumanParticipants participatingPlayers, List<FlightTypes> playerFlightTypes, MissionProfile missionProfile) throws PWCGException 
    {
        campaign.setCurrentMission(null);        
        CoordinateBox missionBorders = buildMissionBorders(playerFlightTypes, participatingPlayers);
        Mission mission = new Mission(campaign, missionProfile, participatingPlayers, missionBorders);
        campaign.setCurrentMission(mission);
        mission.generate(playerFlightTypes);
        
        return mission;
    }

    private CoordinateBox buildMissionBorders(List<FlightTypes> playerFlightTypes, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox(playerFlightTypes);
        return missionBorders;
    }
}

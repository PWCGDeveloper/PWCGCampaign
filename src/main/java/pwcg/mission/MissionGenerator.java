package pwcg.mission;

import pwcg.campaign.Campaign;
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
        Mission mission = makeMissionFromFlightType(participatingPlayers, FlightTypes.ANY);
        return mission;
    }

    public Mission makeLoneWolfMission(MissionHumanParticipants participatingPlayers) throws PWCGException 
    {
        Mission mission = makeMissionFromFlightType(participatingPlayers, FlightTypes.LONE_WOLF);
        return mission;
    }

    public Mission makeMissionFromFlightType(MissionHumanParticipants participatingPlayers, FlightTypes playerFlightType) throws PWCGException 
    {
        Mission mission = buildMission(participatingPlayers, playerFlightType);
        return mission;
    }

    private Mission buildMission(MissionHumanParticipants participatingPlayers, FlightTypes overrideFlightType) throws PWCGException 
    {
        campaign.setCurrentMission(null);        
        CoordinateBox missionBorders = buildMissionBorders(overrideFlightType, participatingPlayers);
        Mission mission = new Mission(campaign, participatingPlayers, missionBorders);
        campaign.setCurrentMission(mission);
        mission.generate(overrideFlightType);
        
        return mission;
    }

    private CoordinateBox buildMissionBorders(FlightTypes overrideFlightType, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox(overrideFlightType);
        return missionBorders;
    }
}

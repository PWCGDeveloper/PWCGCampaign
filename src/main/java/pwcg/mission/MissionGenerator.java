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
        MissionProfile missionProfile = generateProfile(participatingPlayers);
        FlightTypes flightType = finalizePlayerFlightType(participatingPlayers, missionProfile);
        Mission mission = makeMissionFromFlightType(participatingPlayers, flightType, missionProfile);
        return mission;
    }

    public Mission makeLoneWolfMission(MissionHumanParticipants participatingPlayers) throws PWCGException 
    {
        Mission mission = makeMissionFromFlightType(participatingPlayers, FlightTypes.LONE_WOLF, MissionProfile.DAY_TACTICAL_MISSION);
        return mission;
    }

    public Mission makeMissionFromFlightType(MissionHumanParticipants participatingPlayers, FlightTypes overrideFlightType, MissionProfile missionProfile) throws PWCGException 
    {
        Mission mission = buildMission(participatingPlayers, overrideFlightType, missionProfile);
        return mission;
    }

    private FlightTypes finalizePlayerFlightType(MissionHumanParticipants participatingPlayers, MissionProfile missionProfile) throws PWCGException
    {
        PlayerFlightTypeBuilder playerFlightTypeBuilder = new PlayerFlightTypeBuilder(campaign);
        return playerFlightTypeBuilder.preDeterminePlayerFlightType(FlightTypes.ANY, participatingPlayers, missionProfile.isNightMission());
    }
    
    private MissionProfile generateProfile(MissionHumanParticipants participatingPlayers) throws PWCGException 
    {
        MissionProfileGenerator missionProfileGenerator = new MissionProfileGenerator(campaign, participatingPlayers);
        MissionProfile missionProfile = missionProfileGenerator.generateMissionProfile();
        return missionProfile;
    }

    private Mission buildMission(MissionHumanParticipants participatingPlayers, FlightTypes overrideFlightType, MissionProfile missionProfile) throws PWCGException 
    {
        campaign.setCurrentMission(null);        
        CoordinateBox missionBorders = buildMissionBorders(overrideFlightType, participatingPlayers);
        Mission mission = new Mission(campaign, missionProfile, participatingPlayers, missionBorders);
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

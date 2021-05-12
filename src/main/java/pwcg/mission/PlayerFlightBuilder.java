package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.plane.PlaneMcu;

public class PlayerFlightBuilder
{
    private Campaign campaign;
    private Mission mission;

    private List<IFlight> playerFlightSet;
 
    public PlayerFlightBuilder(Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public List<IFlight> createPlayerFlight(FlightTypes requestedFlightType, Squadron squadron, MissionHumanParticipants participatingPlayers, boolean isNightMission) throws PWCGException 
    {
        buildFlight(requestedFlightType, squadron);
        return playerFlightSet;
    }

    private void buildFlight(FlightTypes requestedFlightType, Squadron squadron) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        playerFlightSet = flightFactory.buildFlight(mission, squadron, requestedFlightType, NecessaryFlightType.PLAYER_FLIGHT);        
        validatePlayerFlight();
    }

    private void validatePlayerFlight() throws PWCGException
    {
        boolean playerIsInFlight = false;
        IFlight playerFlight = MissionFlightBuilder.getPlayerFlight(playerFlightSet);
        for (PlaneMcu plane : playerFlight.getFlightPlanes().getPlanes())
        {
            if (plane.getPilot().isPlayer())
            {
                playerIsInFlight = true;
            }
        }
        
        if (!playerIsInFlight)
        {
            throw new PWCGException("No plane assigned to player");
        }
    }
}

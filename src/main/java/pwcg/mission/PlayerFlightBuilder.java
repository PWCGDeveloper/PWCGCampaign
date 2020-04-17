package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class PlayerFlightBuilder
{
    private Campaign campaign;
    private Mission mission;

    private IFlight playerFlight;
 
    public PlayerFlightBuilder(Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public IFlight createPlayerFlight(FlightTypes requestedFlightType, Squadron squadron, MissionHumanParticipants participatingPlayers, boolean isNightMission) throws PWCGException 
    {
        buildFlight(requestedFlightType, squadron);
        return playerFlight;
    }

    private void buildFlight(FlightTypes requestedFlightType, Squadron squadron) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        boolean isPlayerFlight = true;
        playerFlight = flightFactory.buildFlight(mission, squadron, requestedFlightType, isPlayerFlight);        
        validatePlayerFlight();
    }

    private void validatePlayerFlight() throws PWCGException
    {
        boolean playerIsInFlight = false;
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

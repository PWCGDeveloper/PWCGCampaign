package pwcg.mission.flight.escort;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;

public class EscortFlightBuilder
{
    public void addEscort(Mission mission, Flight escortedFlight) throws PWCGException 
    {
        Campaign campaign = mission.getCampaign();
        Flight escortFlight = null;
        if (escortedFlight.isPlayerFlight())
        {
            escortFlight = createEscortForPlayerFlight(escortedFlight);
        }
        else if (!campaign.getCampaignData().isCoop())
        {
            escortFlight = createEscortForAiFlight(mission, escortedFlight);
        }
        
        if (escortFlight != null)
        {
            escortedFlight.addLinkedUnit(escortFlight);
        }
    }

    private Flight createEscortForPlayerFlight(Flight escortedFlight) throws PWCGException 
    {
        PlayerEscortBuilder playerEscortBuilder = new PlayerEscortBuilder();
        Flight escortForPlayerFlight = playerEscortBuilder.createEscortForPlayerFlight(escortedFlight);
        return escortForPlayerFlight;
    }


    private Flight createEscortForAiFlight(Mission mission, Flight escortedFlight) throws PWCGException 
    {
        VirtualEscortFlightBuilder virtualEscortFlightBuilder = new VirtualEscortFlightBuilder();
        Flight escortForAiFlight = virtualEscortFlightBuilder.createVirtualEscortFlight(escortedFlight);
        return escortForAiFlight;
    }
}

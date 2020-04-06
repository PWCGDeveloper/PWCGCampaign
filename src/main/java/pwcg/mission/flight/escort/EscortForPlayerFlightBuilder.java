package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class EscortForPlayerFlightBuilder
{
    public void addEscort(Mission mission, IFlight escortedFlight) throws PWCGException 
    {
        if (mission.isNightMission())
        {
            return;
        }
        
        if (!escortedFlight.getFlightInformation().isPlayerFlight())
        {
            return;
        }
        
        IFlight escortFlight = createEscortForPlayerFlight(escortedFlight);
        if (escortFlight != null)
        {
            escortedFlight.getLinkedFlights().addLinkedFlight(escortFlight);
        }
    }

    private IFlight createEscortForPlayerFlight(IFlight escortedFlight) throws PWCGException 
    {
        EscortForPlayerBuilder playerEscortBuilder = new EscortForPlayerBuilder();
        IFlight escortForPlayerFlight = playerEscortBuilder.createEscortForPlayerFlight(escortedFlight);
        return escortForPlayerFlight;
    }
}

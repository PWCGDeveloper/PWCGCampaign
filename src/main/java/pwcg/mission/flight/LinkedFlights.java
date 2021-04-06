package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.mission.flight.escort.EscortForPlayerFlight;
import pwcg.mission.flight.escort.EscortedByPlayerFlight;
import pwcg.mission.flight.waypoint.IWaypointPackage;

public class LinkedFlights
{
    private List<IFlight> linkedFlights = new ArrayList<>();

    public IWaypointPackage getLinkedWaypoints()
    {
        for (IFlight linkedFlight : linkedFlights)
        {
            if (linkedFlight.getFlightInformation().isEscortedByPlayerFlight())
            {
                return linkedFlight.getWaypointPackage();
            }
        }

        return null;
    }
    
    public void addLinkedFlight(IFlight linkedFlight)
    {
        linkedFlights.add(linkedFlight);
    }
    
    public List<IFlight> getLinkedFlights()
    {
        return linkedFlights;
    }

    public EscortForPlayerFlight getEscortForPlayer()
    {
        EscortForPlayerFlight escortForPlayerFlight = null;
        for (IFlight linkedFlight : linkedFlights)
        {
            if (linkedFlight instanceof EscortForPlayerFlight)
            {
                escortForPlayerFlight = (EscortForPlayerFlight)linkedFlight;
            }
        }
        return escortForPlayerFlight;
    }

    public EscortedByPlayerFlight getEscortedByPlayer()
    {
        EscortedByPlayerFlight escortedByPlayerFlight = null;
        for (IFlight linkedFlight : linkedFlights)
        {
            if (linkedFlight instanceof EscortedByPlayerFlight)
            {
                escortedByPlayerFlight = (EscortedByPlayerFlight)linkedFlight;
            }
        }
        return escortedByPlayerFlight;
    }

}

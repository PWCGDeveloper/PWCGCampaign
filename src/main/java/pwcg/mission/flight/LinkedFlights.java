package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.mission.flight.escort.EscortForPlayerFlight;
import pwcg.mission.flight.waypoint.IWaypointPackage;

public class LinkedFlights implements ILinkedFlights
{
    private List<IFlight> linkedFlights = new ArrayList<>();

    @Override
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
    
    @Override
    public void addLinkedFlight(IFlight linkedFlight)
    {
        linkedFlights.add(linkedFlight);
    }
    
    @Override
    public List<IFlight> getLinkedFlights()
    {
        return linkedFlights;
    }

    @Override
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

}

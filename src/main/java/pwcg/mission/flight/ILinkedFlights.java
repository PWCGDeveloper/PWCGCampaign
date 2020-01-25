package pwcg.mission.flight;

import java.util.List;

import pwcg.mission.flight.escort.EscortForPlayerFlight;
import pwcg.mission.flight.waypoint.IWaypointPackage;

public interface ILinkedFlights
{

    IWaypointPackage getLinkedWaypoints();

    void addLinkedFlight(IFlight linkedFlight);

    List<IFlight> getLinkedFlights();

    EscortForPlayerFlight getEscortForPlayer();

}
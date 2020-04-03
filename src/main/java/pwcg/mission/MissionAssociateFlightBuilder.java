package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.EscortForPlayerFlightBuilder;
import pwcg.mission.flight.escort.NeedsEscortDecider;

public class MissionAssociateFlightBuilder
{
    public void buildAssociatedFlights(Mission mission) throws PWCGException
    {
        List<IFlight> flightsForMission = mission.getMissionFlightBuilder().getAllAerialFlights();
        for (IFlight flight : flightsForMission)
        {
            if (flight.isPlayerFlight())
            {
                NeedsEscortDecider needsEscortDecider = new NeedsEscortDecider();
                if (needsEscortDecider.needsEscort(mission, flight))
                {
                    EscortForPlayerFlightBuilder escortFlightBuilder = new EscortForPlayerFlightBuilder();
                    escortFlightBuilder.addEscort(mission, flight);
                }
            }
        }
    }
}

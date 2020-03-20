package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.EscortForPlayerFlightBuilder;
import pwcg.mission.flight.escort.NeedsEscortDecider;
import pwcg.mission.flight.intercept.InterceptOpposingFlightBuilder;

public class MissionAssociateFlightBuilder
{
    public void buildAssociatedFlights(Mission mission) throws PWCGException
    {
        List<IFlight> flightsForMission = mission.getMissionFlightBuilder().getAllAerialFlights();
        for (IFlight flight : flightsForMission)
        {
            if (flight.isPlayerFlight())
            {
                if (flight.getFlightType() == FlightTypes.INTERCEPT || flight.getFlightType() == FlightTypes.HOME_DEFENSE)
                {
                    makeLinkedInterceptFlights(flight);
                }
                else if (flight.getFlightType() == FlightTypes.BALLOON_BUST)
                {
                    // TODO Flying Circus
                }
                else if (flight.getFlightType() == FlightTypes.BALLOON_DEFENSE)
                {
                    // TODO Flying Circus
                }
                
                NeedsEscortDecider needsEscortDecider = new NeedsEscortDecider();
                if (needsEscortDecider.needsEscort(mission, flight))
                {
                    EscortForPlayerFlightBuilder escortFlightBuilder = new EscortForPlayerFlightBuilder();
                    escortFlightBuilder.addEscort(mission, flight);
                }
            }
        }
    }

    private void makeLinkedInterceptFlights(IFlight flight) throws PWCGException
    {
        InterceptOpposingFlightBuilder opposingFlightBuilder = new InterceptOpposingFlightBuilder(flight.getFlightInformation());
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (IFlight opposingFlight: opposingFlights)
        {
            flight.getLinkedFlights().addLinkedFlight(opposingFlight);
        }
    }

}

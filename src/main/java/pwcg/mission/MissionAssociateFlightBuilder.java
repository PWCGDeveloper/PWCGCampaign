package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.intercept.InterceptOpposingFlightBuilder;

public class MissionAssociateFlightBuilder
{
    public void buildAssociatedFlights(Mission mission) throws PWCGException
    {
        List<Flight> flightsForMission = mission.getMissionFlightBuilder().getAllAerialFlights();
        for (Flight flight : flightsForMission)
        {
            if (flight.getFlightType() == FlightTypes.INTERCEPT)
            {
                if (flight.isPlayerFlight())
                {
                    InterceptOpposingFlightBuilder opposingFlightBuilder = new InterceptOpposingFlightBuilder(flight.getFlightInformation());
                    opposingFlightBuilder.buildOpposingFlights();
                }
            }
            else if (flight.getFlightType() == FlightTypes.ESCORT)
            {
                throw new PWCGException("Associated flight not implemented for flight type " + flight.getFlightType());
            }
            else if (flight.getFlightType() == FlightTypes.SCRAMBLE)
            {
                throw new PWCGException("Associated flight not implemented for flight type " + flight.getFlightType());
            }
            else if (flight.getFlightType() == FlightTypes.HOME_DEFENSE)
            {
                if (flight.isPlayerFlight())
                {
                    InterceptOpposingFlightBuilder opposingFlightBuilder = new InterceptOpposingFlightBuilder(flight.getFlightInformation());
                    opposingFlightBuilder.buildOpposingFlights();
                }
            }
            else if (flight.getFlightType() == FlightTypes.BALLOON_BUST)
            {
                throw new PWCGException("Associated flight not implemented for flight type " + flight.getFlightType());
            }
            else if (flight.getFlightType() == FlightTypes.BALLOON_DEFENSE)
            {
                throw new PWCGException("Associated flight not implemented for flight type " + flight.getFlightType());
            }
            else if (flight.getFlightType() == FlightTypes.SEA_PATROL)
            {
                throw new PWCGException("Associated flight not implemented for flight type " + flight.getFlightType());
            }
        }
    }
}

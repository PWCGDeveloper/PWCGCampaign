package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.EscortFlightBuilder;
import pwcg.mission.flight.escort.NeedsEscortDecider;
import pwcg.mission.flight.intercept.InterceptOpposingFlightBuilder;
import pwcg.mission.flight.scramble.ScrambleOpposingFlightBuilder;

public class MissionAssociateFlightBuilder
{
    public void buildAssociatedFlights(Mission mission) throws PWCGException
    {
        List<IFlight> flightsForMission = mission.getMissionFlightBuilder().getAllAerialFlights();
        for (IFlight flight : flightsForMission)
        {
            if (flight.getFlightInformation().isPlayerFlight())
            {
                if (flight.getFlightInformation().getFlightType() == FlightTypes.INTERCEPT || flight.getFlightInformation().getFlightType() == FlightTypes.HOME_DEFENSE)
                {
                    makeLinkedInterceptFlights(flight);
                }
                else if (flight.getFlightInformation().getFlightType() == FlightTypes.SCRAMBLE)
                {
                    makeLinkedScrambleFlights(flight);
                }
                else if (flight.getFlightInformation().getFlightType() == FlightTypes.BALLOON_BUST)
                {
                    // TODO Flying Circus
                }
                else if (flight.getFlightInformation().getFlightType() == FlightTypes.BALLOON_DEFENSE)
                {
                    // TODO Flying Circus
                }
                
                NeedsEscortDecider needsEscortDecider = new NeedsEscortDecider();
                if (needsEscortDecider.needsEscort(mission, flight))
                {
                    EscortFlightBuilder escortFlightBuilder = new EscortFlightBuilder();
                    escortFlightBuilder.addEscort(mission, flight);
                }
            }
        }
    }

    private void makeLinkedScrambleFlights(IFlight flight) throws PWCGException
    {
        ScrambleOpposingFlightBuilder opposingFlightBuilder = new ScrambleOpposingFlightBuilder(flight.getFlightInformation());
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (IFlight opposingFlight: opposingFlights)
        {
            flight.getLinkedFlights().addLinkedFlight(opposingFlight);
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

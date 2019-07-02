package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.escort.PlayerEscortFlight;
import pwcg.mission.flight.escort.PlayerEscortFlightLinker;
import pwcg.mission.flight.escort.PlayerEscortedFlightBuilder;
import pwcg.mission.flight.intercept.InterceptOpposingFlight;
import pwcg.mission.flight.intercept.InterceptOpposingFlightBuilder;
import pwcg.mission.flight.scramble.ScrambleOpposingFlight;
import pwcg.mission.flight.scramble.ScrambleOpposingFlightBuilder;

public class MissionAssociateFlightBuilder
{
    public void buildAssociatedFlights(Mission mission) throws PWCGException
    {
        List<Flight> flightsForMission = mission.getMissionFlightBuilder().getAllAerialFlights();
        for (Flight flight : flightsForMission)
        {
            if (flight.isPlayerFlight())
            {
                if (flight.getFlightType() == FlightTypes.INTERCEPT)
                {
                    makeLinkedInterceptFlights(flight);
                }
                else if (flight.getFlightType() == FlightTypes.ESCORT)
                {
                    makeLinkedEscortFlights(flight);
                }
                else if (flight.getFlightType() == FlightTypes.SCRAMBLE)
                {
                    makeLinkedScrambleFlights(flight);
                }
                else if (flight.getFlightType() == FlightTypes.HOME_DEFENSE)
                {
                    makeLinkedHomeDefenseFlight(flight);
                }
                else if (flight.getFlightType() == FlightTypes.BALLOON_BUST)
                {
                    // TODO Flying Circus
                }
                else if (flight.getFlightType() == FlightTypes.BALLOON_DEFENSE)
                {
                    // TODO Flying Circus
                }
                else if (flight.getFlightType() == FlightTypes.SEA_PATROL)
                {
                    // TODO Flying Circus
                }
            }
        }
    }

    private void makeLinkedScrambleFlights(Flight flight) throws PWCGException
    {
        ScrambleOpposingFlightBuilder opposingFlightBuilder = new ScrambleOpposingFlightBuilder(flight.getFlightInformation());
        List<ScrambleOpposingFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (ScrambleOpposingFlight opposingFlight: opposingFlights)
        {
            flight.addLinkedUnit(opposingFlight);
        }
    }

    private void makeLinkedInterceptFlights(Flight flight) throws PWCGException
    {
        InterceptOpposingFlightBuilder opposingFlightBuilder = new InterceptOpposingFlightBuilder(flight.getFlightInformation());
        List<InterceptOpposingFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (InterceptOpposingFlight opposingFlight: opposingFlights)
        {
            flight.addLinkedUnit(opposingFlight);
        }
    }

    private void makeLinkedEscortFlights(Flight flight) throws PWCGException
    {
        PlayerEscortFlight escortFlight = (PlayerEscortFlight)flight;
        PlayerEscortedFlightBuilder playerEscortedFlightBuilder = new PlayerEscortedFlightBuilder();
        Flight escortedFlight = playerEscortedFlightBuilder.createEscortedFlight(escortFlight);
        
        PlayerEscortFlightLinker playerEscortFlightLinker = new PlayerEscortFlightLinker(escortFlight, escortedFlight);
        playerEscortFlightLinker.linkEscortedFlight();
        
        escortFlight.addLinkedUnit(escortedFlight);
    }

    private void makeLinkedHomeDefenseFlight(Flight flight) throws PWCGException
    {
        InterceptOpposingFlightBuilder opposingFlightBuilder = new InterceptOpposingFlightBuilder(flight.getFlightInformation());
        opposingFlightBuilder.buildOpposingFlights();
    }

}

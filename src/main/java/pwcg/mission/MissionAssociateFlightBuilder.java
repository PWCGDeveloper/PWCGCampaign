package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.balloonBust.BalloonBustOpposingFlightBuilder;
import pwcg.mission.flight.balloondefense.BalloonDefenseOpposingFlightBuilder;
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
                if (flight.getFlightType() == FlightTypes.INTERCEPT)
                {
                    makeLinkedInterceptFlights(flight);
                }
                else if (flight.getFlightType() == FlightTypes.BALLOON_BUST)
                {
                    makeLinkedBalloonBustFlights(flight);
                }
                else if (flight.getFlightType() == FlightTypes.BALLOON_DEFENSE)
                {
                    makeLinkedBalloonDefenseFlights(flight);
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

    private void makeLinkedBalloonBustFlights(IFlight flight) throws PWCGException
    {
        BalloonBustOpposingFlightBuilder opposingFlightBuilder = new BalloonBustOpposingFlightBuilder(flight.getFlightInformation());
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (IFlight opposingFlight: opposingFlights)
        {
            flight.getLinkedFlights().addLinkedFlight(opposingFlight);
        }
    }

    private void makeLinkedBalloonDefenseFlights(IFlight flight) throws PWCGException
    {
        BalloonDefenseOpposingFlightBuilder opposingFlightBuilder = new BalloonDefenseOpposingFlightBuilder(flight.getFlightInformation());
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (IFlight opposingFlight: opposingFlights)
        {
            flight.getLinkedFlights().addLinkedFlight(opposingFlight);
        }
    }
}

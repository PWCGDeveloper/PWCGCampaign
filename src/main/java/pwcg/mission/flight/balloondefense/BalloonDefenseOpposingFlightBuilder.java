package pwcg.mission.flight.balloondefense;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.balloonBust.BalloonBustFlight;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class BalloonDefenseOpposingFlightBuilder
{
    private IFlightInformation playerFlightInformation;
    private IGroundUnitCollection balloonUnit;

    public BalloonDefenseOpposingFlightBuilder(IFlightInformation playerFlightInformation, IGroundUnitCollection balloonUnit)
    {
        this.playerFlightInformation = playerFlightInformation;
        this.balloonUnit = balloonUnit;
    }

    public List<IFlight> buildOpposingFlights() throws PWCGException
    {
        BalloonDefenseOpposingFlightSquadronChooser opposingSquadronChooser = new BalloonDefenseOpposingFlightSquadronChooser(playerFlightInformation);
        List<Squadron> opposingSquadrons = opposingSquadronChooser.getOpposingSquadrons();            
        return createOpposingFlights(opposingSquadrons);
    }
    
    private List<IFlight> createOpposingFlights(List<Squadron> opposingSquadrons) throws PWCGException
    {
        List<IFlight> opposingFlights = new ArrayList<>();
        for (Squadron squadron : opposingSquadrons)
        {
            IFlight opposingFlight = createOpposingFlight(squadron);
            if (opposingFlight != null)
            {
                opposingFlights.add(opposingFlight);
            }
        }
        return opposingFlights;
    }

    private IFlight createOpposingFlight(Squadron opposingSquadron) throws PWCGException
    {
        IFlight opposingBalloonBustFlight = null;
        String opposingFieldName = opposingSquadron.determineCurrentAirfieldName(playerFlightInformation.getCampaign().getDate());
        if (opposingFieldName != null)
        {
            opposingBalloonBustFlight = buildOpposingFlight(opposingSquadron);
        }
        return opposingBalloonBustFlight;
    }

    private IFlight buildOpposingFlight(Squadron opposingSquadron) throws PWCGException 
    {
        boolean isPlayerFlight = false;
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(this.playerFlightInformation.getMission(), opposingSquadron, isPlayerFlight);
        IFlightInformation opposingFlightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BALLOON_BUST);
        opposingFlightInformation.getTargetDefinition().setTargetPosition(balloonUnit.getPosition());

        BalloonBustFlight opposingFlight = new BalloonBustFlight(opposingFlightInformation);
        opposingFlight.createFlight();
        return opposingFlight;
    }
}

package pwcg.mission.flight.cap;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.attack.GroundAttackPackage;
import pwcg.mission.target.TargetDefinition;

public class CAPOpposingFlightBuilder
{
    private IFlightInformation playerFlightInformation;
    private TargetDefinition playerTargetDefinition;

    public CAPOpposingFlightBuilder(IFlight playerFlight)
    {
        this.playerFlightInformation = playerFlight.getFlightInformation();
        this.playerTargetDefinition = playerFlight.getTargetDefinition();
    }

    public IFlight buildOpposingFlights() throws PWCGException
    {
        CAPOpposingFlightSquadronChooser opposingFlightSquadronChooser = new CAPOpposingFlightSquadronChooser(playerFlightInformation);
        Squadron opposingSquadron = opposingFlightSquadronChooser.getOpposingSquadrons();
        if (opposingSquadron != null)
        {
            return createOpposingFlights(opposingSquadron);
        }
        else
        {
            return null;
        }
    }
    
    private IFlight createOpposingFlights(Squadron opposingSquadron) throws PWCGException
    {
        IFlight opposingFlight = createOpposingFlight(opposingSquadron);
        return opposingFlight;
    }

    private IFlight createOpposingFlight(Squadron opposingSquadron) throws PWCGException
    {
        IFlight interceptOpposingFlight = null;

        String opposingFieldName = opposingSquadron.determineCurrentAirfieldName(playerFlightInformation.getCampaign().getDate());
        if (opposingFieldName != null)
        {
            Coordinate startingPosition = determineOpposingFlightStartPosition(opposingFieldName);
            interceptOpposingFlight = buildOpposingFlight(opposingSquadron, startingPosition);
        }
        
        return interceptOpposingFlight;
    }

    private Coordinate determineOpposingFlightStartPosition(String opposingFieldName) throws PWCGException
    {
        return playerTargetDefinition.getPosition().copy();
    }

    private IFlight buildOpposingFlight(Squadron opposingSquadron, Coordinate startingPosition) throws PWCGException 
    {
        FlightTypes opposingFlightType = getFlightType(opposingSquadron);
        
        boolean isPlayerFlight = false;
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(playerFlightInformation.getMission(), opposingSquadron, isPlayerFlight);        
        IFlight opposingFlight = buildOpposingFlight(flightBuildInformation, opposingFlightType);
        
        opposingFlight.createFlight();
        return opposingFlight;
    }
    
    private FlightTypes getFlightType(Squadron opposingSquadron) throws PWCGException
    {
        return FlightTypes.GROUND_ATTACK;
    }
    
    private IFlight buildOpposingFlight(FlightBuildInformation opposingFlightBuildInformation, FlightTypes flightType) throws PWCGException
    {
        GroundAttackPackage bombingPackage = new GroundAttackPackage();
        return bombingPackage.createPackage(opposingFlightBuildInformation);
    }
}

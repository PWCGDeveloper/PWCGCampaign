package pwcg.mission.target;

import java.util.Arrays;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.locator.TargetLocatorOpposing;

public class TargetDefinitionBuilderOpposing implements ITargetDefinitionBuilder
{
    private FlightInformation flightInformation;

    public TargetDefinitionBuilderOpposing(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public TargetDefinition buildTargetDefinition() throws PWCGException
    {
        if (findOpposingFlight() == null)
        {
            return revertToStandardTargetDefinitionBuilder();
        }
        else
        {
            return buildOpposingTargetDefinition();
        }
    }

    private TargetDefinition revertToStandardTargetDefinitionBuilder() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }

    private TargetDefinition buildOpposingTargetDefinition() throws PWCGException
    {
        Coordinate targetLocation = createTargetLocation();
        ICountry targetCountry = PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(flightInformation.getSquadron().determineEnemySide());
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_AIR, targetLocation, targetCountry);
        return targetDefinition;
    }

    private Coordinate createTargetLocation() throws PWCGException
    {
        if (flightInformation.getFlightType() == FlightTypes.LOW_ALT_CAP ||
            flightInformation.getFlightType() == FlightTypes.INTERCEPT ||
            flightInformation.getFlightType() == FlightTypes.SCRAMBLE ||
            flightInformation.getFlightType() == FlightTypes.BALLOON_BUST ||
            flightInformation.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            IFlight opposingFlight = findOpposingFlight();
            TargetLocatorOpposing targetLocatorOpposing = new TargetLocatorOpposing(opposingFlight);
            return targetLocatorOpposing.getOpposingLocationFromTargetWaypoints();
        }        
        else if (flightInformation.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT)
        {
            throw new PWCGException("Strategic intercept not yet implemented ");
        }
        else
        {
            throw new PWCGException("No target locations for flight type " + flightInformation.getFlightType());
        }
    }

    private IFlight findOpposingFlight() throws PWCGException
    {
        for (FlightTypes opposingFlightType : findOpposingFlightTypes())
        {
            IFlight opposingFlight = findOpposingFlightOfType(opposingFlightType);
            if (opposingFlight != null)
            {
                return opposingFlight;
            }
        }
        
        return null;
    }
    

    private List<FlightTypes> findOpposingFlightTypes() throws PWCGException
    {
        if (flightInformation.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            return Arrays.asList(FlightTypes.GROUND_ATTACK);
        }
        else if (flightInformation.getFlightType() == FlightTypes.INTERCEPT)
        {
            return Arrays.asList(FlightTypes.BOMB, FlightTypes.TRANSPORT, FlightTypes.DIVE_BOMB);
        }
        else if (flightInformation.getFlightType() == FlightTypes.BALLOON_BUST)
        {
            return Arrays.asList(FlightTypes.BALLOON_DEFENSE);
        }
        else if (flightInformation.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            return Arrays.asList(FlightTypes.BALLOON_BUST);
        }
        else if (flightInformation.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            return Arrays.asList(FlightTypes.BALLOON_BUST);
        }
        else if (flightInformation.getFlightType() == FlightTypes.SCRAMBLE)
        {
            return Arrays.asList(FlightTypes.BOMB, FlightTypes.DIVE_BOMB, FlightTypes.GROUND_ATTACK);
        }
        else if (flightInformation.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT)
        {
            throw new PWCGException("Strategic intercept not yet implemented ");
        }
        else
        {
            throw new PWCGException("No opposing flight type for flight type " + flightInformation.getFlightType());
        }
    }

    private IFlight findOpposingFlightOfType(FlightTypes opposingFlightType) throws PWCGException
    {
        Side enemySide = flightInformation.getSquadron().determineEnemySide();
        for (IFlight opposingFlight: flightInformation.getMission().getMissionFlightBuilder().getAiFlightsForSide(enemySide))
        {
            if (opposingFlight.getFlightInformation().isOpposingFlight())
            {
                if (opposingFlight.getFlightInformation().getFlightType() == opposingFlightType)
                {
                    return opposingFlight;
                }
            }
        }
        
        return null;
    }

}

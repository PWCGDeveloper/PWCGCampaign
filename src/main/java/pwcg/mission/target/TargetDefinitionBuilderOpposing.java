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
    private TargetDefinition targetDefinition;
    private FlightInformation playerFlightInformation;
    private IFlight opposingFlight;

    public TargetDefinitionBuilderOpposing(FlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public TargetDefinition buildTargetDefinition() throws PWCGException
    {
        opposingFlight = findOpposingFlight();
        if (opposingFlight == null)
        {
            revertToStandardTargetDefinitionBuilder();
        }
        else
        {
            buildOpposingTargetDefinition();
        }
        return targetDefinition;
    }

    private void revertToStandardTargetDefinitionBuilder() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(playerFlightInformation);
        targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
    }

    private void buildOpposingTargetDefinition() throws PWCGException
    {
        Coordinate targetLocation = createTargetLocation();
        ICountry targetCountry = PWCGContext.getInstance().getMap(playerFlightInformation.getCampaignMap()).getGroundCountryForMapBySide(playerFlightInformation.getSquadron().determineEnemySide());
        targetDefinition = new TargetDefinition(TargetType.TARGET_AIR, targetLocation, targetCountry, "Opposing Air");
        targetDefinition.setOpposingFlight(opposingFlight);
    }

    private Coordinate createTargetLocation() throws PWCGException
    {
        if (playerFlightInformation.getFlightType() == FlightTypes.LOW_ALT_CAP ||
            playerFlightInformation.getFlightType() == FlightTypes.INTERCEPT ||
            playerFlightInformation.getFlightType() == FlightTypes.SCRAMBLE ||
            playerFlightInformation.getFlightType() == FlightTypes.BALLOON_BUST ||
            playerFlightInformation.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            TargetLocatorOpposing targetLocatorOpposing = new TargetLocatorOpposing(opposingFlight);
            return targetLocatorOpposing.getOpposingLocationFromTargetWaypoints();
        }        
        else if (playerFlightInformation.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT)
        {
            throw new PWCGException("Invalid path for Strategic Intercept");
        }
        else
        {
            throw new PWCGException("No target locations for flight type " + playerFlightInformation.getFlightType());
        }
    }

    private IFlight findOpposingFlight() throws PWCGException
    {
        Side enemySide = playerFlightInformation.getSquadron().determineEnemySide();
        List<FlightTypes> opposingFlightTypes = findOpposingFlightTypes();
        return playerFlightInformation.getMission().getFlights().findOpposingFlight(opposingFlightTypes, enemySide);
    }

    private List<FlightTypes> findOpposingFlightTypes() throws PWCGException
    {
        if (playerFlightInformation.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            return Arrays.asList(FlightTypes.GROUND_ATTACK);
        }
        else if (playerFlightInformation.getFlightType() == FlightTypes.INTERCEPT)
        {
            return Arrays.asList(FlightTypes.BOMB, FlightTypes.TRANSPORT, FlightTypes.DIVE_BOMB);
        }
        else if (playerFlightInformation.getFlightType() == FlightTypes.BALLOON_BUST)
        {
            return Arrays.asList(FlightTypes.BALLOON_DEFENSE);
        }
        else if (playerFlightInformation.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            return Arrays.asList(FlightTypes.BALLOON_BUST);
        }
        else if (playerFlightInformation.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            return Arrays.asList(FlightTypes.BALLOON_BUST);
        }
        else if (playerFlightInformation.getFlightType() == FlightTypes.SCRAMBLE)
        {
            return Arrays.asList(FlightTypes.LOW_ALT_BOMB, FlightTypes.DIVE_BOMB, FlightTypes.GROUND_ATTACK);
        }
        else if (playerFlightInformation.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT)
        {
            throw new PWCGException("Strategic intercept not yet implemented ");
        }
        else
        {
            throw new PWCGException("No opposing flight type for flight type " + playerFlightInformation.getFlightType());
        }
    }

    public IFlight getOpposingFlight()
    {
        return opposingFlight;
    }
}

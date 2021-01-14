package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.locator.TargetLocatorAir;

public class TargetDefinitionBuilderAirToAir implements ITargetDefinitionBuilder
{
    private FlightInformation flightInformation;

    public TargetDefinitionBuilderAirToAir(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public TargetDefinition buildTargetDefinition() throws PWCGException
    {
        Coordinate targetLocation = createTargetLocation();
        ICountry targetCountry = PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(flightInformation.getSquadron().determineEnemySide());
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_AIR, targetLocation, targetCountry);
        return targetDefinition;
    }

    private Coordinate createTargetLocation() throws PWCGException
    {
        TargetLocatorAir targetLocatorAir = new TargetLocatorAir(flightInformation);

        if (flightInformation.getFlightType() == FlightTypes.PATROL || 
            flightInformation.getFlightType() == FlightTypes.LOW_ALT_PATROL || 
            flightInformation.getFlightType() == FlightTypes.LONE_WOLF)
        {
            return targetLocatorAir.getFrontCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            return targetLocatorAir.getBattleCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.INTERCEPT || flightInformation.getFlightType() == FlightTypes.RECON)
        {
            return targetLocatorAir.getInterceptCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT)
        {
            return flightInformation.getMission().getMissionBorders().getCenter();
        }
        else if (flightInformation.getFlightType() == FlightTypes.OFFENSIVE || flightInformation.getFlightType() == FlightTypes.SPY_EXTRACT)
        {
            return targetLocatorAir.getEnemyTerritoryPatrolCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.TRANSPORT)
        {
            return targetLocatorAir.getTransportAirfieldCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.BALLOON_BUST)
        {
            return targetLocatorAir.getBalloonCoordinate(flightInformation.getSquadron().determineSide());
        }
        else if (flightInformation.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            return targetLocatorAir.getBalloonCoordinate(flightInformation.getSquadron().determineSide().getOppositeSide());
        }
        else if (flightInformation.getFlightType() == FlightTypes.FERRY)
        {
            return targetLocatorAir.getTransportAirfieldCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.SCRAMBLE)
        {
            return targetLocatorAir.getScrambleCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.ESCORT)
        {
            if (flightInformation.isPlayerFlight())
            {
                return targetLocatorAir.getPlayerEscortRendezvousCoordinate();
            }
            else
            {
                return targetLocatorAir.getEscortForPlayerRendezvousCoordinate();
            }
        }

        throw new PWCGException("No target locations for flight type " + flightInformation.getFlightType());
    }
}

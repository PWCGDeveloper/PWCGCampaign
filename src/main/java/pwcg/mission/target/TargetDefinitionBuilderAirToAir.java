package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
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
        ICountry targetCountry = PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(flightInformation.getCountry().getSide().getOppositeSide());
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_AIR, targetLocation, targetCountry, "Planes");
        return targetDefinition;
    }

    private Coordinate createTargetLocation() throws PWCGException
    {
        TargetLocatorAir targetLocatorAir = new TargetLocatorAir(flightInformation);

        if (flightInformation.getFlightType() == FlightTypes.LOW_ALT_PATROL)
        {
            return targetLocatorAir.getFrontCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            return targetLocatorAir.getBattleCoordinate();
        }

        throw new PWCGException("No target locations for flight type " + flightInformation.getFlightType());
    }
}

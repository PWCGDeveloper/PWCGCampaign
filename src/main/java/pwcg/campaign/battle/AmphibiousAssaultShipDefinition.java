package pwcg.campaign.battle;

import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.builder.AmphibiousAssaultShipClass;
import pwcg.mission.ground.org.GroundUnitCollection;

public class AmphibiousAssaultShipDefinition
{
    private String shipType;
    private AmphibiousAssaultShipClass shipClass;
    private Coordinate destination;
    private Orientation orientation;
    private GroundUnitCollection landingCraftGroundUnit;

    public String getShipType()
    {
        return shipType;
    }

    public void setShipType(String shipType)
    {
        this.shipType = shipType;
    }

    public AmphibiousAssaultShipClass getShipClass()
    {
        return shipClass;
    }

    public void setShipClass(AmphibiousAssaultShipClass shipClass)
    {
        this.shipClass = shipClass;
    }

    public Coordinate getDestination()
    {
        return destination;
    }

    public void setDestination(Coordinate destination)
    {
        this.destination = destination;
    }

    public Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }

    public GroundUnitCollection getLandingCraftGroundUnit()
    {
        return landingCraftGroundUnit;
    }

    public void setLandingCraftGroundUnit(GroundUnitCollection landingCraftGroundUnit)
    {
        this.landingCraftGroundUnit = landingCraftGroundUnit;
    }

}

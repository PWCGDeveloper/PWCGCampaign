package pwcg.campaign.battle;

import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.org.GroundUnitCollection;

public class AmphibiousAssaultShipDefinition
{
    private String shipType;
    private Coordinate destination;
    private Orientation orientation;
    private GroundUnitCollection landingCraftGroundUnit;

    public String getShipType()
    {
        return shipType;
    }

    public Coordinate getDestination()
    {
        return destination;
    }

    public Orientation getOrientation()
    {
        return orientation;
    }

    public GroundUnitCollection getLandingCraftGroundUnit()
    {
        return landingCraftGroundUnit;
    }

    public void setGroundUnit(GroundUnitCollection landingCraftGroundUnit)
    {
        this.landingCraftGroundUnit = landingCraftGroundUnit;
    }

}

package pwcg.campaign.group.airfield.hotspot;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;

public class AirfieldHotSpotDefinition
{
    private String model = "";
    private double distance;
    private double angle;

    public AirfieldHotSpotDefinition()
    {
    }
    
    
    public HotSpot convert(FrontMapIdentifier mapIdentifier, Coordinate position, Orientation orientation) throws PWCGException
    {
        double angleForThisField = MathUtils.adjustAngle(orientation.getyOri(), angle);
        Coordinate hotSpotPosition = MathUtils.calcNextCoord(mapIdentifier, position.copy(), angleForThisField, distance);

        Orientation hotSpotOrientation = Orientation.createRandomOrientation();
        
        HotSpot hotSpot = new HotSpot();
        hotSpot.setPosition(hotSpotPosition);
        hotSpot.setOrientation(hotSpotOrientation);
        
        return hotSpot;
 
    }


    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    public double getAngle()
    {
        return angle;
    }

    public void setAngle(double angle)
    {
        this.angle = angle;
    }
    
    
}

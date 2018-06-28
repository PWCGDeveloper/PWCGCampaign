package pwcg.campaign.group.airfield;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class AirfieldHotSpotDefinition
{
    private String model = "";
    private double distance;
    private double angle;

    public AirfieldHotSpotDefinition()
    {
    }
    
    
    public HotSpot convert(Coordinate position, Orientation orientation) throws PWCGException
    {
        double angleForThisField = MathUtils.adjustAngle(orientation.getyOri(), angle);
        Coordinate hotSpotPosition = MathUtils.calcNextCoord(position.copy(), angleForThisField, distance);

        // Finally put the object in its place - orientation is random
        double objectOrientation = RandomNumberGenerator.getRandom(360);
        Orientation hotSpotOrientation = new Orientation();
        hotSpotOrientation.setyOri(objectOrientation);
        
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

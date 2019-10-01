package pwcg.campaign.target.locator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.ShippingLaneIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;

public class ShippingLaneManager
{
	
	private ShippingLanes shippingLanes = new ShippingLanes();

	public ShippingLaneManager()
	{
	}

	public void configure(String mapName) throws PWCGException
	{
		shippingLanes = ShippingLaneIOJson.readJson(mapName);
		if (shippingLanes == null)
		{
		    shippingLanes = new ShippingLanes();
		}
	}

	public ShippingLane getTargetShippingLane(Coordinate referenceCoord, double maxDistannce, Side side) throws PWCGException 
	{
		if (side == Side.ALLIED)
		{
			return getTargetShippingLane(shippingLanes.getAlliedShippingLanes(), referenceCoord, maxDistannce);
		}
		else
		{
			return getTargetShippingLane(shippingLanes.getAxisShippingLanes(), referenceCoord, maxDistannce);			
		}
	}
	   
    private ShippingLane getTargetShippingLane(List<ShippingLane> shippingLanes, Coordinate referenceCoord, double maxDistance) throws PWCGException 
    {        
        ShippingLane selectedShippingLane =  null; 

        List<ShippingLane> randomizedShippingLanes = new ArrayList<ShippingLane>();
        randomizedShippingLanes.addAll(shippingLanes);
        Collections.shuffle(randomizedShippingLanes);

        for (ShippingLane shippingLane : randomizedShippingLanes)
        {
            selectedShippingLane = shippingLane;
            CoordinateBox shippingLaneBorders = shippingLane.getShippingLaneBorders();
            Coordinate centerOfBox = shippingLaneBorders.getCenter();

            double distanceFromBase = MathUtils.calcDist(referenceCoord, centerOfBox);
            if (distanceFromBase < maxDistance)
            {
                break;
            }
        }

        return selectedShippingLane;
    }
    
    public ShippingLane getClosestShippingLaneBySide(Coordinate targetGeneralLocation, Side side) throws PWCGException
    {
        if (side == Side.ALLIED)
        {
            return getClosestShippingLane(shippingLanes.getAlliedShippingLanes(), targetGeneralLocation);
        }
        else
        {
            return getClosestShippingLane(shippingLanes.getAxisShippingLanes(), targetGeneralLocation);
        }
    }
    
    public ShippingLane getClosestShippingLane(Coordinate targetGeneralLocation) throws PWCGException
    {
        List<ShippingLane> allShippingLanes = new ArrayList<>();
        allShippingLanes.addAll(shippingLanes.getAlliedShippingLanes());
        allShippingLanes.addAll(shippingLanes.getAxisShippingLanes());
        return getClosestShippingLane(allShippingLanes, targetGeneralLocation);
    }

    private ShippingLane getClosestShippingLane(List<ShippingLane> shippingLanes, Coordinate targetGeneralLocation) throws PWCGException
    {
        ShippingLane closestShippingLane = null;
        double closestDistance = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        for (ShippingLane shippingLane : shippingLanes)
        {
            double distance = MathUtils.calcDist(shippingLane.getShippingLaneBox().getCenter(), targetGeneralLocation);
            if (distance < closestDistance)
            {
                closestShippingLane = shippingLane;
                closestDistance = distance;
            }
        }
        return closestShippingLane;
    }
}

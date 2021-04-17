package pwcg.campaign.shipping;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;

public class ShippingLanes
{
	private List<ShippingLane> axisShippingLanes = new ArrayList<ShippingLane>();
	private List<ShippingLane> alliedShippingLanes = new ArrayList<ShippingLane>();

	public List<ShippingLane> getAxisShippingLanes()
	{
		return axisShippingLanes;
	}

	public void setAxisShippingLanes(List<ShippingLane> axisShippingLanes)
	{
		this.axisShippingLanes = axisShippingLanes;
	}

	public List<ShippingLane> getAlliedShippingLanes()
	{
		return alliedShippingLanes;
	}

	public void setAlliedShippingLanes(List<ShippingLane> alliedShippingLanes)
	{
		this.alliedShippingLanes = alliedShippingLanes;
	}

	public void addAlliedShippingLane(ShippingLane shippingLane)
	{
		alliedShippingLanes.add(shippingLane);
	}

	public void addAxisShippingLane(ShippingLane shippingLane)
	{
		axisShippingLanes.add(shippingLane);
	}

    public ShippingLane getClosestShippingLaneBySide(Coordinate targetGeneralLocation, Side side) throws PWCGException
    {
        if (side == Side.ALLIED)
        {
            return getClosestShippingLane(alliedShippingLanes, targetGeneralLocation);
        }
        else
        {
            return getClosestShippingLane(axisShippingLanes, targetGeneralLocation);
        }
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

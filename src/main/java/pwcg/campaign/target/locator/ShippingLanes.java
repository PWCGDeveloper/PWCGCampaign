package pwcg.campaign.target.locator;

import java.util.ArrayList;
import java.util.List;

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

}

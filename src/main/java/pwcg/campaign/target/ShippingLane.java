package pwcg.campaign.target;

import java.util.Date;

import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;

public class ShippingLane
{
	private Coordinate neCorner;
	private Coordinate swCorner;
	private Country country;
	private Date startDate;
	private Date endDate;
	
	/**
	 * @throws PWCGException 
	 * 
	 */
	public ShippingLane(Coordinate nwCorner, Coordinate seCorner, Country country)
	{
		this.neCorner = nwCorner.copy();
		this.swCorner = seCorner.copy();
		this.country = country;		
	}

	public CoordinateBox getShippingLaneBox() throws PWCGException {
	    CoordinateBox coordinateBorders = CoordinateBox.coordinateBoxFromCorners(swCorner, neCorner);
		return coordinateBorders;
	}

	public Country getCountry() {
		return country;
	}

    public CoordinateBox getShippingLaneBorders() throws PWCGException 
    {
        CoordinateBox coordinateBorders = CoordinateBox.coordinateBoxFromCorners(swCorner, neCorner);
        return coordinateBorders;
    }

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
}

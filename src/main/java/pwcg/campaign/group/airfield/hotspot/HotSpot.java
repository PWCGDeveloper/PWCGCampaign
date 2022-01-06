package pwcg.campaign.group.airfield.hotspot;

import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;

public class HotSpot
{
    private Coordinate position = new Coordinate();
	private Orientation orientation = new Orientation();
	private HotSpotType hotSpotType = HotSpotType.HOTSPOT_UNUSED;
	
    public Coordinate getPosition()
    {
        return position;
    }
    public void setPosition(Coordinate position)
    {
        this.position = position;
    }
    public Orientation getOrientation()
    {
        return orientation;
    }
    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }
    public HotSpotType getHotSpotType()
    {
        return hotSpotType;
    }
    public void setHotSpotType(HotSpotType hotSpotType)
    {
        this.hotSpotType = hotSpotType;
    }
	
	
}

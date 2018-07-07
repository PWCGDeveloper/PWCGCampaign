package pwcg.aar.ui.events.model;

import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;

public class PlaneStatusEvent extends AAREvent
{
    private EquippedPlane  plane;
	private int planeStatus = PlaneStatus.STATUS_DEPLOYED;
	
	public PlaneStatusEvent ()
	{
	}

    public int getPlaneStatus()
    {
        return planeStatus;
    }

    public void setPlaneStatus(int planeStatus)
    {
        this.planeStatus = planeStatus;
    }

    public EquippedPlane getPlane()
    {
        return plane;
    }

    public void setPlane(EquippedPlane plane)
    {
        this.plane = plane;
    }
}

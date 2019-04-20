package pwcg.aar.ui.events.model;

import pwcg.campaign.plane.PlaneStatus;

public class PlaneStatusEvent extends AAREvent
{
    private int planeSerialNumber;
    private int squadronId;
	private int planeStatus = PlaneStatus.STATUS_DEPLOYED;
	
    public PlaneStatusEvent (int squadronId)
    {
        this.squadronId = squadronId;
    }

    public int getSquadronId()
    {
        return squadronId;
    }

    public void setSquadronId(int squadronId)
    {
        this.squadronId = squadronId;
    }

    public int getPlaneStatus()
    {
        return planeStatus;
    }

    public void setPlaneStatus(int planeStatus)
    {
        this.planeStatus = planeStatus;
    }

    public int getPlaneSerialNumber()
    {
        return planeSerialNumber;
    }

    public void setPlaneSerialNumber(int planeSerialNumber)
    {
        this.planeSerialNumber = planeSerialNumber;
    }
}

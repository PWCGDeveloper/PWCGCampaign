package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.plane.PlaneStatus;

public class PlaneStatusEvent extends AAREvent
{
    private int planeSerialNumber;
    private int squadronId;
	private int planeStatus = PlaneStatus.STATUS_DEPLOYED;
	
    public PlaneStatusEvent(int planeSerialNumber, int squadronId, int planeStatus, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.planeSerialNumber = planeSerialNumber;
        this.squadronId = squadronId;
        this.planeStatus = planeStatus;
    }

    public int getSquadronId()
    {
        return squadronId;
    }

    public int getPlaneStatus()
    {
        return planeStatus;
    }

    public int getPlaneSerialNumber()
    {
        return planeSerialNumber;
    }
}

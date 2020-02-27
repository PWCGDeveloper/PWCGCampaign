package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;

public class LeaveEvent extends AARPilotEvent
{
    private int leaveTime = 0;
	
    public LeaveEvent(Campaign campaign, int leaveTime, int squadronId, int pilotSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, pilotSerialNumber, date, isNewsWorthy);
        this.leaveTime = leaveTime;
    }

    public int getLeaveTime()
    {
        return leaveTime;
    }
}

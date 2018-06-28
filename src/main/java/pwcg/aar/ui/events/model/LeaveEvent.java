package pwcg.aar.ui.events.model;

public class LeaveEvent extends AARPilotEvent
{
    private int leaveTime = 0;
	
	public LeaveEvent ()
	{
	}


    public int getLeaveTime()
    {
        return leaveTime;
    }

    public void setLeaveTime(int leaveTime)
    {
        this.leaveTime = leaveTime;
    }
}

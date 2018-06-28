package pwcg.aar.ui.events.model;

public class TransferEvent extends AARPilotEvent
{
	private boolean transferIn = false;
    private int transferFrom;
    private int transferTo;
    private int leaveTime = 0;
	
	public TransferEvent ()
	{
	}

	public boolean isTransferIn() {
		return transferIn;
	}

	public void setTransferIn(boolean transferIn) {
		this.transferIn = transferIn;
	}

	public int getTransferTo()
    {
        return transferTo;
    }

	public void setTransferTo(int transferTo)
    {
        this.transferTo = transferTo;
    }

    public int getTransferFrom()
    {
        return this.transferFrom;
    }

    public void setTransferFrom(int transferFrom)
    {
        this.transferFrom = transferFrom;
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

package pwcg.aar.ui.events.model;

public class TransferEvent extends AARPilotEvent
{
    private int transferFrom;
    private int transferTo;
    private int leaveTime = 0;
	
    public TransferEvent(int squadronId)
    {
        super(squadronId);
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

package pwcg.aar.ui.events.model;

public class AceLeaveEvent  extends AARPilotEvent
{
	private String status = "";

    public AceLeaveEvent(int squadronId)
    {
        super(squadronId);
    }

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

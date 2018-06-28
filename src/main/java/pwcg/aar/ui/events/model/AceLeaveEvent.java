package pwcg.aar.ui.events.model;

public class AceLeaveEvent  extends AARPilotEvent
{
	private String status = "";

	public AceLeaveEvent ()
	{
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

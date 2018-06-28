package pwcg.aar.ui.events.model;

public class AceKilledEvent  extends AARPilotEvent
{
	private String status = "";

	public AceKilledEvent ()
	{
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

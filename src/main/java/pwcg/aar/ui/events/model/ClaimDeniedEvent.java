package pwcg.aar.ui.events.model;

public class ClaimDeniedEvent extends AARPilotEvent
{
	private String type = "";
	
    public ClaimDeniedEvent(int squadronId)
    {
        super(squadronId);
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

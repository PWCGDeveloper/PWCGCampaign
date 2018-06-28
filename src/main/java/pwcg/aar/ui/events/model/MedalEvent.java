package pwcg.aar.ui.events.model;

public class MedalEvent  extends AARPilotEvent
{
	private String medal = "";

	public MedalEvent ()
	{
	}

	public String getMedal() {
		return medal;
	}

	public void setMedal(String medal) {
		this.medal = medal;
	}
}

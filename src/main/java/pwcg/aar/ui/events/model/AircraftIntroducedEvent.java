package pwcg.aar.ui.events.model;

public class AircraftIntroducedEvent  extends AAREvent
{
	private String aircraft = "";

	public AircraftIntroducedEvent ()
	{
	}

	public String getAircraft() {
		return aircraft;
	}

	public void setAircraft(String aircraft) {
		this.aircraft = aircraft;
	}
}

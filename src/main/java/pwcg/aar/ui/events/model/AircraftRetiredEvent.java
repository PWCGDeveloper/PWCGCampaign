package pwcg.aar.ui.events.model;

public class AircraftRetiredEvent  extends AAREvent
{
	private String aircraft = "";

	public AircraftRetiredEvent ()
	{
	}

	public String getAircraft() {
		return aircraft;
	}

	public void setAircraft(String aircraft) {
		this.aircraft = aircraft;
	}
}

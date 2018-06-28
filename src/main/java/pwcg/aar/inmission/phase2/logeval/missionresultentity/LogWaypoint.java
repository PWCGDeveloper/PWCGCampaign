package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.core.location.Coordinate;

public class LogWaypoint extends LogBase
{
	protected Coordinate location;

	public LogWaypoint()
	{
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}
}

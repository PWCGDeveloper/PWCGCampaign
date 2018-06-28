package pwcg.gui.rofmap.brief;

import pwcg.mission.mcu.McuWaypoint;

public class EditorWaypointGroup
{
    private McuWaypoint waypointInBriefing = null;
	private WaypointEditor WaypointEditor = null;
	private BriefingMapPoint briefingMapPoint = null;

	public void updateAltitude(int altitude)
	{
		waypointInBriefing.getPosition().setYPos(altitude);
	}

	public McuWaypoint getWaypointInBriefing()
	{
		return waypointInBriefing;
	}

	public void setWaypointInBriefing(McuWaypoint waypointInBriefing)
	{
		this.waypointInBriefing = waypointInBriefing;
	}

	public WaypointEditor getWaypointEditor()
	{
		return WaypointEditor;
	}

	public void setWaypointEditor(WaypointEditor waypointEditor)
	{
		WaypointEditor = waypointEditor;
	}

	public BriefingMapPoint getBriefingMapPoint()
	{
		return briefingMapPoint;
	}

	public void setBriefingMapPoint(BriefingMapPoint briefingMapPoint)
	{
		this.briefingMapPoint = briefingMapPoint;
	}
}

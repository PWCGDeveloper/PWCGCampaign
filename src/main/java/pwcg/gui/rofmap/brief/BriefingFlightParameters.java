package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.utils.PWCGStringValidator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingFlightParameters
{
    private List<EditorWaypointGroup> briefingMapEditorGroups = new ArrayList<EditorWaypointGroup>();
	private int selectedMapPointIndex = -1;
    private int actionMapPointIndex = -1;
    private IFlight flight;
	
	public BriefingFlightParameters(IFlight flight)
	{
	    this.flight = flight;
	}

    public int getNumWaypoints()
    {
        return briefingMapEditorGroups.size();
    }

    public void removeWayPoint(int index)
    {
        briefingMapEditorGroups.remove(index);
    }
	
	public void appendEditorGroup(EditorWaypointGroup editorWaypointGroup)
	{
        briefingMapEditorGroups.add(briefingMapEditorGroups.size(), editorWaypointGroup);

	}

    public void updatePosition(Coordinate position) throws PWCGException
    {
		if (selectedMapPointIndex != BriefingMapPanel.NO_MAP_POINT_SELECTED)
		{
	        EditorWaypointGroup editorWaypointGroup = briefingMapEditorGroups.get(selectedMapPointIndex);	        
	        McuWaypoint thisWaypoint = setWaypointPosition(position, editorWaypointGroup);
	        resetWaypointEditors(editorWaypointGroup, thisWaypoint);
		}
    }

    private McuWaypoint setWaypointPosition(Coordinate position, EditorWaypointGroup editorWaypointGroup)
    {
        McuWaypoint thisWaypoint = editorWaypointGroup.getWaypointInBriefing();
        position.setYPos(thisWaypoint.getPosition().getYPos());
        thisWaypoint.setPosition(position.copy());
        return thisWaypoint;
    }

    private void resetWaypointEditors(EditorWaypointGroup editorWaypointGroup, McuWaypoint thisWaypoint) throws PWCGException
    {
        McuWaypoint previousWaypoint = null;
        if (selectedMapPointIndex > 0)
        {
            EditorWaypointGroup preveditorWaypointGroup = briefingMapEditorGroups.get(selectedMapPointIndex-1);
            previousWaypoint = preveditorWaypointGroup.getWaypointInBriefing();
        }
        
        editorWaypointGroup.getWaypointEditor().initializeWPEdit(previousWaypoint, thisWaypoint);
    }

    public List<WaypointEditor> getWaypointEditorsInBriefing()
    {
        List<WaypointEditor> waypointEditors = new ArrayList<WaypointEditor>();
        for (EditorWaypointGroup editorWaypointGroup : briefingMapEditorGroups)
        {
        	if (editorWaypointGroup.getWaypointEditor() != null)
        	{
        		waypointEditors.add(editorWaypointGroup.getWaypointEditor());
        	}
        }
        return waypointEditors;
    }

    public List<McuWaypoint> getWaypointsInBriefing()
    {
        List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();
        for (EditorWaypointGroup editorWaypointGroup : briefingMapEditorGroups)
        {
        	if (editorWaypointGroup.getWaypointInBriefing() != null)
        	{
                waypoints.add(editorWaypointGroup.getWaypointInBriefing());
        	}
        }
        return waypoints;
    }

    public void synchronizeAltitudeEdits()
    {
        for (EditorWaypointGroup editorWaypointGroup : briefingMapEditorGroups)
        {
        	WaypointEditor waypointEditor = editorWaypointGroup.getWaypointEditor();
        	if (waypointEditor != null)
        	{
	            String altitudeText = waypointEditor.getAltitudeSetting().getText();
	            if (PWCGStringValidator.isInteger(altitudeText))
	            {
	                int altitude = Integer.valueOf(altitudeText);
	                editorWaypointGroup.updateAltitude(altitude);
	            }
        	}
        }
    }
    
    public BriefingMapPoint getSelectedMapPoint()
    {
    	if (selectedMapPointIndex >= 0)
    	{
            return briefingMapEditorGroups.get(selectedMapPointIndex).getBriefingMapPoint();
    	}
    	
    	return null;
    }

    public double getSelectedFuel()
    {
        return flight.getFlightPlanes().getFlightLeader().getFuel();
    }

    public void setSelectedFuel(double selectedFuel)
    {
        flight.getFlightPlanes().getFlightLeader().setFuel(selectedFuel);
    }

	public List<EditorWaypointGroup> getWaypointEditorGroups()
	{
		return briefingMapEditorGroups;
	}

	public int getSelectedMapPointIndex()
	{
		return selectedMapPointIndex;
	}

	public void setSelectedMapPointIndex(int selectedMapPointIndex)
	{
		this.selectedMapPointIndex = selectedMapPointIndex;
	}

	public int getActionMapPointIndex()
	{
		return actionMapPointIndex;
	}

	public void setActionMapPointIndex(int actionMapPointIndex)
	{
		this.actionMapPointIndex = actionMapPointIndex;
	}
	
	
}

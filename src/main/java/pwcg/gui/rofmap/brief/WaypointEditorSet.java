package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.List;

import pwcg.mission.mcu.McuWaypoint;

public class WaypointEditorSet
{
    private List<WaypointEditor> waypointEditors = new ArrayList<>();

    public WaypointEditor getWaypointEditorByid(long waypointId)
    {
        for (WaypointEditor waypointEditor : waypointEditors)
        {
            if (!(waypointEditor.getAssociatedWaypointID() == McuWaypoint.NO_WAYPOINT_ID))
            {
                if (waypointEditor.getAssociatedWaypointID() == waypointId)
                {
                    return waypointEditor;
                }
            }
        }
        return null;
    }
    
    public void addWaypointEditor(WaypointEditor waypointEditor)
    {
        waypointEditors.add(waypointEditor);
    }
}

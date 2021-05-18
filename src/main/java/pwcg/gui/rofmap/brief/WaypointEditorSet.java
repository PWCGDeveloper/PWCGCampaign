package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.List;

import pwcg.mission.mcu.McuWaypoint;

public class WaypointEditorSet
{
    private List<IWaypointDetails> waypointEditors = new ArrayList<>();

    public IWaypointDetails getWaypointEditorByid(long waypointId)
    {
        for (IWaypointDetails waypointEditor : waypointEditors)
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
    
    public void addWaypointEditor(IWaypointDetails waypointEditor)
    {
        waypointEditors.add(waypointEditor);
    }
}

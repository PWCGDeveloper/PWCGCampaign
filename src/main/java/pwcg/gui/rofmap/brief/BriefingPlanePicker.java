package pwcg.gui.rofmap.brief;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pwcg.campaign.plane.EquippedPlane;
import pwcg.core.exception.PWCGException;
import pwcg.gui.helper.BriefingMissionFlight;

public class BriefingPlanePicker
{
    private BriefingMissionFlight missionEditHandler;
    private JComponent parent;
    
    public BriefingPlanePicker(BriefingMissionFlight missionEditHandler, JComponent parent)
    {
        this.missionEditHandler = missionEditHandler;
        this.parent = parent;
    }

    public Integer pickPlane(Integer pilotSerialNumber) throws PWCGException
    {       
        List<EquippedPlane> squadronPlanes = missionEditHandler.getSortedUnassignedPlanes();
        Object[] possibilities = new Object[squadronPlanes.size()];
        for (int i = 0; i < squadronPlanes.size(); ++i)
        {
            EquippedPlane plane = squadronPlanes.get(i);
            PickerEntry entry = new PickerEntry();
            entry.description = plane.getDisplayName() + " (" + plane.getDisplayMarkings() + ")";
            entry.plane = plane;
            possibilities[i] = entry;
        }
        
        PickerEntry pickedPlane = (PickerEntry)JOptionPane.showInputDialog(
                parent, 
                "Select Plane", 
                "Select Plane", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                possibilities, 
                null);
        
        if (pickedPlane != null)
            return pickedPlane.plane.getSerialNumber();

        return null;
    }    

    private static class PickerEntry
    {
        public String description;
        public EquippedPlane plane;

        public String toString()
        {
            return description;
        }
    }
}

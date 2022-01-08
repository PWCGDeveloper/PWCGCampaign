package pwcg.gui.rofmap.brief;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingUnit;

public class BriefingTankPicker
{
    private BriefingUnit missionEditHandler;
    private JComponent parent;
    
    public BriefingTankPicker(BriefingUnit missionEditHandler, JComponent parent)
    {
        this.missionEditHandler = missionEditHandler;
        this.parent = parent;
    }

    public Integer pickPlane(Integer crewMemberSerialNumber) throws PWCGException
    {       
        List<EquippedTank> squadronPlanes = missionEditHandler.getSortedUnassignedTanks();
        Object[] possibilities = new Object[squadronPlanes.size()];
        for (int i = 0; i < squadronPlanes.size(); ++i)
        {
            EquippedTank plane = squadronPlanes.get(i);
            PickerEntry entry = new PickerEntry();
            entry.description = plane.getDisplayName();
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
        public EquippedTank plane;

        public String toString()
        {
            return description;
        }
    }
}

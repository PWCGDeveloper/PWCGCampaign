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

    public String pickPlane(Integer pilotSerialNumber) throws PWCGException 
    {       
        List<EquippedPlane> squadronPlanes = missionEditHandler.getSortedUnassignedPlanes();
        Object[] possibilities = new Object[squadronPlanes.size()];
        for (int i = 0; i < squadronPlanes.size(); ++i)
        {
            EquippedPlane plane = squadronPlanes.get(i);
            String planeSelectionString = plane.getDisplayName() + " : " + plane.getSerialNumber();
            possibilities[i] = (Object)planeSelectionString;
        }
        
        String pickedPlane = (String)JOptionPane.showInputDialog(
                parent, 
                "Select Plane", 
                "Select Plane", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                possibilities, 
                "");
        
        return pickedPlane;
    }    
}

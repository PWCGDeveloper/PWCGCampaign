package pwcg.gui.rofmap.brief;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.briefing.BriefingMissionHandler;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingPlanePicker
{
    private BriefingMissionHandler missionEditHandler;
    private JComponent parent;
    
    public BriefingPlanePicker(BriefingMissionHandler missionEditHandler, JComponent parent)
    {
        this.missionEditHandler = missionEditHandler;
        this.parent = parent;
    }

    public String pickPlane(Integer pilotSerialNumber) throws PWCGException 
    {       
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        Squadron squad =  campaign.determineSquadron();

        List<PlaneType> aircraftTypes = getAvailableAircraftTypes(pilotSerialNumber, campaign, squad);
        Object[] possibilities = new String[aircraftTypes.size()];
        for (int i = 0; i < aircraftTypes.size(); ++i)
        {
            PlaneType plane = aircraftTypes.get(i);
            possibilities[i] = plane.getDisplayName();
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

    private List<PlaneType> getAvailableAircraftTypes(Integer pilotSerialNumber, Campaign campaign, Squadron squad) throws PWCGException
    {
        List<PlaneType> aircraftTypes;
        if (!allowAnyScout(pilotSerialNumber))
        {
            aircraftTypes = squad.determineCurrentAircraftList(campaign.getDate());
        }
        else
        {
            aircraftTypes = PWCGContextManager.getInstance().getPlaneTypeFactory().getAllFightersForCampaign(campaign);
            if (aircraftTypes == null || aircraftTypes.size() == 0)
            {
                aircraftTypes = squad.determineCurrentAircraftList(campaign.getDate());
            }
        }
        return aircraftTypes;
    }

    private boolean allowAnyScout(Integer pilotSerialNumber) throws PWCGException 
    {
        boolean allowAnyScout = false;
        CrewPlanePayloadPairing crewPlane = missionEditHandler.getPairingByPilot(pilotSerialNumber);
        if (crewPlane.getPilot().isPlayer())
        {
            Campaign campaign = PWCGContextManager.getInstance().getCampaign();
            if (campaign.isGreatAce())
            {
                allowAnyScout = true;
            }
        }
        
        return allowAnyScout;
    }
}

package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.core.exception.PWCGException;

public class CampaignReportEquipmentStatusGUI extends CampaignDocumentGUI
{
	private static final long serialVersionUID = 1L;
	private PlaneStatusEvent equipmentStatusEvent = null;
	
	public CampaignReportEquipmentStatusGUI(PlaneStatusEvent pilotLostEvent)
	{
		super();
        this.equipmentStatusEvent = pilotLostEvent;
        makePanel();        
	}

    protected String getHeaderText() throws PWCGException
    {
        String header = "";
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_DESTROYED)
        {
            header = "Notification of aircraft loss";
        }
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_DEPOT)
        {
            header = "Notification of aircraft resupply";
        }
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_REMOVED_FROM_SERVICE)
        {
            header = "Notification of aircraft removal from service";
        }
        
        return header;
    }

    protected String getBodyText() throws PWCGException
    {
        String planeEventText = "";
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_DESTROYED)
        {
            planeEventText = getPlaneLostText();
        }
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_DEPOT)
        {
            planeEventText = getPlaneAddedToDepotText();
        }
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_REMOVED_FROM_SERVICE)
        {
            planeEventText = getPlaneWithdrawnFromServiceText();
        }
        
        
        return planeEventText;
    }

    private String getPlaneLostText()
    {
        String planeEventText = 
                "A " + equipmentStatusEvent.getPlaneDesc() +
                ",  serial number " + equipmentStatusEvent.getPlaneSerialNumber() + 
                " has been lost in combat.\n";                

        return planeEventText;
    }

    private String getPlaneAddedToDepotText()
    {
        String planeEventText = 
                "A " + equipmentStatusEvent.getPlaneDesc() +
                ",  serial number " + equipmentStatusEvent.getPlaneSerialNumber() + 
                " has been provided to the depot for distribution to front line units.\n";                

        return planeEventText;
    }

    private String getPlaneWithdrawnFromServiceText()
    {
        String planeEventText = 
                "A " + equipmentStatusEvent.getPlaneDesc() +
                ",  serial number " + equipmentStatusEvent.getPlaneSerialNumber() + 
                " has been withdrawn from service.\n";                

        return planeEventText;
    }

    @Override
    public void finished()
    {
    }
}

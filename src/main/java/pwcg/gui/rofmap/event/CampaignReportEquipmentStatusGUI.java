package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.core.exception.PWCGException;

public class CampaignReportEquipmentStatusGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
    private PlaneStatusEvent equipmentStatusEvent;
    private Campaign campaign;
	
	public CampaignReportEquipmentStatusGUI(Campaign campaign, PlaneStatusEvent pilotLostEvent) throws PWCGException
	{
		super();
        this.campaign = campaign;
        this.equipmentStatusEvent = pilotLostEvent;
        makePanel();        
	}

    protected String getHeaderText() throws PWCGException
    {
        String header = "";
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_DESTROYED)
        {
            header = "Notification of Aircraft Loss";
        }
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_DEPOT)
        {
            header = "Notification of Aircraft Resupply";
        }
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_REMOVED_FROM_SERVICE)
        {
            header = "Notification of Aircraft Removal From Service";
        }
        
        return header;
    }

    protected String getBodyText() throws PWCGException
    {
        String planeEventText = "";
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_DESTROYED)
        {
            planeEventText = equipmentStatusEvent.getPlaneLostText(campaign);
        }
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_DEPOT)
        {
            planeEventText = equipmentStatusEvent.getPlaneAddedToDepotText(campaign);
        }
        if (equipmentStatusEvent.getPlaneStatus() == PlaneStatus.STATUS_REMOVED_FROM_SERVICE)
        {
            planeEventText = equipmentStatusEvent.getPlaneWithdrawnFromServiceText(campaign);
        }
        
        
        return planeEventText;
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    @Override
    public void finished()
    {
    }

    @Override
    protected String getFooterImagePath() throws PWCGException
    {
        return "";
    }
}

package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportPilotStatusGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
	private PilotStatusEvent pilotLostEvent = null;
	
	public CampaignReportPilotStatusGUI(PilotStatusEvent pilotLostEvent) throws PWCGException
	{
		super();
        this.pilotLostEvent = pilotLostEvent;
        makePanel();
	}

    protected String getHeaderText() throws PWCGException
    {
        String header = "";
        if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_KIA)
        {
            header = "Notification of Death of Airman";
        }
        if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_CAPTURED)
        {
            header = "Notification of Capture of Airman";
        }
        if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            header = "Notification of Incapacitation of Airman";
        }
        if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_WOUNDED)
        {
            header = "Notification of Injury to Airman";
        }
        
        return header;
    }

    protected String getBodyText() throws PWCGException
    {
        String pilotLostText = "Squadron: " + pilotLostEvent.getSquadronName() + "\n";
        pilotLostText += "Date: " + DateUtils.getDateStringPretty(pilotLostEvent.getDate()) + "\n";
        pilotLostText += getPilotLostString();   
        
        
        return pilotLostText;
    }

    private String getPilotLostString()
    {
        String pilotLostString = "";
        if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_KIA)
        {
            pilotLostString = "We regret to inform command that " + 
                pilotLostEvent.getPilotName() + 
                " has been killed in action.\n";                
        }

        else if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_CAPTURED)
        {
            pilotLostString = "We regret to inform command that " + 
                pilotLostEvent.getPilotName() + 
                " has been brought down behind enemy lines and is missing in action.\n";                
        }
        else if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            pilotLostString = "We regret to inform command that " + 
                pilotLostEvent.getPilotName() + 
                " has been seriously injured in combat with enemy forces.  He will be unavailable for an extended period of time";                
        }
        else if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_WOUNDED)
        {
            pilotLostString = "It is my duty to inform command that " + 
                pilotLostEvent.getPilotName() + 
                " has been injured in combat with enemy forces.  He will be unavailable for a period of some weeks";                
        }
        return pilotLostString;
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

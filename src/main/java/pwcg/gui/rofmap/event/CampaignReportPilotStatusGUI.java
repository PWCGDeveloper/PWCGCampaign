package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportPilotStatusGUI extends CampaignDocumentGUI
{
	private static final long serialVersionUID = 1L;
	private PilotStatusEvent pilotLostEvent = null;
	
	public CampaignReportPilotStatusGUI(PilotStatusEvent pilotLostEvent)
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
            header = "Notification of death of airman";
        }
        if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_CAPTURED)
        {
            header = "Notification of capture of airman";
        }
        if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            header = "Notification of incapacitation of airman";
        }
        if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_WOUNDED)
        {
            header = "Notification of injury to airman";
        }
        
        return header;
    }

    protected String getBodyText() throws PWCGException
    {
        String pilotLostText = "Squadron: " + pilotLostEvent.getSquadronName() + "\n";
        pilotLostText += "Date: " + DateUtils.getDateStringPretty(pilotLostEvent.getDate()) + "\n";
        pilotLostText += getPilotLostString() + ".\n";   
        
        
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
                " has been seriously injured in combat with enemy forces.\n  " +
                "He will be unavailable for an extended period of time";                
        }
        else if (pilotLostEvent.getStatus() == SquadronMemberStatus.STATUS_WOUNDED)
        {
            pilotLostString = "It is my duty to inform command that " + 
                pilotLostEvent.getPilotName() + 
                " has been injured in combat with enemy forces.\n  " +
                "He will be unavailable for a period of some weeks";                
        }
        return pilotLostString;
    }


    @Override
    public void finished()
    {
    }
}

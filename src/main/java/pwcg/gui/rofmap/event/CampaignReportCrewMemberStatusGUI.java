package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportCrewMemberStatusGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
	private CrewMemberStatusEvent crewMemberLostEvent = null;
	
	public CampaignReportCrewMemberStatusGUI(CrewMemberStatusEvent crewMemberLostEvent) throws PWCGException
	{
		super();
        this.crewMemberLostEvent = crewMemberLostEvent;
        makePanel();
	}

    protected String getHeaderText() throws PWCGException
    {
        String header = "";
        if (crewMemberLostEvent.getStatus() == CrewMemberStatus.STATUS_KIA)
        {
            header = "Notification of Death of Airman";
        }
        if (crewMemberLostEvent.getStatus() == CrewMemberStatus.STATUS_CAPTURED)
        {
            header = "Notification of Capture of Airman";
        }
        if (crewMemberLostEvent.getStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            header = "Notification of Incapacitation of Airman";
        }
        if (crewMemberLostEvent.getStatus() == CrewMemberStatus.STATUS_WOUNDED)
        {
            header = "Notification of Injury to Airman";
        }
        
        return header;
    }

    protected String getBodyText() throws PWCGException
    {
        String crewMemberLostText = "Squadron: " + crewMemberLostEvent.getSquadronName() + "\n";
        crewMemberLostText += "Date: " + DateUtils.getDateStringPretty(crewMemberLostEvent.getDate()) + "\n";
        crewMemberLostText += getCrewMemberLostString();   
        
        
        return crewMemberLostText;
    }

    private String getCrewMemberLostString()
    {
        String crewMemberLostString = "";
        if (crewMemberLostEvent.getStatus() == CrewMemberStatus.STATUS_KIA)
        {
            crewMemberLostString = "We regret to inform command that " + 
                crewMemberLostEvent.getCrewMemberName() + 
                " has been killed in action.\n";                
        }

        else if (crewMemberLostEvent.getStatus() == CrewMemberStatus.STATUS_CAPTURED)
        {
            crewMemberLostString = "We regret to inform command that " + 
                crewMemberLostEvent.getCrewMemberName() + 
                " has been brought down behind enemy lines and is missing in action.\n";                
        }
        else if (crewMemberLostEvent.getStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            crewMemberLostString = "We regret to inform command that " + 
                crewMemberLostEvent.getCrewMemberName() + 
                " has been seriously injured in combat with enemy forces.  He will be unavailable for an extended period of time";                
        }
        else if (crewMemberLostEvent.getStatus() == CrewMemberStatus.STATUS_WOUNDED)
        {
            crewMemberLostString = "It is my duty to inform command that " + 
                crewMemberLostEvent.getCrewMemberName() + 
                " has been injured in combat with enemy forces.  He will be unavailable for a period of some weeks";                
        }
        return crewMemberLostString;
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

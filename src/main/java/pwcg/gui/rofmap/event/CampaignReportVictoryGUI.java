package pwcg.gui.rofmap.event;

import java.util.List;

import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.VictoryDescription;
import pwcg.core.exception.PWCGException;

public class CampaignReportVictoryGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
	private Campaign campaign;
	private List<VictoryEvent> victoriesForCrewMember = null;
	
	public CampaignReportVictoryGUI(Campaign campaign, List<VictoryEvent> victoriesForCrewMember) throws PWCGException
	{
		super();
        this.campaign = campaign;
        this.victoriesForCrewMember = victoriesForCrewMember;
        makePanel();        
	}

    protected String getHeaderText() throws PWCGException
    {
        String header = "Notification of Victory for " + victoriesForCrewMember.get(0).getCrewMemberName();
        return header;
    }

    protected String getBodyText() throws PWCGException
    {
        String crewMemberVictoriesText = "";
        for (VictoryEvent victoryEvent : victoriesForCrewMember)
        {            
            VictoryDescription victoryDescription = new VictoryDescription(campaign, victoryEvent.getVictory());
            String victoryDescriptionText = victoryDescription.createVictoryDescription();
            crewMemberVictoriesText += victoryDescriptionText + "\n\n";   
        }

        return crewMemberVictoriesText;
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

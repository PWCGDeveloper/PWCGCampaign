package pwcg.gui.rofmap.event;

import java.util.List;

import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.VictoryDescription;
import pwcg.core.exception.PWCGException;

public class CampaignReportVictoryGUI extends CampaignDocumentGUI
{
	private static final long serialVersionUID = 1L;
	private Campaign campaign;
	private List<VictoryEvent> victoriesForPilot = null;
	
	public CampaignReportVictoryGUI(Campaign campaign, List<VictoryEvent> victoriesForPilot)
	{
		super();
        this.campaign = campaign;
        this.victoriesForPilot = victoriesForPilot;
        makePanel();        
	}

    protected String getHeaderText() throws PWCGException
    {
        String header = "Notification of Victory for " + victoriesForPilot.get(0).getPilotName();
        return header;
    }

    protected String getBodyText() throws PWCGException
    {
        String pilotVictoriesText = "";
        for (VictoryEvent victoryEvent : victoriesForPilot)
        {            
            VictoryDescription victoryDescription = new VictoryDescription(campaign, victoryEvent.getVictory());
            String victoryDescriptionText = victoryDescription.createVictoryDescription();
            pilotVictoriesText += victoryDescriptionText + "\n\n";   
        }

        return pilotVictoriesText;
    }

    @Override
    public void finished()
    {
    }
}

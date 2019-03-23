package pwcg.gui.campaign.intel;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignIntelligenceFriendlySquadronsGUI extends CampaignIntelligenceBase
{
	private static final long serialVersionUID = 1L;

	public CampaignIntelligenceFriendlySquadronsGUI() throws PWCGException  
	{
		super();
        
        intelHeaderBuffer.append("Intelligence Report on Friendly Air Units\n");
        intelHeaderBuffer.append("Date: " + DateUtils.getDateString(campaign.getDate()) + "\n");

        makePanel(referencePlayer.determineCountry(campaign.getDate()).getSide());
	}

}


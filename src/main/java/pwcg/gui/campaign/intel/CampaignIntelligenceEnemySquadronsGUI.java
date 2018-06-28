package pwcg.gui.campaign.intel;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignIntelligenceEnemySquadronsGUI extends CampaignIntelligenceBase
{
	private static final long serialVersionUID = 1L;

    public CampaignIntelligenceEnemySquadronsGUI(Campaign campaign) throws PWCGException  
    {
		super(campaign);
        
        intelHeaderBuffer.append("Intelligence Report on Enemy Air Units\n");
        intelHeaderBuffer.append("Date: " + DateUtils.getDateString(campaign.getDate()) + "\n");

        makePanel(campaign.determineCountry().getSide().getOppositeSide());
    }
}


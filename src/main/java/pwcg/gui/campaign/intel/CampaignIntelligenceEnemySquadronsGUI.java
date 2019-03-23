package pwcg.gui.campaign.intel;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignIntelligenceEnemySquadronsGUI extends CampaignIntelligenceBase
{
	private static final long serialVersionUID = 1L;

    public CampaignIntelligenceEnemySquadronsGUI() throws PWCGException  
    {
		super();
        
        intelHeaderBuffer.append("Intelligence Report on Enemy Air Units\n");
        intelHeaderBuffer.append("Date: " + DateUtils.getDateString(campaign.getDate()) + "\n");

        makePanel(referencePlayer.determineCountry(campaign.getDate()).getSide().getOppositeSide());
    }
}


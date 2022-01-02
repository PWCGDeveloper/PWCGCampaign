package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.PromotionEvent;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportPromotionGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
	private PromotionEvent promotionEvent = null;
    private CrewMember promotionRecipient;

	public CampaignReportPromotionGUI(Campaign campaign, PromotionEvent promotionEvent) throws PWCGException
	{
		super();
		this.promotionEvent = promotionEvent;
		this.promotionRecipient = campaign.getPersonnelManager().getAnyCampaignMember(promotionEvent.getCrewMemberSerialNumber());
		makePanel();		
	}

    protected String getHeaderText() throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        String promotionHeaderText = promotionRecipient.determineService(campaign.getDate()).getName() + "\n\n";

        promotionHeaderText += "To all who shall see these presents, greeting: \n\n";
        
        return promotionHeaderText;
    }

    protected String getBodyText() throws PWCGException
	{
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        
        String promotionText = "Know ye that reposing special trust and confidence in the fidelity and abilities of ";
        promotionText += "NAME I do hereby appoint him RANK of the SERVICE of NATION, ";
        promotionText += "to rank as such from DATE.  He is carefully and diligently to discharge the duty of ";
        promotionText += "RANK by doing and performing all manner of things thereunto belonging.  And I do ";
        promotionText += "strictly require all Soldiers under his command to be obedient to his orders as RANK ";
        promotionText += "as he shall receive from his Superior Officers set over him, according to the rules ";
        promotionText += "and discipline of war. ";

        promotionText += "\n\nGiven under the hand of OFFICER on DATE";
        
        promotionText = promotionText.replaceAll("NAME", promotionEvent.getOldRank() + " " + promotionRecipient.getName());
        promotionText = promotionText.replaceAll("RANK", promotionEvent.getNewRank());
        promotionText = promotionText.replaceAll("DATE", DateUtils.getDateStringPretty(campaign.getDate()));
        
        ArmedService service = promotionRecipient.determineService(campaign.getDate());
        
        promotionText = promotionText.replaceAll("SERVICE", service.getName());
        ICountry country = CountryFactory.makeCountryByCountry(promotionRecipient.getCountry());
        promotionText = promotionText.replaceAll("NATION", country.getCountryName());

        promotionText = promotionText.replaceAll("OFFICER", promotionRecipient.determineService(campaign.getDate()).getGeneralRankForService() + " " + promotionEvent.getPromotingGeneral());

        return promotionText;
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

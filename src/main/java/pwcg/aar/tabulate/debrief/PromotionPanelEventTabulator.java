package pwcg.aar.tabulate.debrief;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARPromotionPanelData;
import pwcg.aar.ui.events.PromotionEventGenerator;
import pwcg.aar.ui.events.model.PromotionEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class PromotionPanelEventTabulator
{
    private Campaign campaign;
    private AARContext aarContext;

    private AARPromotionPanelData promotionPanelData = new AARPromotionPanelData();

    public PromotionPanelEventTabulator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
        
    public AARPromotionPanelData tabulateForAARPromotionPanel() throws PWCGException
    {
        PromotionEventGenerator promotionEventGenerator = new PromotionEventGenerator(campaign);
        List<PromotionEvent> promotionEventsForCampaignMembers = promotionEventGenerator.createPilotPromotionEvents(aarContext.getReconciledOutOfMissionData().getPersonnelAwards().getPromotions());
        List<PromotionEvent> promotionEventsForSquadronMembers = getPromotionsForCurrentSquadron(promotionEventsForCampaignMembers);
        promotionPanelData.setPromotionEventsDuringElapsedTime(promotionEventsForSquadronMembers);
                
        return promotionPanelData;
    }
    
    private List<PromotionEvent> getPromotionsForCurrentSquadron(List<PromotionEvent> promotionEventsForCampaignMembers) throws PWCGException
    {
        List<PromotionEvent> promotionEventsForSquadronMembers = new ArrayList<>();
        
        for (PromotionEvent promotionEvent : promotionEventsForCampaignMembers)
        {
            if (promotionEvent.getSquadron().equals(campaign.determineSquadron().determineDisplayName(campaign.getDate())))
            {
                promotionEventsForSquadronMembers.add(promotionEvent);
            }
        }
        return promotionEventsForSquadronMembers;
    }

}

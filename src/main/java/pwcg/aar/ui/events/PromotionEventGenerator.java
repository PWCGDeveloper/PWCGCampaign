package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.PromotionEvent;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberNames;
import pwcg.core.exception.PWCGException;

public class PromotionEventGenerator
{
    private Campaign campaign;
    
    public PromotionEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<PromotionEvent> createCrewMemberPromotionEvents(Map<Integer, String> promotionByCrewMember) throws PWCGException
    {
        List<PromotionEvent> promotionEventsForCrewMembers = new ArrayList<>();
        for (Integer serialNumber : promotionByCrewMember.keySet())
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (crewMember != null)
            {
                String newRankForCrewMember = promotionByCrewMember.get(serialNumber);
                PromotionEvent promotionEvent = makePromotionEvent(crewMember, newRankForCrewMember);
                promotionEventsForCrewMembers.add(promotionEvent);
            }
        }
        
        return promotionEventsForCrewMembers;
    }

    private PromotionEvent makePromotionEvent(CrewMember crewMember, String newRank) throws PWCGException
    {
        boolean isNewsworthy = true;
        String oldRank = crewMember.getRank();
        Map<String, String> namesUsed = new HashMap<String, String>();
        
        ArmedService armedService = crewMember.determineService(campaign.getDate());
        String promotingGeneral = CrewMemberNames.getInstance().getName(armedService, namesUsed);
        PromotionEvent promotionEvent = new PromotionEvent(campaign, oldRank, newRank, promotingGeneral, crewMember.getCompanyId(), crewMember.getSerialNumber(), campaign.getDate(), isNewsworthy);
        return promotionEvent;
    }
}

package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.PromotionEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadmember.PilotNames;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class PromotionEventGenerator
{
    private Campaign campaign;
    
    public PromotionEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<PromotionEvent> createPilotPromotionEvents(Map<Integer, String> promotionByPilot) throws PWCGException
    {
        List<PromotionEvent> promotionEventsForSquadronMembers = new ArrayList<>();
        for (Integer serialNumber : promotionByPilot.keySet())
        {
            SquadronMember squadronMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (squadronMember != null)
            {
                String newRankForSquadronMember = promotionByPilot.get(serialNumber);
                PromotionEvent promotionEvent = makePromotionEvent(squadronMember, newRankForSquadronMember);
                promotionEventsForSquadronMembers.add(promotionEvent);
            }
        }
        
        return promotionEventsForSquadronMembers;
    }

    private PromotionEvent makePromotionEvent(SquadronMember pilot, String newRank) throws PWCGException
    {
        PromotionEvent promotionEvent = new PromotionEvent();
        promotionEvent.setPilot(pilot);
        Squadron squadron = pilot.determineSquadron();
        if (squadron != null)
        {
            promotionEvent.setSquadron(squadron.determineDisplayName(campaign.getDate()));
        }
        
        promotionEvent.setOldRank(pilot.getRank());
        promotionEvent.setNewRank(newRank);
        promotionEvent.setDate(campaign.getDate());

        Map<String, String> namesUsed = new HashMap<String, String>();
        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
        String promotingGeneral = PilotNames.getInstance().getName(country, namesUsed);
        promotionEvent.setPromotingGeneral(promotingGeneral);
        
        return promotionEvent;
    }
}

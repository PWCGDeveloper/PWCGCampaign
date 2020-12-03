package pwcg.aar.awards;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class PromotionEventHandlerBomb implements IPromotionEventHandler
{
    public static int PilotRankMedMinMissions = 10;       // Number of missions to advance pilot Rank from low to medium
    public static int PilotRankHighMinMissions = 20;      // Number of victories to advance pilot Rank from medium to high
    public static int PilotRankExecMinMissions = 30;      // Number of victories to advance pilot Rank from medium to exec
    public static int PilotRankCommandMinMissions = 40;   // Number of victories to advance pilot Rank from exec to command

    public String determinePromotion(Campaign campaign, SquadronMember pilot) throws PWCGException 
    {
        String promotion = PromotionEventHandler.NO_PROMOTION;

        ArmedService service = pilot.determineService(campaign.getDate());
        int numMissions = pilot.getMissionFlown();
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPosBeforePromotion = rankObj.getRankPosByService(pilot.getRank(), service);
        
        if (rankPosBeforePromotion > 3)
        {
            if (numMissions >= PilotRankMedMinMissions)
            {
                promotion = rankObj.getRankByService(3, service);
            }
        }
        else if (rankPosBeforePromotion == 3)
        {
            if (numMissions >= PilotRankHighMinMissions)
            {
                promotion = rankObj.getRankByService(2, service);
            }
        }
        else if (rankPosBeforePromotion == 2)
        {
            if (numMissions >= PilotRankExecMinMissions)
            {
                promotion = rankObj.getRankByService(1, service);
            }
        }
        else if (rankPosBeforePromotion == 1)
        {
            boolean squadronHasCommander = false;
            SquadronPersonnel playerPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(pilot.getSquadronId());
            SquadronMembers activePersonnel = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(playerPersonnel.getSquadronMembers().getSquadronMemberCollection(), campaign.getDate());
            for (SquadronMember squadronMember : activePersonnel.getSquadronMemberList())
            {
                if (squadronMember.determineIsSquadronMemberCommander())
                {
                    squadronHasCommander = true;
                }
            }

            if (pilot.isPlayer() || !squadronHasCommander)
            {
                if (numMissions >= PilotRankCommandMinMissions)
                {
                    promotion = rankObj.getRankByService(0, service);
                }
            }
        }

        return promotion;
    }

}

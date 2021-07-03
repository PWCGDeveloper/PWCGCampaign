package pwcg.campaign.promotion;

import pwcg.aar.outofmission.phase2.awards.IPromotionEventHandler;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberVictories;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class PromotionArbitrator implements IPromotionEventHandler
{
    public static String NO_PROMOTION = "No Promotion";
    PromotionMinimumCriteria promotionCriteria;
    
    public PromotionArbitrator(PromotionMinimumCriteria promotionCriteria)
    {
        this.promotionCriteria = promotionCriteria;
    }
    
    public String determinePromotion(Campaign campaign, SquadronMember pilot) throws PWCGException 
    {
        String promotion = NO_PROMOTION;
       
        int rankPosBeforePromotion = getRankPositionForPilot(campaign, pilot);
        if (rankPosBeforePromotion == 0)
        {
            return NO_PROMOTION;
        }
        
        int requiredMissionForNextRank = getRequiredMissionsForNextRank(rankPosBeforePromotion);
        int requiredVictoriesForNextRank = getRequiredVictoriesForNextRank(rankPosBeforePromotion);
        
        int numPilotMissions = pilot.getMissionFlown();
        int numPilotVictories = getPilotVictories(campaign, pilot);
        if (numPilotMissions >= requiredMissionForNextRank && numPilotVictories >= requiredVictoriesForNextRank)
        {
            ArmedService service = pilot.determineService(campaign.getDate());
            IRankHelper rankObj = RankFactory.createRankHelper();
            if (rankPosBeforePromotion > 1)
            {
                int newRankPosition = rankPosBeforePromotion - 1;
                promotion = rankObj.getRankByService(newRankPosition, service);
            } 
            else if (rankPosBeforePromotion == 1)
            {
                boolean squadronHasCommander = determineSquadronhasCommander(campaign, pilot);
                if (pilot.isPlayer() || !squadronHasCommander)
                {
                    promotion = rankObj.getRankByService(0, service);
                }
            }
        }
        
        return promotion;
    }

    private int getPilotVictories(Campaign campaign, SquadronMember pilot) throws PWCGException
    {
        Role role = pilot.determineSquadron().determineSquadronPrimaryRole(campaign.getDate());
        SquadronMemberVictories victories = pilot.getSquadronMemberVictories();
        if (role == Role.ROLE_FIGHTER)
        {
            return victories.getAirToAirVictoryCount();
        }
        else
        {
            return victories.getGroundVictoryPointTotal();
        }
    }

    private boolean determineSquadronhasCommander(Campaign campaign, SquadronMember pilot) throws PWCGException
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
        return squadronHasCommander;
    }

    private int getRankPositionForPilot(Campaign campaign, SquadronMember pilot) throws PWCGException
    {
        ArmedService service = pilot.determineService(campaign.getDate());
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPosBeforePromotion = rankObj.getRankPosByService(pilot.getRank(), service);
        return rankPosBeforePromotion;
    }

    private int getRequiredMissionsForNextRank(int rankPosBeforePromotion)
    {
        if (rankPosBeforePromotion > 3)
        {
            return promotionCriteria.getPilotRankMedMinMissions();
        }
        else if (rankPosBeforePromotion == 3)
        {
            return promotionCriteria.getPilotRankHighMinMissions();
        }
        else if (rankPosBeforePromotion == 2)
        {
            return promotionCriteria.getPilotRankExecMinMissions();
        }
        else if (rankPosBeforePromotion == 1)
        {
            return promotionCriteria.getPilotRankCommandMinMissions();
        }
        return 100000000;
    }

    private int getRequiredVictoriesForNextRank(int rankPosBeforePromotion)
    {
        if (rankPosBeforePromotion > 3)
        {
            return promotionCriteria.getPilotRankMedMinVictories();
        }
        else if (rankPosBeforePromotion == 3)
        {
            return promotionCriteria.getPilotRankHighMinVictories();
        }
        else if (rankPosBeforePromotion == 2)
        {
            return promotionCriteria.getPilotRankExecMinVictories();
        }
        else if (rankPosBeforePromotion == 1)
        {
            return promotionCriteria.getPilotRankCommandMinVictories();
        }
        return 100000000;
    }
}

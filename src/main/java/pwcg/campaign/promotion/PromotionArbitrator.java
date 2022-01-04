package pwcg.campaign.promotion;

import pwcg.aar.outofmission.phase2.awards.IPromotionEventHandler;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberVictories;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;

public class PromotionArbitrator implements IPromotionEventHandler
{
    public static String NO_PROMOTION = "No Promotion";
    PromotionMinimumCriteria promotionCriteria;
    
    public PromotionArbitrator(PromotionMinimumCriteria promotionCriteria)
    {
        this.promotionCriteria = promotionCriteria;
    }
    
    public String determinePromotion(Campaign campaign, CrewMember crewMember) throws PWCGException 
    {
        String promotion = NO_PROMOTION;
       
        int rankPosBeforePromotion = getRankPositionForCrewMember(campaign, crewMember);
        if (rankPosBeforePromotion == 0)
        {
            return NO_PROMOTION;
        }
        
        int requiredMissionForNextRank = getRequiredMissionsForNextRank(rankPosBeforePromotion);
        int requiredVictoriesForNextRank = getRequiredVictoriesForNextRank(rankPosBeforePromotion);
        
        int numCrewMemberMissions = crewMember.getBattlesFought();
        int numCrewMemberVictories = getCrewMemberVictories(campaign, crewMember);
        if (numCrewMemberMissions >= requiredMissionForNextRank && numCrewMemberVictories >= requiredVictoriesForNextRank)
        {
            ArmedService service = crewMember.determineService(campaign.getDate());
            IRankHelper rankObj = RankFactory.createRankHelper();
            if (rankPosBeforePromotion > 1)
            {
                int newRankPosition = rankPosBeforePromotion - 1;
                promotion = rankObj.getRankByService(newRankPosition, service);
            } 
            else if (rankPosBeforePromotion == 1)
            {
                boolean squadronHasCommander = determineSquadronhasCommander(campaign, crewMember);
                if (crewMember.isPlayer() || !squadronHasCommander)
                {
                    promotion = rankObj.getRankByService(0, service);
                }
            }
        }
        
        return promotion;
    }

    private int getCrewMemberVictories(Campaign campaign, CrewMember crewMember) throws PWCGException
    {
        PwcgRoleCategory roleCategory = crewMember.determineSquadron().determineSquadronPrimaryRoleCategory(campaign.getDate());
        CrewMemberVictories victories = crewMember.getCrewMemberVictories();
        if (roleCategory == PwcgRoleCategory.FIGHTER)
        {
            return victories.getAirToAirVictoryCount();
        }
        else
        {
            return victories.getGroundVictoryPointTotal();
        }
    }

    private boolean determineSquadronhasCommander(Campaign campaign, CrewMember referenceCrewMember) throws PWCGException
    {
        boolean squadronHasCommander = false;
        CompanyPersonnel playerPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(referenceCrewMember.getCompanyId());
        CrewMembers activePersonnel = CrewMemberFilter.filterActiveAIAndPlayerAndAces(playerPersonnel.getCrewMembers().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : activePersonnel.getCrewMemberList())
        {
            if (crewMember.determineIsCrewMemberCommander())
            {
                squadronHasCommander = true;
            }
        }
        return squadronHasCommander;
    }

    private int getRankPositionForCrewMember(Campaign campaign, CrewMember crewMember) throws PWCGException
    {
        ArmedService service = crewMember.determineService(campaign.getDate());
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPosBeforePromotion = rankObj.getRankPosByService(crewMember.getRank(), service);
        return rankPosBeforePromotion;
    }

    private int getRequiredMissionsForNextRank(int rankPosBeforePromotion)
    {
        if (rankPosBeforePromotion > 3)
        {
            return promotionCriteria.getCrewMemberRankMedMinMissions();
        }
        else if (rankPosBeforePromotion == 3)
        {
            return promotionCriteria.getCrewMemberRankHighMinMissions();
        }
        else if (rankPosBeforePromotion == 2)
        {
            return promotionCriteria.getCrewMemberRankExecMinMissions();
        }
        else if (rankPosBeforePromotion == 1)
        {
            return promotionCriteria.getCrewMemberRankCommandMinMissions();
        }
        return 100000000;
    }

    private int getRequiredVictoriesForNextRank(int rankPosBeforePromotion)
    {
        if (rankPosBeforePromotion > 3)
        {
            return promotionCriteria.getCrewMemberRankMedMinVictories();
        }
        else if (rankPosBeforePromotion == 3)
        {
            return promotionCriteria.getCrewMemberRankHighMinVictories();
        }
        else if (rankPosBeforePromotion == 2)
        {
            return promotionCriteria.getCrewMemberRankExecMinVictories();
        }
        else if (rankPosBeforePromotion == 1)
        {
            return promotionCriteria.getCrewMemberRankCommandMinVictories();
        }
        return 100000000;
    }
}

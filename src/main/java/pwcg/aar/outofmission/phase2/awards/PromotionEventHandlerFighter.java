package pwcg.aar.outofmission.phase2.awards;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public abstract class PromotionEventHandlerFighter implements IPromotionEventHandler
{
	protected int PilotRankMedVictories = 1;          // Number of victories to advance pilot Rank from low to medium
	protected int PilotRankHighMinVictories = 3;      // Number of victories to advance pilot Rank from medium to high
	protected int PilotRankExecVictories = 5;         // Number of victories to advance pilot Rank from high to exec
	protected int PilotRankCommandVictories = 10;     // Number of victories to advance pilot Rank from exec to command

	protected int PilotRankMedMinMissions = 10;       // Number of missions to advance pilot Rank from low to medium
	protected int PilotRankHighMinMissions = 25;      // Number of victories to advance pilot Rank from medium to high
	protected int PilotRankExecMinMissions = 40;      // Number of victories to advance pilot Rank from medium to exec
	protected int PilotRankCommandMinMissions = 60;   // Number of victories to advance pilot Rank from exec to command


    public String determinePromotion(Campaign campaign, SquadronMember pilot) throws PWCGException 
    {
        String promotion = PromotionEventHandler.NO_PROMOTION;
        
        ArmedService service = pilot.determineService(campaign.getDate());
        int numPilotVictories = pilot.getSquadronMemberVictories().getAirToAirVictoryCount();
        int numMissions = pilot.getMissionFlown();        
        IRankHelper rankHelper = RankFactory.createRankHelper();
        int rankPosBeforePromotion = rankHelper.getRankPosByService(pilot.getRank(), service);

        if (rankPosBeforePromotion > 3)
        {
            if (numPilotVictories >= PilotRankMedVictories && numMissions >= PilotRankMedMinMissions)
            {
                promotion = rankHelper.getRankByService(3, service);
            }
        }
        else if (rankPosBeforePromotion == 3)
        {
            if (numPilotVictories >= PilotRankHighMinVictories && numMissions >= PilotRankHighMinMissions)
            {
                promotion = rankHelper.getRankByService(2, service);
            }
        }
        else if (rankPosBeforePromotion == 2)
        {
            if (numPilotVictories >= PilotRankExecVictories && numMissions >= PilotRankExecMinMissions)
            {
                promotion = rankHelper.getRankByService(1, service);
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
                if (numPilotVictories >= PilotRankCommandVictories && numMissions >= PilotRankCommandMinMissions)
                {
                    promotion = rankHelper.getRankByService(0, service);
                }
            }
        }
        
        return promotion;
    }

}

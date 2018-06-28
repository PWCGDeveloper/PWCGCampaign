package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

public class SquadronMemberInitialVictoryBuilderTest
{
    private Campaign campaign;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE);
    }


    @Test
    public void testInitialVictoriesGermanFighter () throws PWCGException
    {
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(20112052);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        
        for (SquadronMember squadronMember : campaign.getPersonnelManager().getSquadronPersonnel(20112052).getActiveSquadronMembers().getSquadronMembers().values())
        {
            int rankPos = rankHelper.getRankPosByService(squadronMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 8, 20);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 3, 15);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 0, 8);
            }
            else
            {
                validateVictoryRange (squadronMember.getVictories().size(), 0, 0);
            }

        }
    }

    @Test
    public void testInitialVictoriesRussianFighter () throws PWCGException
    {
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(10111126);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        
        for (SquadronMember squadronMember : campaign.getPersonnelManager().getSquadronPersonnel(10111126).getActiveSquadronMembers().getSquadronMembers().values())
        {
            int rankPos = rankHelper.getRankPosByService(squadronMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 0, 6);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 0, 1);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 0, 0);
            }
            else
            {
                validateVictoryRange (squadronMember.getVictories().size(), 0, 0);
            }
        }
    }

    private void validateVictoryRange(int numVictories, int min, int max)
    {
        assert(numVictories >= min);
        assert(numVictories <= max);
    }

}

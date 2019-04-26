package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

public class SquadronMemberInitialVictoryBuilderTest
{
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
    }


    @Test
    public void testInitialVictoriesGermanFighter () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);

        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(20112052);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        
        for (int squadronId : campaign.getPersonnelManager().getCampaignPersonnel().keySet())
        {
            SquadronPersonnel personnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);
            System.out.println("Squadron id: " + squadronId + "     Squadron: " + personnel.getSquadron().getFileName());
            if (squadronId == 20112052)
            {
                System.out.println("Squadron id: " + squadronId + "     Squadron: " + personnel.getSquadron().getFileName());
            }
        }
        
        SquadronPersonnel jg52Personnel = campaign.getPersonnelManager().getSquadronPersonnel(20112052);

        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(jg52Personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
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
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);

        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(10111126);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(campaign.getPersonnelManager().getSquadronPersonnel(10111126).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
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

    @Test
    public void testInitialVictoriesGermanFighterWest () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW_WEST);

        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(20112052);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        SquadronPersonnel jg52Personnel = campaign.getPersonnelManager().getSquadronPersonnel(20112052);

        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(jg52Personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(squadronMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 8, 20);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 1, 15);
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
    public void testInitialVictoriesAmericanFighterWest () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheBoS.FG_362_PROFILE);

        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(102362377);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        SquadronPersonnel fg362Personnel = campaign.getPersonnelManager().getSquadronPersonnel(102362377);

        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(fg362Personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(squadronMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 8, 17);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 0, 10);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (squadronMember.getVictories().size(), 0, 6);
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

package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class SquadronMemberInitialVictoryBuilderTest
{
    Campaign campaign;
    
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }


    @Test
    public void testInitialVictoriesGermanFighter () throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(20112052);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        SquadronPersonnel jg52Personnel = campaign.getPersonnelManager().getSquadronPersonnel(20112052);

        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(jg52Personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(squadronMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (squadronMember, 6, 30);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (squadronMember, 3, 20);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (squadronMember, 0, 8);
            }
            else
            {
                validateVictoryRange (squadronMember, 0, 0);
            }

        }
    }

    @Test
    public void testInitialVictoriesRussianFighter () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(10111126);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(campaign.getPersonnelManager().
                        getSquadronPersonnel(10111126).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(squadronMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (squadronMember, 0, 6);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (squadronMember, 0, 1);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (squadronMember, 0, 0);
            }
            else
            {
                validateVictoryRange (squadronMember, 0, 0);
            }
        }
    }

    @Test
    public void testInitialVictoriesGermanFighterWest () throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(20112052);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        SquadronPersonnel jg52Personnel = campaign.getPersonnelManager().getSquadronPersonnel(20112052);

        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(jg52Personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(squadronMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (squadronMember, 6, 30);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (squadronMember, 1, 20);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (squadronMember, 0, 8);
            }
            else
            {
                validateVictoryRange (squadronMember, 0, 0);
            }

        }
    }

    @Test
    public void testInitialVictoriesAmericanFighterWest () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(102362377);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        SquadronPersonnel fg362Personnel = campaign.getPersonnelManager().getSquadronPersonnel(102362377);

        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(fg362Personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(squadronMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (squadronMember, 4, 17);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (squadronMember, 1, 10);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (squadronMember, 0, 7);
            }
            else
            {
                validateVictoryRange (squadronMember, 0, 0);
            }

        }
    }

    private void validateVictoryRange(SquadronMember squadronMember, int min, int max) throws PWCGException
    {
        if (squadronMember instanceof Ace)
        {
            return;
        }
        
        int numVictories = squadronMember.getSquadronMemberVictories().getAirToAirVictoryCount();
        if (numVictories < min || numVictories > max)
        {
            System.out.println("Victoris not in range : " + numVictories + "     Min: " + min + "     Max: " + max);
        }
        assert(numVictories >= min);
        assert(numVictories <= max);
    }

}

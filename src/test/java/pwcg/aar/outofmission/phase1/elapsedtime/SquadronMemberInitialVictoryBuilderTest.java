package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SquadronMemberInitialVictoryBuilderTest
{
    private Campaign germanCampaign;
    private Campaign americanCampaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        germanCampaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        americanCampaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.FG_362_PROFILE);
    }


    @Test
    public void testInitialVictoriesGermanFighter () throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(20112052);
        ArmedService service = squadron.determineServiceForSquadron(germanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        SquadronPersonnel jg52Personnel = germanCampaign.getPersonnelManager().getSquadronPersonnel(20112052);

        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(jg52Personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), germanCampaign.getDate());
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
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(10111126);
        ArmedService service = squadron.determineServiceForSquadron(germanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(germanCampaign.getPersonnelManager().
                        getSquadronPersonnel(10111126).getSquadronMembersWithAces().getSquadronMemberCollection(), germanCampaign.getDate());
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
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(20112052);
        ArmedService service = squadron.determineServiceForSquadron(germanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        SquadronPersonnel jg52Personnel = germanCampaign.getPersonnelManager().getSquadronPersonnel(20112052);

        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(jg52Personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), germanCampaign.getDate());
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

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(102362377);
        ArmedService service = squadron.determineServiceForSquadron(americanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        SquadronPersonnel fg362Personnel = americanCampaign.getPersonnelManager().getSquadronPersonnel(102362377);

        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(fg362Personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), americanCampaign.getDate());
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

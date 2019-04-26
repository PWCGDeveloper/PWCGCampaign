package pwcg.aar.campaign.update;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.ww1.country.RoFServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

public class CampaignServiceChangeHandlerTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

    @Test
    public void testRafTransition() throws PWCGException 
    {
        Campaign campaign = CampaignCache.makeCampaignForceCreation(SquadrontTestProfile.RFC_2_PROFILE);
        ArmedService service = campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate());
        ICountry country = service.getCountry();
        SquadronPersonnel personnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadrontTestProfile.RFC_2_PROFILE.getSquadronId());

        assertTrue (country.getCountry() == Country.BRITAIN);
        assertTrue (service.getName().equals(RoFServiceManager.RFC_NAME));

        SquadronMembers rfcSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : rfcSquadronMembers.getSquadronMemberList())
        {
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(squadronMember.getRank(), service);
            assert(rankPos >= 0);
        }

        CampaignServiceChangeHandler serviceChangeHandler = new CampaignServiceChangeHandler(campaign);
        serviceChangeHandler.handleChangeOfService(DateUtils.getRAFDate());
                
        service = campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(DateUtils.getRAFDate());
        assertTrue (service.getCountry().getCountry() == Country.BRITAIN);
        assertTrue (service.getName().equals(RoFServiceManager.RAF_NAME));

        SquadronMembers rafSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : rafSquadronMembers.getSquadronMemberList())
        {
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(squadronMember.getRank(), service);
            assert(rankPos >= 0);
        }
    }
    
    
    @Test
    public void testLafayetteEscTransition() throws PWCGException 
    {
        Campaign campaign = CampaignCache.makeCampaignForceCreation(SquadrontTestProfile.ESC_124_PROFILE);
        ArmedService service = campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate());
        SquadronPersonnel personnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadrontTestProfile.ESC_124_PROFILE.getSquadronId());

        assertTrue (service.getCountry().getCountry() == Country.FRANCE);
        assertTrue (service.getName().equals(RoFServiceManager.LAVIATION_MILITAIRE_NAME));

        SquadronMembers lafayetteSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayer(personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : lafayetteSquadronMembers.getSquadronMemberList())
        {
            assertTrue (squadronMember.getCountry() == Country.FRANCE);
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(squadronMember.getRank(), service);
            assert(rankPos >= 0);
        }

        Date lafEscTransitionDate = DateUtils.getDateYYYYMMDD("19180219");
        
        CampaignServiceChangeHandler serviceChangeHandler = new CampaignServiceChangeHandler(campaign);
        serviceChangeHandler.handleChangeOfService(lafEscTransitionDate);                
        service = campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(lafEscTransitionDate);
        assertTrue (service.getName().equals(RoFServiceManager.USAS_NAME));

        SquadronMembers usasSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayer(personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : usasSquadronMembers.getSquadronMemberList())
        {
            assertTrue (squadronMember.getCountry() == Country.USA);
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(squadronMember.getRank(), service);
            assert(rankPos >= 0);
        }
    }
}

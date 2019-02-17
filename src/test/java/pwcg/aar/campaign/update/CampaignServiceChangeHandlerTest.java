package pwcg.aar.campaign.update;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pwcg.aar.campaign.update.CampaignServiceChangeHandler;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
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
import pwcg.testutils.CampaignCacheRoF;

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
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.RFC_2_PROFILE);

        ArmedService service = campaign.determineSquadron().determineServiceForSquadron(campaign.getDate());
        assertTrue (campaign.determineCountry().getCountry() == Country.BRITAIN);
        assertTrue (service.getName().equals(RoFServiceManager.RFC_NAME));

        SquadronPersonnel rfcPersonnel = campaign.getPersonnelManager().getPlayerPersonnel();
        SquadronMembers rfcSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(rfcPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : rfcSquadronMembers.getSquadronMemberList())
        {
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(squadronMember.getRank(), service);
            assert(rankPos >= 0);
        }

        CampaignServiceChangeHandler serviceChangeHandler = new CampaignServiceChangeHandler(campaign);
        serviceChangeHandler.handleChangeOfService(DateUtils.getRAFDate());
                
        service = campaign.determineSquadron().determineServiceForSquadron(DateUtils.getRAFDate());
        assertTrue (campaign.determineCountry().getCountry() == Country.BRITAIN);
        assertTrue (service.getName().equals(RoFServiceManager.RAF_NAME));

        SquadronPersonnel rafPersonnel = campaign.getPersonnelManager().getPlayerPersonnel();
        SquadronMembers rafSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(rafPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
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
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_124_PROFILE);

        ArmedService service = campaign.determineSquadron().determineServiceForSquadron(campaign.getDate());
        assertTrue (campaign.determineCountry().getCountry() == Country.FRANCE);
        assertTrue (service.getName().equals(RoFServiceManager.LAVIATION_MILITAIRE_NAME));

        SquadronPersonnel lafayettePersonnel = campaign.getPersonnelManager().getPlayerPersonnel();
        SquadronMembers lafayetteSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayer(lafayettePersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
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
        service = campaign.determineSquadron().determineServiceForSquadron(lafEscTransitionDate);
        assertTrue (service.getName().equals(RoFServiceManager.USAS_NAME));

        SquadronPersonnel usasPersonnel = campaign.getPersonnelManager().getPlayerPersonnel();
        SquadronMembers usasSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayer(usasPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : usasSquadronMembers.getSquadronMemberList())
        {
            assertTrue (squadronMember.getCountry() == Country.USA);
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(squadronMember.getRank(), service);
            assert(rankPos >= 0);
        }
    }
}

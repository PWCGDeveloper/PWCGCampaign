package pwcg.aar.campaign.update;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import pwcg.aar.campaign.update.CampaignServiceChangeHandler;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.ww1.country.RoFServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;

public class CampaignServiceChangeHandlerTest
{
    @Test
    public void testRafTransition() throws PWCGException 
    {
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.RFC_2_PROFILE);

        ArmedService service = campaign.determineSquadron().determineServiceForSquadron(campaign.getDate());
        assertTrue (campaign.determineCountry().getCountry() == Country.BRITAIN);
        assertTrue (service.getName().equals(RoFServiceManager.RFC_NAME));

        SquadronPersonnel rfcPersonnel = campaign.getPersonnelManager().getPlayerPersonnel();
        for (SquadronMember squadronMember : rfcPersonnel.getActiveSquadronMembers().getSquadronMembers().values())
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
        for (SquadronMember squadronMember : rafPersonnel.getActiveSquadronMembers().getSquadronMembers().values())
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
        for (SquadronMember squadronMember : lafayettePersonnel.getActiveSquadronMembers().getSquadronMembers().values())
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
        for (SquadronMember squadronMember : usasPersonnel.getActiveSquadronMembers().getSquadronMembers().values())
        {
            assertTrue (squadronMember.getCountry() == Country.USA);
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(squadronMember.getRank(), service);
            assert(rankPos >= 0);
        }
    }
}

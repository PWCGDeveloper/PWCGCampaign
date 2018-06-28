package pwcg.campaign.io.json;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.ww1.country.RoFServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.CampaignRemover;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.CampaignCacheRoF;

@RunWith(MockitoJUnitRunner.class)
public class CampaignIOJsonTest
{    
    @Test
    public void campaignIOJsonTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);

        deleteCampaign();
        writeCampaign();
        readCampaign();
        deleteCampaign();
    }

    private void writeCampaign() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.JASTA_11_PROFILE);
        CampaignIOJson.writeJson(campaign);
    }

    private void readCampaign() throws PWCGException
    {
        Campaign campaign = new Campaign();
        campaign.open(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        PWCGContextManager.getInstance().setCampaign(campaign);

        assert (campaign.getPlayer().getSerialNumber() == SerialNumber.PLAYER_SERIAL_NUMBER);
        assert (campaign.getDate().equals(DateUtils.getDateYYYYMMDD("19170501")));
        assert (campaign.getSquadronId() == 501011);
        assert (campaign.getName().equals(CampaignCacheBase.TEST_CAMPAIGN_NAME));
        
        Map<Integer, SquadronMember> fighterSquadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(501011).getActiveSquadronMembersWithAces().getSquadronMembers();
        assert (campaign.getSerialNumber().getNextSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        assert (fighterSquadronPersonnel.size() >= 12);
        for (SquadronMember squadronMember : fighterSquadronPersonnel.values())
        {
            if (squadronMember.isPlayer())
            {
                assert (squadronMember.getSerialNumber() >= SerialNumber.PLAYER_SERIAL_NUMBER);
                assert (squadronMember.getMissionFlown() == 0);
            }
            else if (squadronMember instanceof Ace)
            {
                assert (squadronMember.getSerialNumber() >= SerialNumber.ACE_STARTING_SERIAL_NUMBER);
                assert (squadronMember.getMissionFlown() > 0);
            }
            else
            {
                assert (squadronMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
                assert (squadronMember.getMissionFlown() > 0);
            }
        }
        
        
        Map<Integer, SquadronMember> reconSquadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(101002).getActiveSquadronMembers().getSquadronMembers();
        assert (reconSquadronPersonnel.size() == 12);
        for (SquadronMember squadronMember : reconSquadronPersonnel.values())
        {
            assert (squadronMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
            assert (squadronMember.getMissionFlown() > 0);
        }
        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService germanArmedService = armedServiceManager.getArmedServiceByName(RoFServiceManager.DEUTSCHE_LUFTSTREITKRAFTE_NAME, campaign.getDate());
        PersonnelReplacementsService germanReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(germanArmedService.getServiceId());
        assert(germanReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(germanReplacements.getDailyReplacementRate() == 10);
        assert(germanReplacements.getLastReplacementDate().equals(campaign.getDate()));

        ArmedService belgianArmedService = armedServiceManager.getArmedServiceByName(RoFServiceManager.AVIATION_MILITAIRE_BELGE_NAME, campaign.getDate());
        PersonnelReplacementsService belgianReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(belgianArmedService.getServiceId());
        assert(belgianReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(belgianReplacements.getDailyReplacementRate() == 1);
    }


    private void deleteCampaign()
    {
        CampaignRemover campaignRemover = new CampaignRemover();
        campaignRemover.deleteCampaign(CampaignCacheBase.TEST_CAMPAIGN_NAME);
    }
}

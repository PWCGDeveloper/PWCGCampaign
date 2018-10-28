package pwcg.campaign.io.json;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
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
        Campaign campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheRoF.JASTA_11_PROFILE);
        CampaignIOJson.writeJson(campaign);
    }

    private void readCampaign() throws PWCGException
    {
        Campaign campaign = new Campaign();
        campaign.open(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        PWCGContextManager.getInstance().setCampaign(campaign);

        validateCoreCampaign(campaign);        
        validateFighterSquadronMembers(campaign);        
        validateReconSquadronMembers(campaign);        
    	validatePersonnelReplacements(campaign);
    	validateFighterEquipment(campaign);
    	validateReconEquipment(campaign);
    }

    private void validateCoreCampaign(Campaign campaign) throws PWCGException
    {
        List<SquadronMember> players = campaign.getPlayers();
        for (SquadronMember player : players)
        {
            assert (player.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER && player.getSerialNumber() < SerialNumber.AI_STARTING_SERIAL_NUMBER);
        }
        
        assert (campaign.getDate().equals(DateUtils.getDateYYYYMMDD("19170501")));
        assert (campaign.getSquadronId() == 501011);
        assert (campaign.getCampaignData().getName().equals(CampaignCacheBase.TEST_CAMPAIGN_NAME));
        assert (campaign.getPlayers().get(0).getName().equals(CampaignCacheBase.TEST_PLAYER_NAME));
    }

    private void validatePersonnelReplacements(Campaign campaign) throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService germanArmedService = armedServiceManager.getArmedServiceByName(RoFServiceManager.DEUTSCHE_LUFTSTREITKRAFTE_NAME, campaign.getDate());
        PersonnelReplacementsService germanReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(germanArmedService.getServiceId());
        assert(germanReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(germanReplacements.getDailyReplacementRate() == 15);
        assert(germanReplacements.getLastReplacementDate().equals(campaign.getDate()));

        ArmedService belgianArmedService = armedServiceManager.getArmedServiceByName(RoFServiceManager.AVIATION_MILITAIRE_BELGE_NAME, campaign.getDate());
        PersonnelReplacementsService belgianReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(belgianArmedService.getServiceId());
        assert(belgianReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(belgianReplacements.getDailyReplacementRate() == 1);
    }

    private void validateReconSquadronMembers(Campaign campaign) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(101002);
        SquadronMembers reconSquadronPersonnel = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        assert (reconSquadronPersonnel.getSquadronMemberList().size() == 12);
        for (SquadronMember squadronMember : reconSquadronPersonnel.getSquadronMemberList())
        {
            assert (squadronMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
            assert (squadronMember.getMissionFlown() > 0);
        }
    }

    private void validateFighterSquadronMembers(Campaign campaign) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(501011);
        SquadronMembers fighterSquadronPersonnel = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        assert (campaign.getSerialNumber().getNextPilotSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        assert (fighterSquadronPersonnel.getSquadronMemberList().size() >= 12);
        for (SquadronMember squadronMember : fighterSquadronPersonnel.getSquadronMemberList())
        {
            if (squadronMember.isPlayer())
            {
                assert (squadronMember.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
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
    }

    private void validateFighterEquipment(Campaign campaign) throws PWCGException
    {
        Equipment fighterSquadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(501011);
        assert (campaign.getSerialNumber().getNextPlaneSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 100);
        assert (fighterSquadronEquipment.getActiveEquippedPlanes().size() >= 14);
        for (EquippedPlane equippedPlane : fighterSquadronEquipment.getActiveEquippedPlanes().values())
        {
            assert (equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
            assert (equippedPlane.getArchType().equals("albatrosd"));
        }
    }

    private void validateReconEquipment(Campaign campaign) throws PWCGException
    {
        Equipment reconSquadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(101002);
        assert (campaign.getSerialNumber().getNextPlaneSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 100);
        assert (reconSquadronEquipment.getActiveEquippedPlanes().size() >= 14);
        for (EquippedPlane equippedPlane : reconSquadronEquipment.getActiveEquippedPlanes().values())
        {
            assert (equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
            assert (equippedPlane.getArchType().equals("re8") || equippedPlane.getArchType().equals("sopstr"));
        }
    }

    private void deleteCampaign()
    {
        CampaignRemover campaignRemover = new CampaignRemover();
        campaignRemover.deleteCampaign(CampaignCacheBase.TEST_CAMPAIGN_NAME);
    }
}

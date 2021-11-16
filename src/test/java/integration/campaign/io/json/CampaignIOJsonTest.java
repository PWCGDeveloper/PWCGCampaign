package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.io.json.CampaignIOJson;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.CampaignRemover;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.country.FCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class CampaignIOJsonTest
{    
    @Test
    public void campaignIOJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);

        deleteCampaign();
        writeCampaign();
        readCampaign();
        deleteCampaign();
    }

    private void writeCampaign() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
        CampaignIOJson.writeJson(campaign);
    }

    private void readCampaign() throws PWCGException
    {
        Campaign campaign = new Campaign();
        campaign.open(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        PWCGContext.getInstance().setCampaign(campaign);

        validateCoreCampaign(campaign);        
        validateFighterSquadronMembers(campaign);        
        validateReconSquadronMembers(campaign);        
    	validatePersonnelReplacements(campaign);
    	validateFighterEquipment(campaign);
    	validateReconEquipment(campaign);
    }

    private void validateCoreCampaign(Campaign campaign) throws PWCGException
    {
    	SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (SquadronMember player : players.getSquadronMemberList())
        {
            Assertions.assertTrue (player.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER && player.getSerialNumber() < SerialNumber.AI_STARTING_SERIAL_NUMBER);
        }
        
        Assertions.assertTrue (campaign.getDate().equals(DateUtils.getDateYYYYMMDD(SquadronTestProfile.JASTA_11_PROFILE.getDateString())));
        Assertions.assertTrue (campaign.getCampaignData().getName().equals(CampaignCacheBase.TEST_CAMPAIGN_NAME));
        SquadronMember player = campaign.findReferencePlayer();
        Assertions.assertTrue (player.getName().equals(CampaignCacheBase.TEST_PLAYER_NAME));
    }

    private void validatePersonnelReplacements(Campaign campaign) throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService germanArmedService = armedServiceManager.getArmedServiceByName(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE_NAME, campaign.getDate());
        PersonnelReplacementsService germanReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(germanArmedService.getServiceId());
        assert(germanReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(germanReplacements.getDailyReplacementRate() == 22);
        assert(germanReplacements.getLastReplacementDate().equals(campaign.getDate()));

        ArmedService belgianArmedService = armedServiceManager.getArmedServiceByName(FCServiceManager.AVIATION_MILITAIRE_BELGE_NAME, campaign.getDate());
        PersonnelReplacementsService belgianReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(belgianArmedService.getServiceId());
        assert(belgianReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(belgianReplacements.getDailyReplacementRate() == 3);
    }

    private void validateReconSquadronMembers(Campaign campaign) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.RFC_2_PROFILE.getSquadronId());
        SquadronMembers reconSquadronPersonnel = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (reconSquadronPersonnel.getSquadronMemberList().size() == 12);
        for (SquadronMember squadronMember : reconSquadronPersonnel.getSquadronMemberList())
        {
            Assertions.assertTrue (squadronMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
            Assertions.assertTrue (squadronMember.getMissionFlown() > 0);
        }
    }

    private void validateFighterSquadronMembers(Campaign campaign) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        SquadronMembers fighterSquadronPersonnel = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (campaign.getSerialNumber().getNextPilotSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (fighterSquadronPersonnel.getSquadronMemberList().size() >= 12);
        for (SquadronMember squadronMember : fighterSquadronPersonnel.getSquadronMemberList())
        {
            if (squadronMember.isPlayer())
            {
                Assertions.assertTrue (squadronMember.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
                Assertions.assertTrue (squadronMember.getMissionFlown() == 0);
            }
            else if (squadronMember instanceof Ace)
            {
                Assertions.assertTrue (squadronMember.getSerialNumber() >= SerialNumber.ACE_STARTING_SERIAL_NUMBER);
                Assertions.assertTrue (squadronMember.getMissionFlown() > 0);
            }
            else
            {
                Assertions.assertTrue (squadronMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
                Assertions.assertTrue (squadronMember.getMissionFlown() > 0);
            }
        }
    }

    private void validateFighterEquipment(Campaign campaign) throws PWCGException
    {
        Equipment fighterSquadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        Assertions.assertTrue (campaign.getSerialNumber().getNextPlaneSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (fighterSquadronEquipment.getActiveEquippedPlanes().size() >= 14);
        for (EquippedPlane equippedPlane : fighterSquadronEquipment.getActiveEquippedPlanes().values())
        {
            Assertions.assertTrue (equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
            Assertions.assertTrue (equippedPlane.getArchType().equals("albatrosd"));
        }
    }

    private void validateReconEquipment(Campaign campaign) throws PWCGException
    {
        Equipment reconSquadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(SquadronTestProfile.RFC_2_PROFILE.getSquadronId());
        Assertions.assertTrue (campaign.getSerialNumber().getNextPlaneSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (reconSquadronEquipment.getActiveEquippedPlanes().size() >= 14);
        for (EquippedPlane equippedPlane : reconSquadronEquipment.getActiveEquippedPlanes().values())
        {
            Assertions.assertTrue (equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
            Assertions.assertTrue (equippedPlane.getArchType().contains("aircodh4"));
        }
    }

    private void deleteCampaign()
    {
        CampaignRemover.deleteCampaign(CampaignCacheBase.TEST_CAMPAIGN_NAME);
    }
}

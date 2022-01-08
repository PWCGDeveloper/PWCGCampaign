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
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.io.json.CampaignIOJson;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
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
        PWCGContext.setProduct(PWCGProduct.BOS);

        deleteCampaign();
        writeCampaign();
        readCampaign();
        deleteCampaign();
    }

    private void writeCampaign() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaignOnDisk(SquadronTestProfile.JASTA_11_PROFILE);
        CampaignIOJson.writeJson(campaign);
    }

    private void readCampaign() throws PWCGException
    {
        Campaign campaign = new Campaign();
        campaign.open(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        PWCGContext.getInstance().setCampaign(campaign);

        validateCoreCampaign(campaign);        
        validateFighterCrewMembers(campaign);        
        validateReconCrewMembers(campaign);        
    	validatePersonnelReplacements(campaign);
    	validateFighterEquipment(campaign);
    	validateReconEquipment(campaign);
    }

    private void validateCoreCampaign(Campaign campaign) throws PWCGException
    {
    	CrewMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (CrewMember player : players.getCrewMemberList())
        {
            Assertions.assertTrue (player.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER && player.getSerialNumber() < SerialNumber.AI_STARTING_SERIAL_NUMBER);
        }
        
        Assertions.assertTrue (campaign.getDate().equals(DateUtils.getDateYYYYMMDD(SquadronTestProfile.JASTA_11_PROFILE.getDateString())));
        Assertions.assertTrue (campaign.getCampaignData().getName().equals(CampaignCacheBase.TEST_CAMPAIGN_NAME));
        CrewMember player = campaign.findReferencePlayer();
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

    private void validateReconCrewMembers(Campaign campaign) throws PWCGException
    {
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.RFC_2_PROFILE.getCompanyId());
        CrewMembers reconSquadronPersonnel = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (reconSquadronPersonnel.getCrewMemberList().size() == 12);
        for (CrewMember crewMember : reconSquadronPersonnel.getCrewMemberList())
        {
            Assertions.assertTrue (crewMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
            Assertions.assertTrue (crewMember.getBattlesFought() > 0);
        }
    }

    private void validateFighterCrewMembers(Campaign campaign) throws PWCGException
    {
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.JASTA_11_PROFILE.getCompanyId());
        CrewMembers fighterSquadronPersonnel = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (campaign.getSerialNumber().getNextCrewMemberSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (fighterSquadronPersonnel.getCrewMemberList().size() >= 12);
        for (CrewMember crewMember : fighterSquadronPersonnel.getCrewMemberList())
        {
            if (crewMember.isPlayer())
            {
                Assertions.assertTrue (crewMember.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
                Assertions.assertTrue (crewMember.getBattlesFought() == 0);
            }
            else if (crewMember instanceof TankAce)
            {
                Assertions.assertTrue (crewMember.getSerialNumber() >= SerialNumber.ACE_STARTING_SERIAL_NUMBER);
                Assertions.assertTrue (crewMember.getBattlesFought() > 0);
            }
            else
            {
                Assertions.assertTrue (crewMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
                Assertions.assertTrue (crewMember.getBattlesFought() > 0);
            }
        }
    }

    private void validateFighterEquipment(Campaign campaign) throws PWCGException
    {
        Equipment fighterSquadronEquipment = campaign.getEquipmentManager().getEquipmentForCompany(SquadronTestProfile.JASTA_11_PROFILE.getCompanyId());
        Assertions.assertTrue (campaign.getSerialNumber().getNextPlaneSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (fighterSquadronEquipment.getActiveEquippedTanks().size() >= 14);
        for (EquippedTank equippedPlane : fighterSquadronEquipment.getActiveEquippedTanks().values())
        {
            Assertions.assertTrue (equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
            Assertions.assertTrue (equippedPlane.getArchType().equals("albatrosd"));
        }
    }

    private void validateReconEquipment(Campaign campaign) throws PWCGException
    {
        Equipment reconSquadronEquipment = campaign.getEquipmentManager().getEquipmentForCompany(SquadronTestProfile.RFC_2_PROFILE.getCompanyId());
        Assertions.assertTrue (campaign.getSerialNumber().getNextPlaneSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (reconSquadronEquipment.getActiveEquippedTanks().size() >= 14);
        for (EquippedTank equippedPlane : reconSquadronEquipment.getActiveEquippedTanks().values())
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

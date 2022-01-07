package pwcg.campaign.plane;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.resupply.InitialCompanyEquipper;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class InitialSquadronEquipperTest
{
    public InitialSquadronEquipperTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);      
    }

    @Test
    public void testEquipSquadronGermanFighter() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);        
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper squadronEquipper = new InitialCompanyEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("bf109"));
            assert(equippedPlane.getType().equals("bf109f2") || equippedPlane.getType().equals("bf109f4"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipSquadronGermanBomber() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);        
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.KG53_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper squadronEquipper = new InitialCompanyEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("he111"));
            assert(equippedPlane.getType().equals("he111h6") || equippedPlane.getType().equals("he111h16"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipSquadronGermanDiveBomber() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);        
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.STG77_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper squadronEquipper = new InitialCompanyEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("ju87"));
            assert(equippedPlane.getType().equals("ju87d3"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipSquadronGermanTransport() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.TG2_PROFILE);        
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.TG2_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper squadronEquipper = new InitialCompanyEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("ju52"));
            assert(equippedPlane.getType().equals("ju523mg4e"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipSquadronRussianAttack() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_503_PROFILE);        
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.REGIMENT_503_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper squadronEquipper = new InitialCompanyEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("il2"));
            assert(equippedPlane.getType().equals("il2m41") || equippedPlane.getType().equals("il2m42"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }
}

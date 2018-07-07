package pwcg.campaign.plane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

@RunWith(MockitoJUnitRunner.class)
public class InitialSquadronEquipperTest
{
    @Before 
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);      
    }

    @Test
    public void testEquipSquadronGermanFighter() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.JG_51_PROFILE);        
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign);
        InitialSquadronEquipper squadronEquipper = new InitialSquadronEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedPlanes().size() == Squadron.SQUADRON_EQUIPMENT_SIZE);
        for (EquippedPlane equippedPlane : equipment.getActiveEquippedPlanes().values())
        {
            assert(equippedPlane.getArchType().equals("bf109"));
            assert(equippedPlane.getType().equals("bf109f2") || equippedPlane.getType().equals("bf109f4"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipSquadronGermanBomber() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.KG53_PROFILE);        
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign);
        InitialSquadronEquipper squadronEquipper = new InitialSquadronEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedPlanes().size() == Squadron.SQUADRON_EQUIPMENT_SIZE);
        for (EquippedPlane equippedPlane : equipment.getActiveEquippedPlanes().values())
        {
            assert(equippedPlane.getArchType().equals("he111"));
            assert(equippedPlane.getType().equals("he111h6") || equippedPlane.getType().equals("he111h16"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipSquadronGermanDiveBomber() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.STG77_PROFILE);        
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign);
        InitialSquadronEquipper squadronEquipper = new InitialSquadronEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedPlanes().size() == Squadron.SQUADRON_EQUIPMENT_SIZE);
        for (EquippedPlane equippedPlane : equipment.getActiveEquippedPlanes().values())
        {
            assert(equippedPlane.getArchType().equals("ju87"));
            assert(equippedPlane.getType().equals("ju87d3"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipSquadronGermanTransport() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.TG2_PROFILE);        
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign);
        InitialSquadronEquipper squadronEquipper = new InitialSquadronEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedPlanes().size() == Squadron.SQUADRON_EQUIPMENT_SIZE);
        for (EquippedPlane equippedPlane : equipment.getActiveEquippedPlanes().values())
        {
            assert(equippedPlane.getArchType().equals("ju52"));
            assert(equippedPlane.getType().equals("ju523mg4e"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipSquadronRussianAttack() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.REG_503_PROFILE);        
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign);
        InitialSquadronEquipper squadronEquipper = new InitialSquadronEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment equipment = squadronEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedPlanes().size() == Squadron.SQUADRON_EQUIPMENT_SIZE);
        for (EquippedPlane equippedPlane : equipment.getActiveEquippedPlanes().values())
        {
            assert(equippedPlane.getArchType().equals("il2"));
            assert(equippedPlane.getType().equals("il2m41") || equippedPlane.getType().equals("il2m42"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }
}

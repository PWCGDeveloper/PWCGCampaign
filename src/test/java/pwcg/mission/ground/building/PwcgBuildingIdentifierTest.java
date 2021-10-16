package pwcg.mission.ground.building;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PwcgBuildingIdentifierTest
{
    @Test
    public void unknownBuildingTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("hoteldeville[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.UNKNOWN);
        Assertions.assertTrue (building.getDescription().equals("some random building"));
    }
    
    @Test
    public void genericIndustrialTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("industrial_foo[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.INDUSTRIAL);
        Assertions.assertTrue (building.getDescription().equals("industrial facility"));
    }
    
    @Test
    public void depotIndustrialTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("industrial_block_smallwarehouse2_300x100[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.DEPOT);
        Assertions.assertTrue (building.getDescription().equals("depot"));
    }
    
    @Test
    public void fuelDepotIndustrialTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("industrial_block_fuel[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.FUEL);
        Assertions.assertTrue (building.getDescription().equals("fuel depot"));
    }
    
    @Test
    public void churchTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("church_eu_mid2[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.CHURCH);
        Assertions.assertTrue (building.getDescription().equals("church"));
    }
    
    @Test
    public void bridge1Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("br_rw_ramph[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.BRIDGE);
        Assertions.assertTrue (building.getDescription().equals("bridge"));
    }
    
    @Test
    public void bridge2Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("bridge_hw150[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.BRIDGE);
        Assertions.assertTrue (building.getDescription().equals("bridge"));
    }
    
    @Test
    public void railway1Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("kuban_railroadstation00[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.RAILWAY);
        Assertions.assertTrue (building.getDescription().equals("railway facility"));
    }
    
    @Test
    public void railway2Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("rwstation04[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.RAILWAY);
        Assertions.assertTrue (building.getDescription().equals("railway facility"));
    }
    
    @Test
    public void railway3Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("rw[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.RAILWAY);
        Assertions.assertTrue (building.getDescription().equals("railway facility"));
    }
    
    @Test
    public void hangarTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("hangardoubleside[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.HANGAR);
        Assertions.assertTrue (building.getDescription().equals("hangar"));
    }
    
    @Test
    public void airfieldFacilityTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("arf_ammo_3[4601,9]");
        Assertions.assertTrue (building == PwcgStructure.AIRFIELD_ARF);
        Assertions.assertTrue (building.getDescription().equals("airfield facility"));
    }
    
    @Test
    public void portTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("port_up_group_smallwarehouse[-1,-1]");
        Assertions.assertTrue (building == PwcgStructure.PORT_FACILITY);
        Assertions.assertTrue (building.getDescription().equals("port facility"));
    }
    
    @Test
    public void model1Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("graphics\\bridges\\br_rw_ramphi_dn.mgm");
        Assertions.assertTrue (building == PwcgStructure.BRIDGE);
    }
    
    @Test
    public void model2Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("graphics\\buildings\\rwstation04.mgm");
        Assertions.assertTrue (building == PwcgStructure.RAILWAY);
    }
}

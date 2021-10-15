package pwcg.mission.ground.building;

import org.junit.jupiter.api.Test;

public class PwcgBuildingIdentifierTest
{
    @Test
    public void unknownBuildingTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("hoteldeville[-1,-1]");
        assert (building == PwcgStructure.UNKNOWN);
        assert (building.getDescription().equals("some random building"));
    }
    
    @Test
    public void genericIndustrialTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("industrial_foo[-1,-1]");
        assert (building == PwcgStructure.INDUSTRIAL);
        assert (building.getDescription().equals("industrial facility"));
    }
    
    @Test
    public void depotIndustrialTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("industrial_block_smallwarehouse2_300x100[-1,-1]");
        assert (building == PwcgStructure.DEPOT);
        assert (building.getDescription().equals("depot"));
    }
    
    @Test
    public void fuelDepotIndustrialTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("industrial_block_fuel[-1,-1]");
        assert (building == PwcgStructure.FUEL);
        assert (building.getDescription().equals("fuel depot"));
    }
    
    @Test
    public void churchTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("church_eu_mid2[-1,-1]");
        assert (building == PwcgStructure.CHURCH);
        assert (building.getDescription().equals("church"));
    }
    
    @Test
    public void bridge1Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("br_rw_ramph[-1,-1]");
        assert (building == PwcgStructure.BRIDGE);
        assert (building.getDescription().equals("bridge"));
    }
    
    @Test
    public void bridge2Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("bridge_hw150[-1,-1]");
        assert (building == PwcgStructure.BRIDGE);
        assert (building.getDescription().equals("bridge"));
    }
    
    @Test
    public void railway1Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("kuban_railroadstation00[-1,-1]");
        assert (building == PwcgStructure.RAILWAY);
        assert (building.getDescription().equals("railway facility"));
    }
    
    @Test
    public void railway2Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("rwstation04[-1,-1]");
        assert (building == PwcgStructure.RAILWAY);
        assert (building.getDescription().equals("railway facility"));
    }
    
    @Test
    public void railway3Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("rw[-1,-1]");
        assert (building == PwcgStructure.RAILWAY);
        assert (building.getDescription().equals("railway facility"));
    }
    
    @Test
    public void hangarTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("hangardoubleside[-1,-1]");
        assert (building == PwcgStructure.HANGAR);
        assert (building.getDescription().equals("hangar"));
    }
    
    @Test
    public void airfieldFacilityTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("arf_ammo_3[4601,9]");
        assert (building == PwcgStructure.AIRFIELD_ARF);
        assert (building.getDescription().equals("airfield facility"));
    }
    
    @Test
    public void portTest()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("port_up_group_smallwarehouse[-1,-1]");
        assert (building == PwcgStructure.PORT_FACILITY);
        assert (building.getDescription().equals("port facility"));
    }
    
    @Test
    public void model1Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("graphics\\bridges\\br_rw_ramphi_dn.mgm");
        assert (building == PwcgStructure.BRIDGE);
    }
    
    @Test
    public void model2Test()
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding("graphics\\buildings\\rwstation04.mgm");
        assert (building == PwcgStructure.RAILWAY);
    }
}

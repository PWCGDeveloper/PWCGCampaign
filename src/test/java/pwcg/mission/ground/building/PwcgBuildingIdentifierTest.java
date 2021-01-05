package pwcg.mission.ground.building;

import org.junit.Test;

public class PwcgBuildingIdentifierTest
{
    @Test
    public void unknownBuildingTest()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("hoteldeville[-1,-1]");
        assert (building == PwcgBuilding.UNKNOWN);
        assert (building.getDescription().equals("some random building"));
    }
    
    @Test
    public void genericIndustrialTest()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("industrial_foo[-1,-1]");
        assert (building == PwcgBuilding.INDUSTRIAL);
        assert (building.getDescription().equals("industrial facility"));
    }
    
    @Test
    public void depotIndustrialTest()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("industrial_block_smallwarehouse2_300x100[-1,-1]");
        assert (building == PwcgBuilding.DEPOT);
        assert (building.getDescription().equals("depot facility"));
    }
    
    @Test
    public void fuelDepotIndustrialTest()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("industrial_block_fuel[-1,-1]");
        assert (building == PwcgBuilding.FUEL);
        assert (building.getDescription().equals("fuel depot facility"));
    }
    
    @Test
    public void churchTest()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("church_eu_mid2[-1,-1]");
        assert (building == PwcgBuilding.CHURCH);
        assert (building.getDescription().equals("church"));
    }
    
    @Test
    public void bridge1Test()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("br_rw_ramph[-1,-1]");
        assert (building == PwcgBuilding.BRIDGE);
        assert (building.getDescription().equals("bridge"));
    }
    
    @Test
    public void bridge2Test()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("bridge_hw150[-1,-1]");
        assert (building == PwcgBuilding.BRIDGE);
        assert (building.getDescription().equals("bridge"));
    }
    
    @Test
    public void railway1Test()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("kuban_railroadstation00[-1,-1]");
        assert (building == PwcgBuilding.RAILWAY);
        assert (building.getDescription().equals("railway facility"));
    }
    
    @Test
    public void railway2Test()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("rwstation04[-1,-1]");
        assert (building == PwcgBuilding.RAILWAY);
        assert (building.getDescription().equals("railway facility"));
    }
    
    @Test
    public void railway3Test()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("rw[-1,-1]");
        assert (building == PwcgBuilding.RAILWAY);
        assert (building.getDescription().equals("railway facility"));
    }
    
    @Test
    public void hangarTest()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("hangardoubleside[-1,-1]");
        assert (building == PwcgBuilding.HANGAR);
        assert (building.getDescription().equals("hangar"));
    }
    
    @Test
    public void portTest()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("port_up_group_smallwarehouse[-1,-1]");
        assert (building == PwcgBuilding.PORT_FACILITY);
        assert (building.getDescription().equals("port facility"));
    }
    
    @Test
    public void model1Test()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("graphics\\bridges\\br_rw_ramphi_dn.mgm");
        assert (building == PwcgBuilding.BRIDGE);
    }
    
    @Test
    public void model2Test()
    {
        PwcgBuilding building = PwcgBuildingIdentifier.identifyBuilding("graphics\\buildings\\rwstation04.mgm");
        assert (building == PwcgBuilding.RAILWAY);
    }
}

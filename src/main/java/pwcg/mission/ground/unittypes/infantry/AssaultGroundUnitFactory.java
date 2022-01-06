package pwcg.mission.ground.unittypes.infantry;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.artillery.GroundAAArtilleryBattery;
import pwcg.mission.ground.unittypes.artillery.GroundAAMachineGunBattery;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryBattery;

public class AssaultGroundUnitFactory
{    
    public IGroundUnit createAntiTankGunUnit (GroundUnitInformation groundUnitInformation) throws PWCGException
    {        
        IGroundUnit atDefenseUnit = new GroundAntiTankArtillery(groundUnitInformation);
        atDefenseUnit.createGroundUnit();
        return atDefenseUnit;
    }

    public IGroundUnit createAssaultTankUnit (GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        IGroundUnit tankAssaultUnit = new GroundAssaultTankUnit(groundUnitInformation);
        tankAssaultUnit.createGroundUnit();
        return tankAssaultUnit;
    }

    public IGroundUnit createMachineGunUnit (GroundUnitInformation groundUnitInformation) throws PWCGException 
    {
        IGroundUnit mgUnit = new GroundMachineGunUnit(groundUnitInformation);
        mgUnit.createGroundUnit();
        return mgUnit;
    }

    public IGroundUnit createAssaultArtilleryUnit (GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        IGroundUnit artilleryUnit = new GroundArtilleryBattery(groundUnitInformation);
        artilleryUnit.createGroundUnit();
        return artilleryUnit;
    }

    public IGroundUnit createAAMachineGunUnitUnit (GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        IGroundUnit aaMgBattery = new GroundAAMachineGunBattery(groundUnitInformation);
        aaMgBattery.createGroundUnit();
        return aaMgBattery;
    }

    public IGroundUnit createAAArtilleryUnitUnit (GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        IGroundUnit aaaBattery = new GroundAAArtilleryBattery(groundUnitInformation);
        aaaBattery.createGroundUnit();
        return aaaBattery;
    }


}

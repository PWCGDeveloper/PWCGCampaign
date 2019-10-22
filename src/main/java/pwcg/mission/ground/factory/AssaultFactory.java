package pwcg.mission.ground.factory;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundAntiTankArtillery;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultTankUnit;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunFlareUnit;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunUnit;

public class AssaultFactory
{    
    public AssaultFactory ()
    {
    }

    public GroundUnit createAntiTankGunUnit (GroundUnitInformation groundUnitInformation) throws PWCGException
    {        
        GroundAntiTankArtillery atDefenseUnit = new GroundAntiTankArtillery(groundUnitInformation);
        atDefenseUnit.createUnitMission();
        return atDefenseUnit;
    }

    public GroundUnit createAssaultTankUnit (GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        GroundAssaultTankUnit tankAssaultUnit = new GroundAssaultTankUnit(groundUnitInformation);
        tankAssaultUnit.createUnitMission();
        return tankAssaultUnit;
    }

    public GroundUnit createMachineGunUnit (GroundUnitInformation groundUnitInformation) throws PWCGException 
    {
        GroundMachineGunUnit mgUnit = new GroundMachineGunUnit(groundUnitInformation);
        mgUnit.createUnitMission();
        return mgUnit;
    }

    public GroundUnit createMachineGunFlareUnit (GroundUnitInformation groundUnitInformation) throws PWCGException 
    {
        GroundMachineGunFlareUnit pillBoxUnit = new GroundMachineGunFlareUnit(groundUnitInformation);
        pillBoxUnit.createUnitMission();
        return pillBoxUnit;
    }
    

    public GroundUnit createAssaultArtilleryUnit (GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        GroundArtilleryUnit artilleryUnit = new GroundArtilleryUnit(groundUnitInformation);
        artilleryUnit.createUnitMission();

        return artilleryUnit;
    }

}

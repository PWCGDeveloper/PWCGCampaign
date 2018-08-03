package pwcg.mission.ground.factory;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundAntiTankArtillery;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultInfantryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultTankUnit;
import pwcg.mission.ground.unittypes.infantry.GroundInfantryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunUnit;
import pwcg.mission.ground.unittypes.infantry.GroundPillBoxFlareUnit;
import pwcg.mission.ground.unittypes.infantry.GroundPillBoxUnit;

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

    public GroundUnit createStandingInfantryUnit (GroundUnitInformation groundUnitInformation) throws PWCGException 
    {
        GroundInfantryUnit infantryAssaultUnit = new GroundInfantryUnit(groundUnitInformation);
        infantryAssaultUnit.createUnitMission();
        return infantryAssaultUnit;
    }

    public GroundUnit createAssaultInfantryUnit (GroundUnitInformation groundUnitInformation) throws PWCGException 
    {
        GroundAssaultInfantryUnit infantryAssaultUnit = new GroundAssaultInfantryUnit(groundUnitInformation);
        infantryAssaultUnit.createUnitMission();
        return infantryAssaultUnit;
    }

    public GroundUnit createMachineGunUnit (GroundUnitInformation groundUnitInformation) throws PWCGException 
    {
        GroundMachineGunUnit mgUnit = new GroundMachineGunUnit(groundUnitInformation);
        mgUnit.createUnitMission();
        return mgUnit;
    }

    public GroundUnit createPillBoxUnit (GroundUnitInformation groundUnitInformation) throws PWCGException 
    {
        GroundPillBoxUnit pillBoxUnit = new GroundPillBoxUnit(groundUnitInformation);
        pillBoxUnit.createUnitMission();
        return pillBoxUnit;
    }

    public GroundUnit createPillBoxFlareUnit (GroundUnitInformation groundUnitInformation) throws PWCGException 
    {
        GroundPillBoxFlareUnit pillBoxUnit = new GroundPillBoxFlareUnit(groundUnitInformation);
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

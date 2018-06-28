package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.infantry.GroundATArtillery;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultInfantryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultTankUnit;
import pwcg.mission.ground.unittypes.infantry.GroundInfantryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunUnit;
import pwcg.mission.ground.unittypes.infantry.GroundPillBoxFlareUnit;
import pwcg.mission.ground.unittypes.infantry.GroundPillBoxUnit;

public class GroundUnitAssaultFactory
{    
    private Campaign campaign;
    
    public GroundUnitAssaultFactory (Campaign campaign)
    {
        this.campaign  = campaign;
    }

    public GroundUnit createDefenseUnit (
                    MissionBeginUnit missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException
    {
        GroundATArtillery atDefenseUnit = new GroundATArtillery(campaign);
        atDefenseUnit.initialize(missionBeginUnit, startCoords, destinationCoords, country);
        atDefenseUnit.createUnitMission();

        return atDefenseUnit;
    }

    public GroundUnit createAssaultTankUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry iCountry, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException
    {
        GroundAssaultTankUnit tankAssaultUnit = new GroundAssaultTankUnit(campaign);
        tankAssaultUnit.initialize(missionBeginUnit, startCoords, destinationCoords, iCountry);
        tankAssaultUnit.createUnitMission();

        return tankAssaultUnit;
    }

    public GroundUnit createStandingInfantryUnit (
                    MissionBeginUnit missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException 
    {
        
        GroundInfantryUnit infantryAssaultUnit = new GroundInfantryUnit(TacticalTarget.TARGET_ASSAULT);
        infantryAssaultUnit.initialize(missionBeginUnit, startCoords, destinationCoords, country);
        infantryAssaultUnit.createUnitMission();

        return infantryAssaultUnit;
    }

    public GroundUnit createAssaultInfantryUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException 
    {
        GroundAssaultInfantryUnit infantryAssaultUnit = new GroundAssaultInfantryUnit();
        infantryAssaultUnit.initialize(missionBeginUnit, startCoords, destinationCoords, country);
        infantryAssaultUnit.createUnitMission();

        return infantryAssaultUnit;
    }

    public GroundUnit createMachineGunUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException 
    {
        GroundMachineGunUnit mgUnit = new GroundMachineGunUnit(campaign);
        mgUnit.initialize(missionBeginUnit, startCoords, destinationCoords, country);
        mgUnit.createUnitMission();

        return mgUnit;
    }

    public GroundUnit createPillBoxUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate targetCoords) throws PWCGException 
    {
        GroundPillBoxUnit pillBoxUnit = new GroundPillBoxUnit();
        pillBoxUnit.initialize(missionBeginUnit, startCoords, targetCoords, country);
        pillBoxUnit.createUnitMission();

        return pillBoxUnit;
    }

    public GroundUnit createPillBoxFlareUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException 
    {
        GroundPillBoxFlareUnit pillBoxUnit = new GroundPillBoxFlareUnit();
        pillBoxUnit.initialize(missionBeginUnit, startCoords, destinationCoords, country);
        pillBoxUnit.createUnitMission();

        return pillBoxUnit;
    }
}

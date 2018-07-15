package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.AssaultGenerator.BattleSize;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.infantry.GroundATArtillery;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultInfantryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultTankUnit;
import pwcg.mission.ground.unittypes.infantry.GroundInfantryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunUnit;
import pwcg.mission.ground.unittypes.infantry.GroundPillBoxFlareUnit;
import pwcg.mission.ground.unittypes.infantry.GroundPillBoxUnit;

public class AssaultFactory
{    
    private Campaign campaign;
    private BattleSize battleSize;
    
    public AssaultFactory (Campaign campaign, BattleSize battleSize)
    {
        this.campaign  = campaign;
        this.battleSize  = battleSize;
    }

    public GroundUnit createDefenseUnit (
                    MissionBeginUnit missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException
    {        
        String nationality = country.getNationality();
        String name = nationality + " AT Gun";
        
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_INFANTRY, startCoords, destinationCoords);
        
        GroundATArtillery atDefenseUnit = new GroundATArtillery(campaign,groundUnitInformation);
        atDefenseUnit.createUnitMission();

        return atDefenseUnit;
    }

    public GroundUnit createAssaultTankUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException
    {
        String name = "Tank";

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_INFANTRY, startCoords, destinationCoords);

        GroundAssaultTankUnit tankAssaultUnit = new GroundAssaultTankUnit(campaign, groundUnitInformation, battleSize);
        tankAssaultUnit.createUnitMission();

        return tankAssaultUnit;
    }

    public GroundUnit createStandingInfantryUnit (
                    MissionBeginUnit missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException 
    {
        String nationality = country.getNationality();
        String name = nationality + " Infantry";

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_ASSAULT, startCoords, destinationCoords);

        GroundInfantryUnit infantryAssaultUnit = new GroundInfantryUnit(groundUnitInformation);
        infantryAssaultUnit.createUnitMission();

        return infantryAssaultUnit;
    }

    public GroundUnit createAssaultInfantryUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException 
    {
        String name = "Infantry";

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_INFANTRY, startCoords, destinationCoords);

        GroundAssaultInfantryUnit infantryAssaultUnit = new GroundAssaultInfantryUnit(groundUnitInformation, battleSize);
        infantryAssaultUnit.createUnitMission();

        return infantryAssaultUnit;
    }

    public GroundUnit createMachineGunUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException 
    {
        String nationality = country.getNationality();
        String name = nationality + " Machine Gun";

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_INFANTRY, startCoords, destinationCoords);

        GroundMachineGunUnit mgUnit = new GroundMachineGunUnit(campaign, groundUnitInformation);
        mgUnit.createUnitMission();

        return mgUnit;
    }

    public GroundUnit createPillBoxUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate targetCoords) throws PWCGException 
    {
        String nationality = country.getNationality();
        String name = nationality + " Pillbox";

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_DEFENSE, startCoords, targetCoords);

        GroundPillBoxUnit pillBoxUnit = new GroundPillBoxUnit(groundUnitInformation);
        pillBoxUnit.createUnitMission();

        return pillBoxUnit;
    }

    public GroundUnit createPillBoxFlareUnit (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException 
    {
        String nationality = country.getNationality();
        String name = nationality + " Pillbox";
        
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_DEFENSE, startCoords, destinationCoords);

        GroundPillBoxFlareUnit pillBoxUnit = new GroundPillBoxFlareUnit(groundUnitInformation);
        pillBoxUnit.createUnitMission();

        return pillBoxUnit;
    }
}

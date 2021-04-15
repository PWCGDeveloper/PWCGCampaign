package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShipDefinition;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.infantry.AssaultGroundUnitFactory;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class AmphibiousDefenseBuilder
{
    private Mission mission;
    private AmphibiousAssault amphibiousAssault;
    private AmphibiousAssaultShipDefinition landingCraft;
    private AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();
    private GroundUnitCollection amphibiousAssaultDefense;
    private AmphibiousPositionBuilder amphibiousPositionBuilder;
    
    public AmphibiousDefenseBuilder(Mission mission, AmphibiousAssault amphibiousAssault, AmphibiousAssaultShipDefinition landingCraft)
    {
        this.mission = mission;
        this.amphibiousAssault = amphibiousAssault;
        this.landingCraft = landingCraft;

        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Amphibious Defense", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitions());

        amphibiousAssaultDefense = new GroundUnitCollection ("Amphibious Assault Defense", groundUnitCollectionData);
    }

    public GroundUnitCollection generateAmphibiousAssaultDefense() throws PWCGException
    {
        buildPositionAndOrientation();

        defendingMachineGun();
        defendingATGuns();
        defendingArtillery();
        defendingAAAMachineGun();
        finishGroundUnitCollection();
        
        return amphibiousAssaultDefense;        
    }

    private void finishGroundUnitCollection() throws PWCGException
    {
        List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();
        primaryAssaultSegmentGroundUnits.add(amphibiousAssaultDefense.getPrimaryGroundUnit());
        amphibiousAssaultDefense.setPrimaryGroundUnit(primaryAssaultSegmentGroundUnits.get(0));
        amphibiousAssaultDefense.finishGroundUnitCollection();
    }

    private void buildPositionAndOrientation() throws PWCGException
    {
        amphibiousPositionBuilder = new AmphibiousPositionBuilder(landingCraft);
        amphibiousPositionBuilder.buildPositionAndOrientation();
    }

    private void defendingMachineGun() throws PWCGException
    { 
        Coordinate machineGunStartPosition = MathUtils.calcNextCoord(amphibiousPositionBuilder.getDefensePosition(), amphibiousPositionBuilder.getAssaultOrientation().getyOri(), 10);  

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(machineGunStartPosition, "Machine Gun", TargetType.TARGET_INFANTRY);
        AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();
        IGroundUnit defenseMachineGunUnit = assaultFactory.createMachineGunUnit (groundUnitInformation);
        amphibiousAssaultDefense.addGroundUnit(defenseMachineGunUnit);
    }

    private void defendingATGuns() throws PWCGException
    { 
        Coordinate antiTankDefensePosition = MathUtils.calcNextCoord(amphibiousPositionBuilder.getDefensePosition(), amphibiousPositionBuilder.getAssaultOrientation().getyOri(), 20);
        double toTheRight = MathUtils.adjustAngle( amphibiousPositionBuilder.getAssaultOrientation().getyOri(), 90);
        antiTankDefensePosition = MathUtils.calcNextCoord(antiTankDefensePosition.copy(), toTheRight, 75);  

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(antiTankDefensePosition, "Anti Tank Gun", TargetType.TARGET_INFANTRY);
        IGroundUnit defenseAntiTankUnit = assaultFactory.createAntiTankGunUnit (groundUnitInformation);
        amphibiousAssaultDefense.addGroundUnit(defenseAntiTankUnit);
    }

    private void defendingArtillery() throws PWCGException
    { 
        Coordinate artilleryDefensePosition = MathUtils.calcNextCoord(amphibiousPositionBuilder.getDefensePosition(), amphibiousPositionBuilder.getAssaultOrientation().getyOri(), 1000);  

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(artilleryDefensePosition, "Artillery", TargetType.TARGET_ARTILLERY);
        IGroundUnit defenseArtilleryUnit = assaultFactory.createAssaultArtilleryUnit (groundUnitInformation);
        amphibiousAssaultDefense.addGroundUnit(defenseArtilleryUnit);
    }
    
    private void defendingAAAMachineGun() throws PWCGException
    {
        Coordinate aaaMgDefensePosition = MathUtils.calcNextCoord(amphibiousPositionBuilder.getDefensePosition(), amphibiousPositionBuilder.getAssaultOrientation().getyOri(), 30);  
        double toTheRight = MathUtils.adjustAngle( amphibiousPositionBuilder.getAssaultOrientation().getyOri(), 90);
        aaaMgDefensePosition = MathUtils.calcNextCoord(aaaMgDefensePosition.copy(), toTheRight, 50);  

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(aaaMgDefensePosition, "Machine Gun AA", TargetType.TARGET_INFANTRY);
        IGroundUnit defenseAAMachineGunUnit = assaultFactory.createAAMachineGunUnitUnit(groundUnitInformation);
        amphibiousAssaultDefense.addGroundUnit(defenseAAMachineGunUnit);
    }

    private GroundUnitInformation buildDefenseGroundUnitInformation(Coordinate unitPosition, String unitName,TargetType targetType) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                CountryFactory.makeCountryByCountry(amphibiousAssault.getDefendingCountry()),
                targetType, 
                unitPosition, 
                unitPosition, 
                amphibiousPositionBuilder.getDefenseOrientation());
        
        groundUnitInformation.setUnitSize(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        
        return groundUnitInformation;
    }
 }

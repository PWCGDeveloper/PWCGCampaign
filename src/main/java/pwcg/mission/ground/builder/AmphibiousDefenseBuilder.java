package pwcg.mission.ground.builder;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShip;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.infantry.AssaultGroundUnitFactory;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.AssaultDefinitionGenerator;
import pwcg.mission.target.TargetType;

public class AmphibiousDefenseBuilder
{
    private Mission mission;
    private AmphibiousAssault amphibiousAssault;
    private AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();
    private GroundUnitCollection amphibiousAssaultDefense;
    private Coordinate assaultPosition;
    private Coordinate defensePosition;
    private Orientation assaultOrientation;
    private Orientation defenseOrientation;
    
    public AmphibiousDefenseBuilder(Mission mission, AmphibiousAssault amphibiousAssault)
    {
        this.mission = mission;
        this.amphibiousAssault = amphibiousAssault;
        
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
        defendingAAAArty();
        
        return amphibiousAssaultDefense;        
    }

    private void buildPositionAndOrientation() throws PWCGException
    {
        AmphibiousAssaultShip referenceShip = amphibiousAssault.getShips().get(0);
        defensePosition = MathUtils.calcNextCoord(referenceShip.getDestination(), referenceShip.getOrientation().getyOri(), 350);
        assaultPosition = MathUtils.calcNextCoord(referenceShip.getDestination(), referenceShip.getOrientation().getyOri(), 150);
        assaultOrientation = referenceShip.getOrientation().copy();
        double defenseAngle = MathUtils.adjustAngle(referenceShip.getOrientation().getyOri(), 180);
        defenseOrientation = new Orientation(defenseAngle);
    }

    private void defendingMachineGun() throws PWCGException
    { 
        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(defensePosition, "Machine Gun", TargetType.TARGET_INFANTRY);
        AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();
        IGroundUnit defenseMachineGunUnit = assaultFactory.createMachineGunUnit (groundUnitInformation);
        amphibiousAssaultDefense.addGroundUnit(defenseMachineGunUnit);
    }

    private void defendingATGuns() throws PWCGException
    { 
        Coordinate antiTankDefensePosition = MathUtils.calcNextCoord(
                defensePosition, 
                defenseOrientation.getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 200.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(antiTankDefensePosition, "Anti Tank Gun", TargetType.TARGET_INFANTRY);
        IGroundUnit defenseAntiTankUnit = assaultFactory.createAntiTankGunUnit (groundUnitInformation);
        amphibiousAssaultDefense.addGroundUnit(defenseAntiTankUnit);
    }

    private void defendingArtillery() throws PWCGException
    { 
        Coordinate artilleryDefensePosition = MathUtils.calcNextCoord(
                defensePosition, 
                defenseOrientation.getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 3000.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(artilleryDefensePosition, "Artillery", TargetType.TARGET_ARTILLERY);
        IGroundUnit defenseArtilleryUnit = assaultFactory.createAssaultArtilleryUnit (groundUnitInformation);
        amphibiousAssaultDefense.addGroundUnit(defenseArtilleryUnit);
    }
    
    private void defendingAAAMachineGun() throws PWCGException
    {
        Coordinate aaaMgDefensePosition = MathUtils.calcNextCoord(
                defensePosition, 
                defenseOrientation.getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 100.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(aaaMgDefensePosition, "Machine Gun AA", TargetType.TARGET_INFANTRY);
        IGroundUnit defenseAAMachineGunUnit = assaultFactory.createAAMachineGunUnitUnit(groundUnitInformation);
        amphibiousAssaultDefense.addGroundUnit(defenseAAMachineGunUnit);
    }
    
    private void defendingAAAArty() throws PWCGException
    {
        Coordinate aaaArtilleryDefensePosition = MathUtils.calcNextCoord(
                defensePosition, 
                defenseOrientation.getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1000.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(aaaArtilleryDefensePosition, "AA Artillery", TargetType.TARGET_ARTILLERY);
        IGroundUnit assaultAAArtilleryUnit = assaultFactory.createAAArtilleryUnitUnit(groundUnitInformation);
        amphibiousAssaultDefense.addGroundUnit(assaultAAArtilleryUnit);
    }

    private GroundUnitInformation buildDefenseGroundUnitInformation(Coordinate unitPosition, String unitName,TargetType targetType) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                CountryFactory.makeCountryByCountry(amphibiousAssault.getDefendingCountry()),
                targetType, 
                unitPosition, 
                assaultPosition, 
                assaultOrientation);
        return groundUnitInformation;
    }
 }

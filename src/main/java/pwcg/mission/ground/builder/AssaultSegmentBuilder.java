package pwcg.mission.ground.builder;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.BattleSize;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.infantry.AssaultGroundUnitFactory;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.target.AssaultDefinitionGenerator;
import pwcg.mission.target.TargetType;

public class AssaultSegmentBuilder
{
    private Mission mission;
    private AssaultDefinition assaultDefinition;
    private AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();

    private GroundUnitCollection battleSegmentUnitCollection;
   
    public AssaultSegmentBuilder(Mission mission, AssaultDefinition assaultDefinition)
	{
        this.mission = mission;
        this.assaultDefinition = assaultDefinition;
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Battle Segment", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitions());

        this.battleSegmentUnitCollection = new GroundUnitCollection (mission.getCampaign(), "Assault Segment", groundUnitCollectionData);
	}


    public GroundUnitCollection generateAssaultSegment() throws PWCGException
    {
        createAssault();
        createDefenders();        
        return battleSegmentUnitCollection;
    }

    /////////////////////////////////////////   Assault  ///////////////////////////////////

    private void createAssault() throws PWCGException
    {
        assaultingTanks();
        assaultingMachineGun();
        if (assaultDefinition.getBattleSize() == BattleSize.BATTLE_SIZE_ASSAULT || assaultDefinition.getBattleSize() == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            assaultingArtillery();
            assaultingAAAMachineGun();
            assaultingAAAArty();
        }
    }

    private void assaultingMachineGun() throws PWCGException
    { 
        Coordinate machineGunStartPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS);  

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(machineGunStartPosition, "Machine Gun", TargetType.TARGET_INFANTRY);
        IGroundUnit assaultingMachineGunUnit = assaultFactory.createMachineGunUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultingMachineGunUnit);
    }

    private void assaultingTanks() throws PWCGException
    {         
        Coordinate tankAssaultStartPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1000.0);  
        
        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(tankAssaultStartPosition, "Tank", TargetType.TARGET_ARMOR);
        IGroundUnit assaultTankUnit = assaultFactory.createAssaultTankUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultTankUnit);
    }

    private void assaultingArtillery() throws PWCGException
    { 
        Coordinate artilleryAssaultPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 3500.0);          

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(artilleryAssaultPosition, "Artillery", TargetType.TARGET_ARTILLERY);
        IGroundUnit assaultArtilleryUnit = assaultFactory.createAssaultArtilleryUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultArtilleryUnit);
    }

    private void assaultingAAAMachineGun() throws PWCGException
    { 
        Coordinate aaaMgAssaultPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 300.0);     

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(aaaMgAssaultPosition, "AA Machine Gun", TargetType.TARGET_INFANTRY);
        IGroundUnit assaultAAMachineGunUnit = assaultFactory.createAAMachineGunUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultAAMachineGunUnit);
    }

    private void assaultingAAAArty() throws PWCGException
    { 
        Coordinate aaaArtyAssaultPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1500.0);            

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(aaaArtyAssaultPosition, "AA Machine Gun", TargetType.TARGET_INFANTRY);
        IGroundUnit assaultAAArtilleryUnit = assaultFactory.createAAArtilleryUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultAAArtilleryUnit);
    }
    

    private GroundUnitInformation buildAssaultGroundUnitInformation(Coordinate unitPosition, String unitName,TargetType targetType) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                assaultDefinition.getAssaultingCountry(),
                targetType, 
                unitPosition, 
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation());
        
        return groundUnitInformation;
    }

    
    /////////////////////////////////////////   Defense  ///////////////////////////////////


    private void createDefenders() throws PWCGException
    {
        defendingMachineGun();
        defendingATCapability();
        if (assaultDefinition.getBattleSize() == BattleSize.BATTLE_SIZE_ASSAULT || assaultDefinition.getBattleSize() == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            defendingArtillery();
            defendingAAAMachineGun();
            defendingAAAArty();        
        }
    }

    private void defendingMachineGun() throws PWCGException
    { 
        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(assaultDefinition.getDefensePosition(), "Machine Gun", TargetType.TARGET_INFANTRY);
        AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();
        IGroundUnit defenseMachineGunUnit = assaultFactory.createMachineGunUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(defenseMachineGunUnit);
    }

    private void defendingATCapability() throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 20)
        {
            defendingTanks();
        }
        else
        {
            defendingATGuns();
        }
    }

    private void defendingTanks() throws PWCGException
    {         
        Coordinate tankDefenseStartPosition = MathUtils.calcNextCoord(
                assaultDefinition.getAssaultPosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1000.0);  
        
        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(tankDefenseStartPosition, "Tank", TargetType.TARGET_ARMOR);
        IGroundUnit assaultTankUnit = assaultFactory.createAssaultTankUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultTankUnit);
    }

    private void defendingATGuns() throws PWCGException
    { 
        Coordinate antiTankDefensePosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 150.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(antiTankDefensePosition, "Anti Tank Gun", TargetType.TARGET_INFANTRY);
        IGroundUnit defenseAntiTankUnit = assaultFactory.createAntiTankGunUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(defenseAntiTankUnit);
    }

    private void defendingArtillery() throws PWCGException
    { 
        Coordinate artilleryDefensePosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 3000.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(artilleryDefensePosition, "Artillery", TargetType.TARGET_ARTILLERY);
        IGroundUnit defenseArtilleryUnit = assaultFactory.createAssaultArtilleryUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(defenseArtilleryUnit);
    }
    
    private void defendingAAAMachineGun() throws PWCGException
    {
        Coordinate aaaMgDefensePosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 150.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(aaaMgDefensePosition, "Machine Gun AA", TargetType.TARGET_INFANTRY);
        IGroundUnit defenseAAMachineGunUnit = assaultFactory.createAAMachineGunUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(defenseAAMachineGunUnit);
    }
    
    private void defendingAAAArty() throws PWCGException
    {
        Coordinate aaaArtilleryDefensePosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1000.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(aaaArtilleryDefensePosition, "AA Artillery", TargetType.TARGET_ARTILLERY);
        IGroundUnit assaultAAArtilleryUnit = assaultFactory.createAAArtilleryUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultAAArtilleryUnit);
    }

    private GroundUnitInformation buildDefenseGroundUnitInformation(Coordinate unitPosition, String unitName,TargetType targetType) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                assaultDefinition.getDefendingCountry(),
                targetType, 
                unitPosition, 
                assaultDefinition.getAssaultPosition(), 
                assaultDefinition.getTowardsAttackerOrientation());
        return groundUnitInformation;
    }

    public IGroundUnit getPrimaryGroundUnit() throws PWCGException
    {
        for (IGroundUnit groundUnit : battleSegmentUnitCollection.getGroundUnits())
        {
            if (groundUnit.getName().endsWith("Tank"))
            {
                return groundUnit;
            }
        }

        for (IGroundUnit groundUnit : battleSegmentUnitCollection.getGroundUnits())
        {
            if (groundUnit.getName().endsWith("Machine Gun"))
            {
                return groundUnit;
            }
        }
        
        return battleSegmentUnitCollection.getGroundUnits().get(0);
    }
 }

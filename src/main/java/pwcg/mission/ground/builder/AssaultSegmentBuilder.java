package pwcg.mission.ground.builder;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.BattleSize;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
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

    private IGroundUnitCollection battleSegmentUnitCollection;
   
    public AssaultSegmentBuilder(Mission mission, AssaultDefinition assaultDefinition)
	{
        this.mission = mission;
        this.assaultDefinition = assaultDefinition;
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Battle Segment", 
                TargetType.TARGET_ASSAULT,
                Coalition.getCoalitions());

        this.battleSegmentUnitCollection = new GroundUnitCollection ("Assault Segment", groundUnitCollectionData);
	}


    public IGroundUnitCollection generateAssaultSegment() throws PWCGException
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
        assaultingMachineGunFlares();
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

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(machineGunStartPosition, "Machine Gun");
        IGroundUnit assaultingMachineGunUnit = assaultFactory.createMachineGunUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultingMachineGunUnit);
    }

    private void assaultingMachineGunFlares() throws PWCGException
    { 
        Coordinate machineGunStartPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 50.0);  

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(machineGunStartPosition, "Machine Gun");
        IFlight triggeringFlight = getTriggeringFlight(groundUnitInformation.getCountry().getSide());
        if (triggeringFlight != null)
        {
            IGroundUnit assaultingMachineGunUnit = assaultFactory.createMachineGunFlareUnit(groundUnitInformation, triggeringFlight);
            battleSegmentUnitCollection.addGroundUnit(assaultingMachineGunUnit);
        }
    }


    private void assaultingTanks() throws PWCGException
    {         
        Coordinate tankAssaultStartPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1000.0);  
        
        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(tankAssaultStartPosition, "Tank");
        IGroundUnit assaultTankUnit = assaultFactory.createAssaultTankUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultTankUnit);
    }

    private void assaultingArtillery() throws PWCGException
    { 
        Coordinate artilleryAssaultPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 3500.0);          

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(artilleryAssaultPosition, "Artillery");
        IGroundUnit assaultArtilleryUnit = assaultFactory.createAssaultArtilleryUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultArtilleryUnit);
    }

    private void assaultingAAAMachineGun() throws PWCGException
    { 
        Coordinate aaaMgAssaultPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 300.0);     

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(aaaMgAssaultPosition, "AA Machine Gun");
        IGroundUnit assaultAAMachineGunUnit = assaultFactory.createAAMachineGunUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultAAMachineGunUnit);
    }

    private void assaultingAAAArty() throws PWCGException
    { 
        Coordinate aaaArtyAssaultPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1500.0);            

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(aaaArtyAssaultPosition, "AA Machine Gun");
        IGroundUnit assaultAAArtilleryUnit = assaultFactory.createAAArtilleryUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultAAArtilleryUnit);
    }
    

    private GroundUnitInformation buildAssaultGroundUnitInformation(Coordinate unitPosition, String unitName) throws PWCGException
    {
        String name = assaultDefinition.getTargetDefinition().getTargetCountry().getCountryName() + " " + unitName;
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                assaultDefinition.getTargetDefinition().getAttackingCountry(),
                name, 
                TargetType.TARGET_INFANTRY, 
                unitPosition, 
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation(), 
                assaultDefinition.determineIsBattleForPlayer());
        
        return groundUnitInformation;
    }

    
    /////////////////////////////////////////   Defense  ///////////////////////////////////


    private void createDefenders() throws PWCGException
    {
        defendingMachineGun();
        defendingMachineGunFlares();
        defendingATGuns();
        if (assaultDefinition.getBattleSize() == BattleSize.BATTLE_SIZE_ASSAULT || assaultDefinition.getBattleSize() == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            defendingArtillery();
            defendingAAAMachineGun();
            defendingAAAArty();        
        }
    }

    private void defendingMachineGun() throws PWCGException
    { 
        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(assaultDefinition.getDefensePosition(), "Machine Gun");
        AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();
        IGroundUnit defenseMachineGunUnit = assaultFactory.createMachineGunUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(defenseMachineGunUnit);
    }

    private void defendingMachineGunFlares() throws PWCGException
    { 
        Coordinate machineGunStartPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 50.0);  

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(machineGunStartPosition, "Machine Gun");
        IFlight triggeringFlight = getTriggeringFlight(groundUnitInformation.getCountry().getSide());
        if (triggeringFlight != null)
        {
            IGroundUnit assaultingMachineGunUnit = assaultFactory.createMachineGunFlareUnit(groundUnitInformation, triggeringFlight);
            battleSegmentUnitCollection.addGroundUnit(assaultingMachineGunUnit);
        }
    }

    private void defendingATGuns() throws PWCGException
    { 
        Coordinate antiTankDefensePosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 150.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(antiTankDefensePosition, "Anti Tank Gun");
        IGroundUnit defenseAntiTankUnit = assaultFactory.createAntiTankGunUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(defenseAntiTankUnit);
    }

    private void defendingArtillery() throws PWCGException
    { 
        Coordinate artilleryDefensePosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 3000.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(artilleryDefensePosition, "Artillery");
        IGroundUnit defenseArtilleryUnit = assaultFactory.createAssaultArtilleryUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(defenseArtilleryUnit);
    }
    
    private void defendingAAAMachineGun() throws PWCGException
    {
        Coordinate aaaMgDefensePosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 150.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(aaaMgDefensePosition, "Machine Gun AA");
        IGroundUnit defenseAAMachineGunUnit = assaultFactory.createAAMachineGunUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(defenseAAMachineGunUnit);
    }
    
    private void defendingAAAArty() throws PWCGException
    {
        Coordinate aaaArtilleryDefensePosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1000.0);     

        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(aaaArtilleryDefensePosition, "AA Artillery");
        IGroundUnit assaultAAArtilleryUnit = assaultFactory.createAAArtilleryUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultAAArtilleryUnit);
    }

    private GroundUnitInformation buildDefenseGroundUnitInformation(Coordinate unitPosition, String unitName) throws PWCGException
    {
        String name = assaultDefinition.getTargetDefinition().getTargetCountry().getCountryName() + " " + unitName;
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                assaultDefinition.getTargetDefinition().getTargetCountry(),
                name, 
                TargetType.TARGET_INFANTRY, 
                unitPosition, 
                assaultDefinition.getAssaultPosition(), 
                assaultDefinition.getTowardsAttackerOrientation(), 
                assaultDefinition.determineIsBattleForPlayer());
        return groundUnitInformation;
    }

    private IFlight getTriggeringFlight(Side side)
    {
        for (IFlight playerFlight : mission.getMissionFlightBuilder().getPlayerFlights())
        {
            if (playerFlight.getSquadron().getCountry().getSide() == side)
            {
                return playerFlight;
            }
        }
        return null;
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

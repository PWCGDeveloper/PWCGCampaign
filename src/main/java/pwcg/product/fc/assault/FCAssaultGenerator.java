package pwcg.product.fc.assault;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.unit.GroundUnitType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.AssaultInformation;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.AssaultGenerator;
import pwcg.mission.ground.BattleSize;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.factory.AssaultFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.mcu.Coalition;

public class FCAssaultGenerator extends AssaultGenerator
{
    private MissionBeginUnitCheckZone missionBeginUnit = null;
    private AssaultInformation assaultInformation = new AssaultInformation();
   
    public FCAssaultGenerator(Campaign campaign, Mission mission, Date date)
    {
        super(campaign, mission, date);
        DISTANCE_BETWEEN_COMBATANTS = 400;
    }

    protected void generateAssaultComponent(Coordinate battleComponentPosition) throws PWCGException 
    {
        assaultInformation.setAggressor(targetDefinition.getAttackingCountry());
        assaultInformation.setDefender(targetDefinition.getTargetCountry());
        
        createPositions(battleComponentPosition, date);
        createMissionBeginUnit();
        createAssault();
        createDefenders();
        
        assaultInformation.finalizeBattle();
        registerAssaultSegment(assaultInformation);
    }

    private void createMissionBeginUnit() throws PWCGException
    {
        Double inBetweenDistance = MathUtils.calcDist(assaultInformation.getAssaultPosition(), assaultInformation.getDefensePosition()) / 2.0;
        Double assaultAngle = MathUtils.calcAngle(assaultInformation.getAssaultPosition(), assaultInformation.getDefensePosition());
        Coordinate inBetweenPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultAngle, inBetweenDistance);
         
        missionBeginUnit = new MissionBeginUnitCheckZone(inBetweenPosition, 20000);
        missionBeginUnit.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalitions(Coalition.getAllCoalitions());
        missionBeginUnit.setStartTime(2);
    }

    private void createPositions(Coordinate groundPosition, Date date) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);

        Coordinate alliedPosition = frontLinesForMap.findClosestFrontCoordinateForSide(groundPosition, Side.ALLIED);
        Coordinate axisPosition = frontLinesForMap.findClosestFrontCoordinateForSide(groundPosition, Side.AXIS);

        // Who is assaulting whom?
        assaultInformation.setAssaultPosition(axisPosition.copy());
        assaultInformation.setDefensePosition(alliedPosition.copy());                
        if (assaultInformation.getAggressor().getSide() == Side.ALLIED)
        {
            assaultInformation.setAssaultPosition(alliedPosition.copy());
            assaultInformation.setDefensePosition(axisPosition.copy());                
        }
    }

    private void createAssault() throws PWCGException
    {
        assaultingTanks();
        if (battleSize == BattleSize.BATTLE_SIZE_ASSAULT || battleSize == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            assaultingArtillery();
            assaultingAAA();
        }
    }

    private void createDefenders() throws PWCGException
    {
        defendingATGuns();
        if (battleSize == BattleSize.BATTLE_SIZE_ASSAULT || battleSize == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            defendingArtillery();
            defendingAAA();
        }
    }

    private void assaultingTanks() throws PWCGException
    {         
        Coordinate tankAssaultStartPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultInformation.getDefenseOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 500.0);            
        String name = assaultInformation.getAggressor().getCountryName() + " Tank";

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                assaultInformation.getAggressor(),
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                tankAssaultStartPosition, 
                assaultInformation.getDefensePosition(), 
                assaultInformation.getAssaultOrientation(), 
                determineIsPlayer());

        AssaultFactory assaultFactory = new AssaultFactory();
        GroundUnit assaultTankUnit = assaultFactory.createAssaultTankUnit (groundUnitInformation);
        assaultInformation.addGroundUnit(GroundUnitType.TANK_UNIT, assaultTankUnit);
    }

    private void assaultingArtillery() throws PWCGException
    { 
        Coordinate artilleryAssaultPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultInformation.getDefenseOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 3000.0);            
        String name = assaultInformation.getAggressor().getCountryName() + " Artillery";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                assaultInformation.getAggressor(),
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                artilleryAssaultPosition, 
                assaultInformation.getDefensePosition(), 
                assaultInformation.getAssaultOrientation(), 
                determineIsPlayer());

        AssaultFactory assaultFactory =  new AssaultFactory();
        GroundUnit assaultArtilleryUnit = assaultFactory.createAssaultArtilleryUnit (groundUnitInformation);
        assaultInformation.addGroundUnit(GroundUnitType.ARTILLERY_UNIT, assaultArtilleryUnit);
    }

    private void assaultingAAA() throws PWCGException
    { 
        assaultingAAAMG();
        assaultingAAAArty();
        
    }

    private void assaultingAAAMG() throws PWCGException
    { 
        Coordinate aaaMgAssaultPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultInformation.getDefenseOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 150.0);            
        AAAUnitFactory groundUnitAAAFactory = new AAAUnitFactory(campaign, assaultInformation.getAggressor(), aaaMgAssaultPosition);
        GroundUnit assaultAaaMgUnit = groundUnitAAAFactory.createAAAMGBattery(2, 2);
        assaultInformation.addGroundUnit(GroundUnitType.AAA_MG_UNIT, assaultAaaMgUnit);
    }

    private void assaultingAAAArty() throws PWCGException
    { 
        Coordinate aaaArtyAssaultPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultInformation.getDefenseOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 1000.0);            
        AAAUnitFactory groundUnitAAAFactory = new AAAUnitFactory(campaign, assaultInformation.getAggressor(), aaaArtyAssaultPosition);
        GroundUnit assaultAaaMgUnit = groundUnitAAAFactory.createAAAArtilleryBattery(1, 1);
        assaultInformation.addGroundUnit(GroundUnitType.AAA_ARTY_UNIT, assaultAaaMgUnit);
    }

    private void defendingATGuns() throws PWCGException
    { 
        String name = assaultInformation.getDefender().getCountryName() + " AntiTank";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                assaultInformation.getDefender(),
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                assaultInformation.getDefensePosition(), 
                assaultInformation.getAssaultPosition(), 
                assaultInformation.getDefenseOrientation(), 
                determineIsPlayer());

        AssaultFactory assaultFactory =  new AssaultFactory();
        GroundUnit defenseAntiTankUnit = assaultFactory.createAntiTankGunUnit (groundUnitInformation);
        assaultInformation.addGroundUnit(GroundUnitType.ARTILLERY_UNIT, defenseAntiTankUnit);
    }

    private void defendingArtillery() throws PWCGException
    { 
        Coordinate artilleryDefensePosition = MathUtils.calcNextCoord(assaultInformation.getDefensePosition(), assaultInformation.getAssaultOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 3000.0);
        String name = assaultInformation.getDefender().getCountryName() + " Artillery";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                assaultInformation.getDefender(),
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                artilleryDefensePosition, 
                assaultInformation.getAssaultPosition(), 
                assaultInformation.getDefenseOrientation(), 
                determineIsPlayer());

        AssaultFactory assaultFactory =  new AssaultFactory();
        GroundUnit defenseArtilleryUnit = assaultFactory.createAssaultArtilleryUnit (groundUnitInformation);
        assaultInformation.addGroundUnit(GroundUnitType.ARTILLERY_UNIT, defenseArtilleryUnit);
    }

    private void defendingAAA() throws PWCGException
    { 
        defendingAAAMG();
        defendingAAAArty();        
    }
    
    private void defendingAAAMG() throws PWCGException
    {
        Coordinate aaaMgDefensePosition = MathUtils.calcNextCoord(assaultInformation.getDefensePosition(), assaultInformation.getAssaultOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 100.0);
        AAAUnitFactory groundUnitFactory =  new AAAUnitFactory(campaign, assaultInformation.getAggressor(), aaaMgDefensePosition);
        GroundUnit assaultAaaMgUnit = groundUnitFactory.createAAAMGBattery(2, 4);
        assaultInformation.addGroundUnit(GroundUnitType.AAA_MG_UNIT, assaultAaaMgUnit);
    }
    
    private void defendingAAAArty() throws PWCGException
    {
        Coordinate aaaArtyDefensePosition = MathUtils.calcNextCoord(assaultInformation.getDefensePosition(), assaultInformation.getAssaultOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 1000.0);
        AAAUnitFactory groundUnitFactory =  new AAAUnitFactory(campaign, assaultInformation.getAggressor(), aaaArtyDefensePosition);
        GroundUnit assaultAaaArtilleryUnit = groundUnitFactory.createAAAMGBattery(2, 4);   
        assaultInformation.addGroundUnit(GroundUnitType.AAA_ARTY_UNIT, assaultAaaArtilleryUnit);
    }
}

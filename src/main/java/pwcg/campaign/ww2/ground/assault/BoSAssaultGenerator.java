package pwcg.campaign.ww2.ground.assault;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAssaultGenerator;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.GroundUnitType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.AssaultGenerator;
import pwcg.mission.ground.GroundUnitFactory;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.factory.AssaultFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.mcu.Coalition;

public class BoSAssaultGenerator extends AssaultGenerator implements IAssaultGenerator
{
    private MissionBeginUnitCheckZone missionBeginUnit = null;

    private double angleOfCombatants = 0;
    
    private static final int DISTANCE_BETWEEN_COMBATANTS = 200;

    public BoSAssaultGenerator(Campaign campaign, Mission mission, Date date)
	{
    	super(campaign, mission, date);
	}

	protected void generateAssaultComponent(Coordinate battleComponentPosition) throws PWCGException 
    {
	    missionBattle.setAggressor(targetDefinition.getAttackingCountry());
	    missionBattle.setDefender(targetDefinition.getTargetCountry());
        
        createPositions(battleComponentPosition, date);
        createMissionBeginUnit();
        createAssault();
        createDefenders();
        
        missionBattle.finalizeBattle();
 	}

    private void createMissionBeginUnit() throws PWCGException
    {
        Coordinate missionBeginPosition = missionBattle.getAssaultPosition().copy();
        if (campaign.determineCountry().isSameSide(targetDefinition.getTargetCountry()))
        {
            missionBeginPosition = missionBattle.getDefensePosition().copy();
        }
        else 
        {
            missionBeginPosition = missionBattle.getAssaultPosition().copy();
        }
        missionBeginUnit = new MissionBeginUnitCheckZone();
        Coalition playerCoalition  = Coalition.getFriendlyCoalition(campaign.determineCountry());
        missionBeginUnit.initialize(missionBeginPosition, 10000, playerCoalition);
        missionBeginUnit.setStartTime(2);
    }

    private void createPositions(Coordinate groundPosition, Date date) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(date);

        Coordinate alliedPosition = frontLinesForMap.findClosestFrontCoordinateForSide(groundPosition, Side.ALLIED);
        Coordinate axisPosition = frontLinesForMap.findClosestFrontCoordinateForSide(groundPosition, Side.AXIS);

        // Who is assaulting whom?
        missionBattle.setAssaultPosition(axisPosition.copy());
        missionBattle.setDefensePosition(alliedPosition.copy());                
        if (missionBattle.getAggressor().getSide() == Side.ALLIED)
        {
            missionBattle.setAssaultPosition(alliedPosition.copy());
            missionBattle.setDefensePosition(axisPosition.copy());                
        }

        angleOfCombatants = MathUtils.calcAngle(missionBattle.getDefensePosition(), missionBattle.getAssaultPosition());        
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
        Coordinate tankAssaultPosition = MathUtils.calcNextCoord(missionBattle.getDefensePosition(), angleOfCombatants, DISTANCE_BETWEEN_COMBATANTS + 500.0);
        
        AssaultFactory groundUnitFactory =  new AssaultFactory(campaign, battleSize);

        GroundUnit assaultTankUnit = groundUnitFactory.createAssaultTankUnit (
                        missionBeginUnit, 
                        missionBattle.getAggressor(), 
                        tankAssaultPosition, 
                        missionBattle.getDefensePosition());

        missionBattle.addGroundUnit(GroundUnitType.TANK_UNIT, assaultTankUnit);
    }

    private void assaultingArtillery() throws PWCGException
    { 
        // We put artillery behind the assault lines
        Coordinate artilleryAssaultPosition = MathUtils.calcNextCoord(missionBattle.getAssaultPosition(), angleOfCombatants, 200.0);
        
        // Assaults are big so set player flight to true.  we will never create an AI minimized assault
        GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(campaign, artilleryAssaultPosition, missionBattle.getAggressor());

        // Generate supporting Artillery
        GroundUnit assaultArtilleryUnit = groundUnitFactory.createArtilleryUnit (
                        missionBeginUnit, 
                        missionBattle.getAggressor(), 
                        artilleryAssaultPosition, 
                        missionBattle.getDefensePosition());

        missionBattle.addGroundUnit(GroundUnitType.ARTILLERY_UNIT, assaultArtilleryUnit);
    }

    private void assaultingAAA() throws PWCGException
    { 
        assaultingAAAMG();
        assaultingAAAArty();
        
    }

    private void assaultingAAAMG() throws PWCGException
    { 
        Coordinate aaaMgAssaultPosition = MathUtils.calcNextCoord(missionBattle.getAssaultPosition(), angleOfCombatants, 10.0);
        AAAUnitFactory groundUnitFactory =  new AAAUnitFactory(campaign, missionBattle.getAggressor(), aaaMgAssaultPosition);
        GroundUnit assaultAaaMgUnit = groundUnitFactory.createAAAMGBattery(2, 4);
        missionBattle.addGroundUnit(GroundUnitType.AAA_MG_UNIT, assaultAaaMgUnit);
    }

    private void assaultingAAAArty() throws PWCGException
    { 
        Coordinate aaaArtyAssaultPosition = MathUtils.calcNextCoord(missionBattle.getAssaultPosition(), angleOfCombatants, 150.0);
        AAAUnitFactory groundUnitFactory =  new AAAUnitFactory(campaign, missionBattle.getAggressor(), aaaArtyAssaultPosition);
        GroundUnit assaultAaaArtilleryUnit = groundUnitFactory.createAAAArtilleryBattery(1, 2);
        missionBattle.addGroundUnit(GroundUnitType.AAA_ARTY_UNIT, assaultAaaArtilleryUnit);
    }

    private void defendingATGuns() throws PWCGException
    { 
        AssaultFactory groundUnitFactory =  new AssaultFactory(campaign, battleSize);
        GroundUnit defenseATFlareUnit = groundUnitFactory.createDefenseUnit (
                        missionBeginUnit, 
                        missionBattle.getDefender(), 
                        missionBattle.getDefensePosition(), 
                        missionBattle.getAssaultPosition());

        missionBattle.addGroundUnit(GroundUnitType.FLARE_UNIT, defenseATFlareUnit);
    }

    private void defendingArtillery() throws PWCGException
    { 

        double angleOfDefendingArillery = MathUtils.adjustAngle(angleOfCombatants, 180.0);
        Coordinate artilleryDefensePosition = MathUtils.calcNextCoord(missionBattle.getDefensePosition(), angleOfDefendingArillery, DISTANCE_BETWEEN_COMBATANTS + 100.0);
        
        GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(campaign, artilleryDefensePosition, missionBattle.getDefender());
        GroundUnit defenseArtilleryUnit = groundUnitFactory.createArtilleryUnit (
                        missionBeginUnit, 
                        missionBattle.getDefender(), 
                        artilleryDefensePosition, 
                        missionBattle.getAssaultPosition());

        missionBattle.addGroundUnit(GroundUnitType.ARTILLERY_UNIT, defenseArtilleryUnit);
    }

    private void defendingAAA() throws PWCGException
    { 
        double angleOfDefendingArtillery = MathUtils.adjustAngle(angleOfCombatants, 180.0);
        defendingAAAMG(angleOfDefendingArtillery);
        defendingAAAArty(angleOfDefendingArtillery);        
    }
    
    private void defendingAAAMG(double angleOfDefendingArtillery) throws PWCGException
    {
        Coordinate aaaMgDefensePosition = MathUtils.calcNextCoord(missionBattle.getDefensePosition(), angleOfDefendingArtillery, DISTANCE_BETWEEN_COMBATANTS + 10.0);
        AAAUnitFactory groundUnitFactory =  new AAAUnitFactory(campaign, missionBattle.getAggressor(), aaaMgDefensePosition);
        GroundUnit assaultAaaMgUnit = groundUnitFactory.createAAAMGBattery(2, 4);
        missionBattle.addGroundUnit(GroundUnitType.AAA_MG_UNIT, assaultAaaMgUnit);
    }
    
    private void defendingAAAArty(double angleOfDefendingArtillery) throws PWCGException
    {
        Coordinate aaaArtyDefensePosition = MathUtils.calcNextCoord(missionBattle.getDefensePosition(), angleOfDefendingArtillery, DISTANCE_BETWEEN_COMBATANTS + 150.0);
        AAAUnitFactory groundUnitFactory =  new AAAUnitFactory(campaign, missionBattle.getAggressor(), aaaArtyDefensePosition);
        GroundUnit assaultAaaArtilleryUnit = groundUnitFactory.createAAAMGBattery(2, 4);   
        missionBattle.addGroundUnit(GroundUnitType.AAA_ARTY_UNIT, assaultAaaArtilleryUnit);
    }
 }

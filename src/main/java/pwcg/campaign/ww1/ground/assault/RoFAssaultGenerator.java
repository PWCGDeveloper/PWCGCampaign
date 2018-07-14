package pwcg.campaign.ww1.ground.assault;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.GroundUnitType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.AssaultGenerator;
import pwcg.mission.ground.GroundUnitFactory;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.factory.AssaultFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.mcu.Coalition;

public class RoFAssaultGenerator extends AssaultGenerator
{
    private MissionBeginUnitCheckZone missionBeginUnit = null;

    private double angleOfCombatants = 0;
    
    private static final int DISTANCE_BETWEEN_COMBATANTS = 200;


    public RoFAssaultGenerator(Campaign campaign, Mission mission, Date date)
    {
        super(campaign, mission, date);
    }

    
    @Override
    protected void generateAssaultComponent(Coordinate battleComponentPosition) throws PWCGException 
    {
        missionBattle.setAggressor(targetDefinition.getAttackingCountry());
        missionBattle.setDefender(targetDefinition.getTargetCountry());
        
        createPositions(battleComponentPosition);
        createMissionBegin();
        createAssault();
        createDefenders();
        
        missionBattle.finalizeBattle();
 	}


    private void createAssault() throws PWCGException
    {
        standingFiringInfantry();
        assaultingInfantry();
        
        if (battleSize != BattleSize.BATTLE_SIZE_SKIRMISH)
        {
            assaultingTanks();
            assaultingMachineGuns();
            assaultingArtillery();
            assaultingAAA();
        }
    }


    private void createDefenders() throws PWCGException
    {
        defendingMachineGuns();
        
        if (battleSize != BattleSize.BATTLE_SIZE_SKIRMISH)
        {
            defendingArtillery();
            defendingAAA();
        }
    }


    private void createMissionBegin() throws PWCGException
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
        missionBeginUnit.initialize(missionBeginPosition, 8000, playerCoalition);
        missionBeginUnit.setStartTime(2);
    }
    
    /**
     * @param groundPosition
     * @throws PWCGException 
     */
    private void createPositions(Coordinate groundPosition) throws PWCGException 
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
    
    /**
     * @throws PWCGException 
     * @
     */
    private void assaultingInfantry() throws PWCGException 
    { 
        AssaultFactory GroundUnitAssaultFactory = new AssaultFactory(campaign, battleSize);

        Coordinate infantryAssaultPosition = MathUtils.calcNextCoord(missionBattle.getDefensePosition(), angleOfCombatants, DISTANCE_BETWEEN_COMBATANTS + 600.0);
        
        GroundUnit assaultInfantryUnit = GroundUnitAssaultFactory.createAssaultInfantryUnit (
                        missionBeginUnit, 
                        missionBattle.getAggressor(), 
                        infantryAssaultPosition, 
                        missionBattle.getDefensePosition());

        missionBattle.addGroundUnit(GroundUnitType.INFANTRY_UNIT, assaultInfantryUnit);
    }
    
    /**
     * @throws PWCGException 
     * @
     */
    private void standingFiringInfantry() throws PWCGException 
    { 
        // Assaults are big so set player flight to true.  we will never create an AI minimized assault
        AssaultFactory GroundUnitAssaultFactory =  new AssaultFactory(campaign, battleSize);

        // Move the assault position about 300 meters from the defensive positions
        Coordinate infantryAssaultPosition = MathUtils.calcNextCoord(missionBattle.getDefensePosition(), angleOfCombatants, DISTANCE_BETWEEN_COMBATANTS);
        
        // Generate the assaulting infantry unit
        GroundUnit assaultInfantryUnit = GroundUnitAssaultFactory.createStandingInfantryUnit (
                        missionBeginUnit, 
                        missionBattle.getAggressor(), 
                        infantryAssaultPosition, 
                        missionBattle.getDefensePosition());
        
        missionBattle.addGroundUnit(GroundUnitType.INFANTRY_UNIT, assaultInfantryUnit);
    }
    
    /**
     * @throws PWCGException 
     * @
     */
    private void assaultingTanks() throws PWCGException 
    {         
        // Assaults are big so set player flight to true.  we will never create an AI minimized assault
        AssaultFactory GroundUnitAssaultFactory =  new AssaultFactory(campaign, battleSize);

        // Generate the assaulting tank unit
        Date campaignDate=     PWCGContextManager.getInstance().getCampaign().getDate();
        Date tankDate = DateUtils.getDateWithValidityCheck("01/10/1917");
        if (missionBattle.getAggressor().isCountry(Country.BRITAIN))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/06/1916");
        }
        else if (missionBattle.getAggressor().isCountry(Country.FRANCE))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/06/1917");
        }
        else if (missionBattle.getAggressor().isCountry(Country.GERMANY))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/03/1918");
        }
        else if (missionBattle.getAggressor().isCountry(Country.USA))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/04/1918");
        }
        else if (missionBattle.getAggressor().isCountry(Country.AUSTRIA))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/12/1918");
        }
        else if (missionBattle.getAggressor().isCountry(Country.RUSSIA))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/12/1918");
        }
        
        if (campaignDate.after(tankDate))
        {
            Coordinate tankAssaultPosition = MathUtils.calcNextCoord(missionBattle.getDefensePosition(), angleOfCombatants, DISTANCE_BETWEEN_COMBATANTS + 500.0);
            
            GroundUnit assaultTankUnit = GroundUnitAssaultFactory.createAssaultTankUnit (
                            missionBeginUnit, 
                            missionBattle.getAggressor(), 
                            tankAssaultPosition, 
                            missionBattle.getDefensePosition());

            missionBattle.addGroundUnit(GroundUnitType.TANK_UNIT, assaultTankUnit);
        }
    }

    private void assaultingMachineGuns() throws PWCGException 
    { 
        // Assaults are big so set player flight to true.  we will never create an AI minimized assault
        AssaultFactory GroundUnitAssaultFactory =  new AssaultFactory(campaign, battleSize);

        Coordinate mgAssaultPosition = MathUtils.calcNextCoord(missionBattle.getDefensePosition(), angleOfCombatants, DISTANCE_BETWEEN_COMBATANTS + 50.0);
        
        // Generate supporting MG
        GroundUnit assaultMGUnit = GroundUnitAssaultFactory.createMachineGunUnit (
                        missionBeginUnit, 
                        missionBattle.getAggressor(), 
                        mgAssaultPosition, 
                        missionBattle.getDefensePosition());

        missionBattle.addGroundUnit(GroundUnitType.MG_UNIT, assaultMGUnit);        
    }

    private void assaultingArtillery() throws PWCGException 
    { 
        Coordinate artilleryAssaultPosition = MathUtils.calcNextCoord(missionBattle.getAssaultPosition(), angleOfCombatants, 200.0);
        GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(campaign, artilleryAssaultPosition, missionBattle.getAggressor());
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
        assaultingAAAArtillery();
    }

    private void assaultingAAAMG() throws PWCGException 
    { 
        Coordinate aaaMgAssaultPosition = MathUtils.calcNextCoord(missionBattle.getAssaultPosition(), angleOfCombatants, 10.0);        
        AAAUnitFactory groundUnitAAAFactory = new AAAUnitFactory(campaign, missionBattle.getAggressor(), aaaMgAssaultPosition);
        GroundUnit assaultAaaMgUnit = groundUnitAAAFactory.createAAAMGBattery(2, 2);
        missionBattle.addGroundUnit(GroundUnitType.AAA_MG_UNIT, assaultAaaMgUnit);
    }

    private void assaultingAAAArtillery() throws PWCGException 
    { 
        Coordinate aaaArtyAssaultPosition = MathUtils.calcNextCoord(missionBattle.getAssaultPosition(), angleOfCombatants, 150.0);
        AAAUnitFactory groundUnitAAAFactory = new AAAUnitFactory(campaign,missionBattle.getAggressor(), aaaArtyAssaultPosition);
        GroundUnit assaultAaaMgUnit = groundUnitAAAFactory.createAAAArtilleryBattery(1, 1);
        missionBattle.addGroundUnit(GroundUnitType.AAA_ARTY_UNIT, assaultAaaMgUnit);
    }
    
    /**
     * @throws PWCGException 
     * @
     */
    private void defendingMachineGuns() throws PWCGException 
    { 
        // Assaults are big so set player flight to true.  we will never create an AI minimized assault
        AssaultFactory GroundUnitAssaultFactory =  new AssaultFactory(campaign, battleSize);

        // Generate supporting MG Pillboxes
        GroundUnit defensePillBoxUnit = GroundUnitAssaultFactory.createPillBoxUnit (
                        missionBeginUnit, 
                        missionBattle.getDefender(), 
                        missionBattle.getDefensePosition(), 
                        missionBattle.getAssaultPosition());

        missionBattle.addGroundUnit(GroundUnitType.MG_UNIT, defensePillBoxUnit);
        
        GroundUnit defensePillBoxFlareUnit = GroundUnitAssaultFactory.createPillBoxFlareUnit (
                        missionBeginUnit, 
                        missionBattle.getDefender(), 
                        missionBattle.getDefensePosition(), 
                        missionBattle.getAssaultPosition());

        missionBattle.addGroundUnit(GroundUnitType.FLARE_UNIT, defensePillBoxFlareUnit);
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
        Coordinate aaaMgDefensePosition = MathUtils.calcNextCoord(missionBattle.getDefensePosition(), angleOfDefendingArtillery, DISTANCE_BETWEEN_COMBATANTS + 10.0);
        
        AAAUnitFactory groundUnitAAAFactory = new AAAUnitFactory(campaign,missionBattle.getDefender(), aaaMgDefensePosition);
        GroundUnit assaultAaaMgUnit = groundUnitAAAFactory.createAAAMGBattery(2, 2);
        missionBattle.addGroundUnit(GroundUnitType.AAA_MG_UNIT, assaultAaaMgUnit);
    }

 }

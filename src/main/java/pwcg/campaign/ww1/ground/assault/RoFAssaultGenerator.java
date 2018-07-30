package pwcg.campaign.ww1.ground.assault;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.GroundUnitType;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
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

public class RoFAssaultGenerator extends AssaultGenerator
{
    private MissionBeginUnitCheckZone missionBeginUnit = null;
    private AssaultInformation assaultInformation = new AssaultInformation();

    public RoFAssaultGenerator(Campaign campaign, Mission mission, Date date)
    {
        super(campaign, mission, date);
        DISTANCE_BETWEEN_COMBATANTS = 200;
    }

    
    @Override
    protected void generateAssaultComponent(Coordinate battleComponentPosition) throws PWCGException 
    {
        assaultInformation.setAggressor(targetDefinition.getAttackingCountry());
        assaultInformation.setDefender(targetDefinition.getTargetCountry());
        
        createPositions(battleComponentPosition);
        createMissionBegin();
        createAssault();
        createDefenders();
        
        assaultInformation.finalizeBattle();
        registerAssaultSegment(assaultInformation);
 	}


    private void createAssault() throws PWCGException
    {
        standingFiringInfantry();
        assaultingInfantry();
        
        if (battleSize == BattleSize.BATTLE_SIZE_ASSAULT || battleSize == BattleSize.BATTLE_SIZE_OFFENSIVE)
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
        
        if (battleSize == BattleSize.BATTLE_SIZE_ASSAULT || battleSize == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            defendingArtillery();
            defendingAAA();
        }
    }

    private void createPositions(Coordinate groundPosition) throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(date);

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


    private void createMissionBegin() throws PWCGException
    {
        Coordinate missionBeginPosition = assaultInformation.getAssaultPosition().copy();
        if (campaign.determineCountry().isSameSide(targetDefinition.getTargetCountry()))
        {
            missionBeginPosition = assaultInformation.getDefensePosition().copy();
        }
        else 
        {
            missionBeginPosition = assaultInformation.getAssaultPosition().copy();
        }
        
        missionBeginUnit = new MissionBeginUnitCheckZone();
        Coalition playerCoalition  = Coalition.getFriendlyCoalition(campaign.determineCountry());
        missionBeginUnit.initialize(missionBeginPosition, 8000, playerCoalition);
        missionBeginUnit.setStartTime(2);
    }

    private void assaultingInfantry() throws PWCGException 
    { 
        String name = assaultInformation.getAggressor().getCountryName() + " Infantry";        
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                assaultInformation.getAggressor(),
                name, 
                TacticalTarget.TARGET_ASSAULT, 
                assaultInformation.getAssaultPosition(), 
                assaultInformation.getDefensePosition(), 
                assaultInformation.getAssaultOrientation(), 
                determineIsPlayer());

        AssaultFactory assaultFactory = new AssaultFactory();        
        GroundUnit assaultInfantryUnit = assaultFactory.createAssaultInfantryUnit (groundUnitInformation);
        assaultInformation.addGroundUnit(GroundUnitType.INFANTRY_UNIT, assaultInfantryUnit);
    }

    private void standingFiringInfantry() throws PWCGException 
    { 
        String name = assaultInformation.getAggressor().getCountryName() + " Infantry";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                assaultInformation.getAggressor(),
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                assaultInformation.getAssaultPosition(), 
                assaultInformation.getDefensePosition(), 
                assaultInformation.getAssaultOrientation(), 
                determineIsPlayer());
        
        AssaultFactory assaultFactory = new AssaultFactory();
        GroundUnit assaultInfantryUnit = assaultFactory.createAssaultInfantryUnit (groundUnitInformation);
        assaultInformation.addGroundUnit(GroundUnitType.INFANTRY_UNIT, assaultInfantryUnit);
    }

    private void assaultingTanks() throws PWCGException 
    {         
        // Assaults are big so set player flight to true.  we will never create an AI minimized assault
        AssaultFactory groundUnitAssaultFactory =  new AssaultFactory();

        // Generate the assaulting tank unit
        Date campaignDate=     PWCGContextManager.getInstance().getCampaign().getDate();
        Date tankDate = DateUtils.getDateWithValidityCheck("01/10/1917");
        if (assaultInformation.getAggressor().isCountry(Country.BRITAIN))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/06/1916");
        }
        else if (assaultInformation.getAggressor().isCountry(Country.FRANCE))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/06/1917");
        }
        else if (assaultInformation.getAggressor().isCountry(Country.GERMANY))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/03/1918");
        }
        else if (assaultInformation.getAggressor().isCountry(Country.USA))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/04/1918");
        }
        else if (assaultInformation.getAggressor().isCountry(Country.AUSTRIA))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/12/1918");
        }
        else if (assaultInformation.getAggressor().isCountry(Country.RUSSIA))
        {
            tankDate = DateUtils.getDateWithValidityCheck("01/12/1918");
        }
        
        if (campaignDate.after(tankDate))
        {
            Coordinate tankAssaultStartPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultInformation.getDefenseOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 200.0);            
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

            GroundUnit assaultTankUnit = groundUnitAssaultFactory.createAssaultTankUnit (groundUnitInformation);
            assaultInformation.addGroundUnit(GroundUnitType.TANK_UNIT, assaultTankUnit);
        }
    }

    private void assaultingMachineGuns() throws PWCGException 
    { 
        Coordinate mgAssaultStartPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultInformation.getDefenseOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 50.0);            
        String name = assaultInformation.getAggressor().getCountryName() + " MG";

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                assaultInformation.getAggressor(),
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                mgAssaultStartPosition, 
                assaultInformation.getDefensePosition(), 
                assaultInformation.getAssaultOrientation(), 
                determineIsPlayer());

        AssaultFactory assaultFactory =  new AssaultFactory();
        GroundUnit assaultMGUnit = assaultFactory.createMachineGunUnit(groundUnitInformation);
        assaultInformation.addGroundUnit(GroundUnitType.MG_UNIT, assaultMGUnit);        
    }

    private void assaultingArtillery() throws PWCGException 
    { 
        Coordinate artilleryAssaultPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultInformation.getDefenseOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 2500.0);            
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
        assaultingAAAArtillery();
    }

    private void assaultingAAAMG() throws PWCGException 
    { 
        Coordinate aaaMgAssaultPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultInformation.getDefenseOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 100.0);            
        AAAUnitFactory groundUnitAAAFactory = new AAAUnitFactory(campaign, assaultInformation.getAggressor(), aaaMgAssaultPosition);
        GroundUnit assaultAaaMgUnit = groundUnitAAAFactory.createAAAMGBattery(2, 2);
        assaultInformation.addGroundUnit(GroundUnitType.AAA_MG_UNIT, assaultAaaMgUnit);
    }

    private void assaultingAAAArtillery() throws PWCGException 
    { 
        Coordinate aaaArtyAssaultPosition = MathUtils.calcNextCoord(assaultInformation.getAssaultPosition(), assaultInformation.getDefenseOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 1000.0);            
        AAAUnitFactory groundUnitAAAFactory = new AAAUnitFactory(campaign, assaultInformation.getAggressor(), aaaArtyAssaultPosition);
        GroundUnit assaultAaaMgUnit = groundUnitAAAFactory.createAAAArtilleryBattery(1, 1);
        assaultInformation.addGroundUnit(GroundUnitType.AAA_ARTY_UNIT, assaultAaaMgUnit);
    }

    private void defendingMachineGuns() throws PWCGException 
    { 
        String name = assaultInformation.getDefender().getCountryName() + " Pillbox";
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
        GroundUnit defensePillBoxUnit = assaultFactory.createPillBoxUnit (groundUnitInformation);
        assaultInformation.addGroundUnit(GroundUnitType.MG_UNIT, defensePillBoxUnit);
        
        GroundUnit defensePillBoxFlareUnit = assaultFactory.createPillBoxFlareUnit (groundUnitInformation);
        assaultInformation.addGroundUnit(GroundUnitType.FLARE_UNIT, defensePillBoxFlareUnit);
    }

    private void defendingArtillery() throws PWCGException 
    { 
        Coordinate artilleryDefensePosition = MathUtils.calcNextCoord(assaultInformation.getDefensePosition(), assaultInformation.getAssaultOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 2500.0);
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
        Coordinate aaaMgDefensePosition = MathUtils.calcNextCoord(assaultInformation.getDefensePosition(), assaultInformation.getAssaultOrientation().getyOri(), DISTANCE_BETWEEN_COMBATANTS + 100.0);
        AAAUnitFactory groundUnitAAAFactory = new AAAUnitFactory(campaign, assaultInformation.getDefender(), aaaMgDefensePosition);
        GroundUnit assaultAaaMgUnit = groundUnitAAAFactory.createAAAMGBattery(2, 2);
        assaultInformation.addGroundUnit(GroundUnitType.AAA_MG_UNIT, assaultAaaMgUnit);
    }

 }

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
import pwcg.mission.target.TargetType;

public class AmphibiousAssaultAttackBuilder
{
    private Mission mission;
    private AmphibiousAssault amphibiousAssault;
    private AmphibiousAssaultShip landingCraft;
    private AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();
    private GroundUnitCollection amphibiousAssaultAttack;
    private Coordinate assaultPosition;
    private Coordinate defensePosition;
    private Orientation assaultOrientation;
    private Orientation defenseOrientation;
    
    public AmphibiousAssaultAttackBuilder(Mission mission, AmphibiousAssault amphibiousAssault, AmphibiousAssaultShip landingCraft)
    {
        this.mission = mission;
        this.amphibiousAssault = amphibiousAssault;
        this.landingCraft = landingCraft;
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Amphibious Assault", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitions());

        amphibiousAssaultAttack = new GroundUnitCollection ("Amphibious Assault Attack", groundUnitCollectionData);
    }

    public GroundUnitCollection generateAmphibiousAssaultAttack() throws PWCGException
    {
        buildPositionAndOrientation();

        assaultingTanks();
        assaultingMachineGun();
        assaultingAAAMachineGun();
        
        return amphibiousAssaultAttack;        
    }

    private void buildPositionAndOrientation() throws PWCGException
    {
        defensePosition = MathUtils.calcNextCoord(landingCraft.getDestination(), landingCraft.getOrientation().getyOri(), 350);
        assaultPosition = MathUtils.calcNextCoord(landingCraft.getDestination(), landingCraft.getOrientation().getyOri(), 150);
        assaultOrientation = landingCraft.getOrientation().copy();
        double defenseAngle = MathUtils.adjustAngle(landingCraft.getOrientation().getyOri(), 180);
        defenseOrientation = new Orientation(defenseAngle);
    }

    private void assaultingMachineGun() throws PWCGException
    { 
        Coordinate machineGunStartPosition = MathUtils.calcNextCoord(assaultPosition, assaultOrientation.getyOri(), 100);  

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(machineGunStartPosition, "Machine Gun", TargetType.TARGET_INFANTRY);
        IGroundUnit assaultingMachineGunUnit = assaultFactory.createMachineGunUnit (groundUnitInformation);
        amphibiousAssaultAttack.addGroundUnit(assaultingMachineGunUnit);
    }

    private void assaultingTanks() throws PWCGException
    {         
        Coordinate tankAssaultStartPosition = MathUtils.calcNextCoord(assaultPosition, assaultOrientation.getyOri(), 150);  
        
        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(tankAssaultStartPosition, "Tank", TargetType.TARGET_ARMOR);
        IGroundUnit assaultTankUnit = assaultFactory.createAssaultTankUnit (groundUnitInformation);
        amphibiousAssaultAttack.addGroundUnit(assaultTankUnit);
    }

    private void assaultingAAAMachineGun() throws PWCGException
    { 
        Coordinate aaaMgAssaultPosition = MathUtils.calcNextCoord(assaultPosition, assaultOrientation.getyOri(), 120);  

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(aaaMgAssaultPosition, "AA Machine Gun", TargetType.TARGET_INFANTRY);
        IGroundUnit assaultAAMachineGunUnit = assaultFactory.createAAMachineGunUnitUnit(groundUnitInformation);
        amphibiousAssaultAttack.addGroundUnit(assaultAAMachineGunUnit);
    }

    private GroundUnitInformation buildAssaultGroundUnitInformation(Coordinate unitPosition, String unitName,TargetType targetType) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                CountryFactory.makeCountryByCountry(amphibiousAssault.getAggressorCountry()),
                targetType, 
                assaultPosition, 
                defensePosition, 
                defenseOrientation);
        
        return groundUnitInformation;
    }


}

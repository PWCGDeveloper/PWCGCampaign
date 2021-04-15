package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShipDefinition;
import pwcg.campaign.context.Country;
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

public class AmphibiousAssaultAttackBuilder
{
    private Mission mission;
    private AmphibiousAssault amphibiousAssault;
    private AmphibiousAssaultShipDefinition landingCraftDefinition;
    private AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();
    private GroundUnitCollection amphibiousAssaultAttack;
    private AmphibiousPositionBuilder amphibiousPositionBuilder;
    
    public AmphibiousAssaultAttackBuilder(Mission mission, AmphibiousAssault amphibiousAssault, AmphibiousAssaultShipDefinition landingCraftDefinition)
    {
        this.mission = mission;
        this.amphibiousAssault = amphibiousAssault;
        this.landingCraftDefinition = landingCraftDefinition;
        
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
        assaultingAAAMachineGun();
        finishGroundUnitCollection();
                
        return amphibiousAssaultAttack;        
    }

    private void finishGroundUnitCollection() throws PWCGException
    {
        List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();
        primaryAssaultSegmentGroundUnits.add(amphibiousAssaultAttack.getPrimaryGroundUnit());
        amphibiousAssaultAttack.setPrimaryGroundUnit(primaryAssaultSegmentGroundUnits.get(0));
        amphibiousAssaultAttack.finishGroundUnitCollection();
        
        amphibiousAssaultAttack.setCheckZoneTriggerDistance(250);
        amphibiousAssaultAttack.setCheckZoneTriggerUnit(landingCraftDefinition.getLandingCraftGroundUnit().getGroundUnits().get(0).getVehicles().get(0).getEntity().getIndex());
    }

    private void buildPositionAndOrientation() throws PWCGException
    {
        amphibiousPositionBuilder = new AmphibiousPositionBuilder(landingCraftDefinition);
        amphibiousPositionBuilder.buildPositionAndOrientation();
    }

    private void assaultingTanks() throws PWCGException
    {         
        Coordinate tankAssaultStartPosition = MathUtils.calcNextCoord(amphibiousPositionBuilder.getAssaultPosition(), amphibiousPositionBuilder.getAssaultOrientation().getyOri(), 100);  

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(tankAssaultStartPosition, "Tank", TargetType.TARGET_ARMOR, GroundUnitSize.GROUND_UNIT_SIZE_LOW);
        if (amphibiousAssault.getAggressorCountry() == Country.RUSSIA)
        {
            groundUnitInformation.setRequestedUnitType("t70");
        }
        else if (amphibiousAssault.getAggressorCountry() == Country.USA || amphibiousAssault.getAggressorCountry() == Country.BRITAIN)
        {
            groundUnitInformation.setRequestedUnitType("m4a3");
        }
        
        IGroundUnit assaultTankUnit = assaultFactory.createAssaultTankUnit (groundUnitInformation);
        amphibiousAssaultAttack.addGroundUnit(assaultTankUnit);
    }

    private void assaultingAAAMachineGun() throws PWCGException
    { 
        Coordinate aaaMgAssaultPosition = MathUtils.calcNextCoord(amphibiousPositionBuilder.getAssaultPosition(), amphibiousPositionBuilder.getAssaultOrientation().getyOri(), 50);  

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(aaaMgAssaultPosition, "AA Machine Gun", TargetType.TARGET_INFANTRY, GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        IGroundUnit assaultAAMachineGunUnit = assaultFactory.createAAMachineGunUnitUnit(groundUnitInformation);
        amphibiousAssaultAttack.addGroundUnit(assaultAAMachineGunUnit);
    }

    private GroundUnitInformation buildAssaultGroundUnitInformation(Coordinate unitPosition, String unitName, TargetType targetType, GroundUnitSize groundUnitSize) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                CountryFactory.makeCountryByCountry(amphibiousAssault.getAggressorCountry()),
                targetType, 
                unitPosition, 
                amphibiousPositionBuilder.getDefensePosition(), 
                amphibiousPositionBuilder.getAssaultOrientation());
        
        groundUnitInformation.setUnitSize(groundUnitSize);

        return groundUnitInformation;
    }


}

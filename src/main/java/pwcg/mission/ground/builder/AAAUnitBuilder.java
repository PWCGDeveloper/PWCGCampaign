package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.artillery.GroundAAArtilleryBattery;
import pwcg.mission.ground.unittypes.artillery.GroundAAMachineGunBattery;
import pwcg.mission.ground.unittypes.artillery.SearchLightUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AAAUnitBuilder
{    
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public AAAUnitBuilder (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign  = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public IGroundUnitCollection createAAAMGBattery (GroundUnitSize groundUnitSize) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createAAGroundUnitInformation(groundUnitSize);
        IGroundUnit mgBattery = new GroundAAMachineGunBattery(groundUnitInformation);
        mgBattery.createGroundUnit();

        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "AA MG Battery", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection ("AAA MG Battery", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(mgBattery);
        groundUnitCollection.setPrimaryGroundUnit(mgBattery);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }
    
    public IGroundUnitCollection createAAAArtilleryBatteryFromMission(Mission mission, GroundUnitSize groundUnitSize) throws PWCGException
    {
        if (mission.isNightMission())
        {
            return createAAAArtilleryBatteryWithSearchLight(groundUnitSize);
        }
        else
        {
            return createAAAArtilleryBattery(groundUnitSize);
        }
    }
    
    public IGroundUnitCollection createAAAArtilleryBattery (GroundUnitSize groundUnitSize) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createAAGroundUnitInformation(groundUnitSize);
        IGroundUnit aaaBattery = new GroundAAArtilleryBattery(groundUnitInformation);
        aaaBattery.createGroundUnit();

        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "AA Artillery Battery", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection ("AAA Artillery Battery", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(aaaBattery);
        groundUnitCollection.setPrimaryGroundUnit(aaaBattery);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }

    public IGroundUnitCollection createAAAArtilleryBatteryWithSearchLight (GroundUnitSize groundUnitSize) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createAAGroundUnitInformation(groundUnitSize);
        IGroundUnit aaaBattery = new GroundAAArtilleryBattery(groundUnitInformation);
        aaaBattery.createGroundUnit();
        
        IGroundUnit searchLightUnit = buildSearchLightUnit();

        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "AA Artillery Battery With Search Light", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection ("AAA Search Light Battery", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(aaaBattery);
        groundUnitCollection.addGroundUnit(searchLightUnit);
        groundUnitCollection.setPrimaryGroundUnit(aaaBattery);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }

    private GroundUnitInformation createAAGroundUnitInformation(GroundUnitSize groundUnitSize) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                targetDefinition.getCountry(), 
                TargetType.TARGET_INFANTRY, 
                targetDefinition.getPosition(), 
                targetDefinition.getPosition(), 
                Orientation.createRandomOrientation());
        
        groundUnitInformation.setUnitSize(groundUnitSize);
        return groundUnitInformation;
    }
    
    private IGroundUnit buildSearchLightUnit() throws PWCGException 
    {
        GroundUnitInformation groundUnitInformation = createSearchlightGroundUnitInformation();
        IGroundUnit searchLightUnit = new SearchLightUnit(groundUnitInformation);
        searchLightUnit.createGroundUnit();
        return searchLightUnit;
    }
    

    private GroundUnitInformation createSearchlightGroundUnitInformation() throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                targetDefinition.getCountry(), 
                TargetType.TARGET_INFANTRY, 
                targetDefinition.getPosition(), 
                targetDefinition.getPosition(), 
                Orientation.createRandomOrientation());
        
        groundUnitInformation.setUnitSize(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        return groundUnitInformation;
    }

}

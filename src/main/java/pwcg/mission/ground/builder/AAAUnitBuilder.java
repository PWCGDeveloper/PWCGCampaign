package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
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
import pwcg.mission.target.TacticalTarget;

public class AAAUnitBuilder
{    
    private Campaign campaign;
    private ICountry country;
    private Coordinate position;
    
    public AAAUnitBuilder (Campaign campaign, ICountry country, Coordinate location)
    {
        this.campaign  = campaign;
        this.country  = country;
        this.position  = location.copy();
    }

    public IGroundUnitCollection createAAAMGBattery (GroundUnitSize groundUnitSize) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createAAGroundUnitInformation(groundUnitSize);
        IGroundUnit mgBattery = new GroundAAMachineGunBattery(groundUnitInformation);
        mgBattery.createGroundUnit();

        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "AA MG Battery", 
                TacticalTarget.TARGET_INFANTRY,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection (groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(mgBattery);
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
                TacticalTarget.TARGET_INFANTRY,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection (groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(aaaBattery);
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
                TacticalTarget.TARGET_INFANTRY,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection (groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(aaaBattery);
        groundUnitCollection.addGroundUnit(searchLightUnit);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }

    private GroundUnitInformation createAAGroundUnitInformation(GroundUnitSize groundUnitSize) throws PWCGException
    {
        String nationality = country.getNationality();
        String name = nationality + " AA";

        boolean isPlayerTarget = false;
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, country, name, TacticalTarget.TARGET_DEFENSE, position, position, Orientation.createRandomOrientation(), isPlayerTarget);
        
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
        String nationality = country.getNationality();
        String name = nationality + " Search Light";

        boolean isPlayerTarget = false;
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, country, name, TacticalTarget.TARGET_DEFENSE, position, position, Orientation.createRandomOrientation(), isPlayerTarget);
        
        groundUnitInformation.setUnitSize(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        return groundUnitInformation;
    }

}

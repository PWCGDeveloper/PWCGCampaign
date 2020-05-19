package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.artillery.GroundAAArtilleryBattery;
import pwcg.mission.ground.unittypes.artillery.GroundAAMachineGunBattery;
import pwcg.mission.ground.unittypes.infantry.BalloonUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class BalloonUnitBuilder
{
    private Mission mission;
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    private GroundUnitInformation groundUnitInformation;
    private IGroundUnitCollection groundUnitCollection;

    public BalloonUnitBuilder (Mission mission, TargetDefinition targetDefinition)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.targetDefinition  = targetDefinition;
    }

    public IGroundUnitCollection createBalloonUnit (ICountry country) throws PWCGException
    {
        buildGroundUnitInformation(country);
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.BALLOON_GROUND_UNIT_COLLECTION, 
                "Balloon Unit", 
                TargetType.TARGET_BALLOON,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        groundUnitCollection = new GroundUnitCollection ("Balloon Defense Battery", groundUnitCollectionData);
        
        buildBalloon();
        buildBalloonAAArtillery();
        buildBalloonAAMachineGuns();
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }

    private void buildGroundUnitInformation(ICountry country) throws PWCGException
    {
        groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                targetDefinition.getCountry(), 
                TargetType.TARGET_BALLOON,
                targetDefinition.getPosition(), 
                targetDefinition.getPosition(),
                targetDefinition.getOrientation());
    }


    private void buildBalloon() throws PWCGException
    {
        IGroundUnit balloonUnit = new BalloonUnit(groundUnitInformation);
        balloonUnit.createGroundUnit();
        groundUnitCollection.setPrimaryGroundUnit(balloonUnit);
        groundUnitCollection.addGroundUnit(balloonUnit);
        registerBalloon(balloonUnit);
    }
    
    private void buildBalloonAAArtillery() throws PWCGException
    {
        IGroundUnit aaArtilleryBattery = new GroundAAArtilleryBattery(groundUnitInformation);
        aaArtilleryBattery.createGroundUnit();
        groundUnitCollection.addGroundUnit(aaArtilleryBattery);
    }
    
    private void buildBalloonAAMachineGuns() throws PWCGException
    {
        IGroundUnit aaMachineGunBattery = new GroundAAMachineGunBattery(groundUnitInformation);
        aaMachineGunBattery.createGroundUnit();
        groundUnitCollection.addGroundUnit(aaMachineGunBattery);
    }
    
    private void registerBalloon(IGroundUnit balloonUnit) throws PWCGException
    {        
        mission.getMissionGroundUnitManager().registerBalloon(balloonUnit);
    }

}

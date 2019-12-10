package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
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
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class BalloonUnitBuilder
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    private GroundUnitInformation groundUnitInformation;
    private IGroundUnitCollection groundUnitCollection;

    public BalloonUnitBuilder (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public IGroundUnitCollection createBalloonUnit (ICountry country) throws PWCGException
    {
        buildGroundUnitInformation(country);
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.BALLOON_GROUND_UNIT_COLLECTION, 
                "Balloon Unit", 
                TacticalTarget.TARGET_BALLOON,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        groundUnitCollection = new GroundUnitCollection (groundUnitCollectionData);
        
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
                country, 
                TacticalTarget.TARGET_BALLOON.getTargetName(), 
                TacticalTarget.TARGET_BALLOON, 
                targetDefinition.getTargetPosition(), 
                targetDefinition.getTargetPosition(), 
                Orientation.createRandomOrientation(), 
                targetDefinition.isPlayerTarget());
    }


    private void buildBalloon() throws PWCGException
    {
        IGroundUnit balloonUnit = new BalloonUnit(groundUnitInformation);
        balloonUnit.createGroundUnit();
        groundUnitCollection.addGroundUnit(balloonUnit);
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
}

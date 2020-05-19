package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class TrainUnitBuilder
{
    private Mission mission;
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public TrainUnitBuilder (Mission mission, TargetDefinition targetDefinition)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.targetDefinition = targetDefinition;
    }

    public IGroundUnitCollection createTrainUnit () throws PWCGException
    {
        IGroundUnitCollection groundUnitCollection = createTrain();
        registerTrainStationInUse();
        return groundUnitCollection;
    }
    
    private IGroundUnitCollection createTrain() throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformation();
        IGroundUnit train = new GroundTrainUnit(groundUnitInformation);
        train.createGroundUnit();

        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Train", 
                TargetType.TARGET_TRAIN,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection ("Train", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(train);
        groundUnitCollection.setPrimaryGroundUnit(train);
        groundUnitCollection.finishGroundUnitCollection();
                
        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformation() throws PWCGException
    {
        Coordinate destination = getTrainDestination();

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                targetDefinition.getCountry(), 
                TargetType.TARGET_TRAIN,
                targetDefinition.getPosition(), 
                destination,
                targetDefinition.getOrientation());
                
        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private Coordinate getTrainDestination() throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Block destinationStation = groupData.getRailroadStationFinder().getDestinationTrainPosition(targetDefinition.getPosition(), targetDefinition.getCountry(), campaign.getDate());
        if (destinationStation != null)
        {
            return destinationStation.getPosition();
        }
        else
        {
            return targetDefinition.getPosition();
        }
    }
    
    private void registerTrainStationInUse() throws PWCGException
    {        
        mission.getMissionGroundUnitManager().registerTrainStation(targetDefinition.getPosition());
    }

}

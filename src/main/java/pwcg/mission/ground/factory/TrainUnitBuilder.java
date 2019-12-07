package pwcg.mission.ground.factory;

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
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;

public class TrainUnitBuilder
{
    private Mission mission;
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public TrainUnitBuilder (Mission mission, TargetDefinition targetDefinition)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.targetDefinition  = targetDefinition;
    }

    public IGroundUnitCollection createTrainUnit () throws PWCGException
    {
        IGroundUnitCollection groundUnitCollection = createTrain();
        registerTrainStationInUse();
        return groundUnitCollection;
    }
    
    private IGroundUnitCollection createTrain() throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit();
        IGroundUnit train = new GroundTrainUnit(groundUnitInformation);
        train.createGroundUnit();
        
        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, "Train", Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));
        groundUnitCollection.addGroundUnit(train);
        groundUnitCollection.finishGroundUnitCollection();

        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit() throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, targetDefinition);
        Coordinate destination = getTrainDestination();
        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private Coordinate getTrainDestination() throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Block destinationStation = groupData.getRailroadStationFinder().getDestinationTrainPosition(targetDefinition.getTargetPosition(), targetDefinition.getTargetCountry(), campaign.getDate());
        if (destinationStation != null)
        {
            return destinationStation.getPosition();
        }
        else
        {
            return targetDefinition.getTargetPosition();
        }
    }
    
    private void registerTrainStationInUse() throws PWCGException
    {        
        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Block trainStation = groupManager.getRailroadStationFinder().getClosestTrainPosition(targetDefinition.getTargetPosition());
        mission.getMissionGroundUnitManager().registerTrainStation(trainStation);
    }

}

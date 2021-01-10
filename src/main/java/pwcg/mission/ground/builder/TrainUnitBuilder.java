package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class TrainUnitBuilder
{
    private Campaign campaign;
    private Block station;
    private ICountry country;
    
    public TrainUnitBuilder (Campaign campaign, Block station, ICountry country)
    {
        this.campaign = campaign;
        this.station = station;
        this.country = country;
    }

    public GroundUnitCollection createTrainUnit () throws PWCGException
    {
        GroundUnitCollection groundUnitCollection = createTrain();
        return groundUnitCollection;
    }
    
    private GroundUnitCollection createTrain() throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformation();
        IGroundUnit train = new GroundTrainUnit(groundUnitInformation);
        train.createGroundUnit();

        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Train", 
                TargetType.TARGET_TRAIN,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        GroundUnitCollection groundUnitCollection = new GroundUnitCollection ("Train", groundUnitCollectionData);
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
                country, 
                TargetType.TARGET_TRAIN,
                station.getPosition(), 
                destination,
                station.getOrientation());
                
        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private Coordinate getTrainDestination() throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Block destinationStation = groupData.getRailroadStationFinder().getDestinationTrainPosition(station.getPosition(), country, campaign.getDate());
        if (destinationStation != null)
        {
            return destinationStation.getPosition();
        }
        else
        {
            return station.getPosition();
        }
    }
}

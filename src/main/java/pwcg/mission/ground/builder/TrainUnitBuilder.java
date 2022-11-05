package pwcg.mission.ground.builder;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
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

        GroundUnitCollection groundUnitCollection = new GroundUnitCollection (campaign, "Train", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(train);
        groundUnitCollection.setPrimaryGroundUnit(train);
        groundUnitCollection.finishGroundUnitCollection();
                
        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformation() throws PWCGException
    {
        Coordinate startPosition = getRailStartLocation(station.getPosition());
        Coordinate destination = getTrainDestination(startPosition);

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country, 
                TargetType.TARGET_TRAIN,
                startPosition, 
                destination,
                station.getOrientation());
                
        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }
    
    private Coordinate getRailStartLocation(Coordinate referenceCoordinate) throws PWCGException 
    {
        double closest = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        Coordinate startPosition = null;
        for (List<Coordinate> railRoute : PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getMapTransportRail().getTransportRoutes())
        {
            for (Coordinate railCoordinate : railRoute)
            {
                double distance = MathUtils.calcDist(referenceCoordinate, railCoordinate);
                if (distance < closest)
                {
                    closest = distance;
                    startPosition = railCoordinate.copy();
                }
            }
        }
        
        if (startPosition == null)
        {
            startPosition = referenceCoordinate.copy();
        }

        return startPosition;
    }

    private Coordinate getTrainDestination(Coordinate startPosition) throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager();
        Block destinationStation = groupData.getRailroadStationFinder().getDestinationTrainPosition(campaign.getCampaignMap(), station.getPosition(), country, campaign.getDate());
        Coordinate railDestinationCoordinate = null;
        if (destinationStation != null)
        {
            double closest = PositionFinder.ABSURDLY_LARGE_DISTANCE;
            for (Coordinate railCoordinate : PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getMapTransportRail().getAllTransportCoordinates())
            {
                double distance = MathUtils.calcDist(destinationStation.getPosition(), railCoordinate);
                if (distance < closest)
                {
                    closest = distance;
                    railDestinationCoordinate = railCoordinate.copy();
                }
            }
        }
        
        if (railDestinationCoordinate == null)
        {
            railDestinationCoordinate = startPosition.copy();
        }
        
        return railDestinationCoordinate;
    }
}

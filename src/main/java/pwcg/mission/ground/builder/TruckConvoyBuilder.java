package pwcg.mission.ground.builder;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Bridge;
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
import pwcg.mission.ground.unittypes.transport.GroundTruckAAConvoyUnit;
import pwcg.mission.ground.unittypes.transport.GroundTruckAmmoConvoyUnit;
import pwcg.mission.ground.unittypes.transport.GroundTruckConvoyUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class TruckConvoyBuilder
{
    private Campaign campaign;
    private Bridge bridge;
    private ICountry country;
    
    public TruckConvoyBuilder (Campaign campaign, Bridge bridge, ICountry country)
    {
        this.campaign = campaign;
        this.bridge  = bridge;
        this.country  = country;
    }

    public GroundUnitCollection createTruckConvoy () throws PWCGException
    {
        GroundUnitCollection groundUnitCollection = createTrucks();
        return groundUnitCollection;
    }

    private GroundUnitCollection createTrucks () throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit();
        IGroundUnit truckConvoy = new GroundTruckConvoyUnit(groundUnitInformation);
        truckConvoy.createGroundUnit();
        
        IGroundUnit ammoTruckConvoy = new GroundTruckAmmoConvoyUnit(groundUnitInformation);
        ammoTruckConvoy.createGroundUnit();
        
        IGroundUnit aaTruckConvoy = new GroundTruckAAConvoyUnit(groundUnitInformation);
        aaTruckConvoy.createGroundUnit();
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Truck Convoy", 
                TargetType.TARGET_TRANSPORT,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));
        
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(campaign, "Truck Convoy", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(truckConvoy);
        groundUnitCollection.addGroundUnit(ammoTruckConvoy);
        groundUnitCollection.addGroundUnit(aaTruckConvoy);
        groundUnitCollection.setPrimaryGroundUnit(truckConvoy);
        groundUnitCollection.finishGroundUnitCollection();

        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit() throws PWCGException
    {
        Coordinate startPosition = getRoadStartLocation(bridge.getPosition());
        Coordinate destination = getConvoyDestination(startPosition);

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country, 
                TargetType.TARGET_TRANSPORT,
                startPosition, 
                destination,
                bridge.getOrientation());

        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }
    
    private Coordinate getRoadStartLocation(Coordinate referenceCoordinate) 
    {
        double closest = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        Coordinate startPosition = null;
        for (List<Coordinate> railRoute : PWCGContext.getInstance().getCurrentMap().getMapTransportRoads().getTransportRoutes())
        {
            for (Coordinate railCoordinate : railRoute)
            {
                double distance = MathUtils.calcDist(referenceCoordinate, railCoordinate);
                if (distance < closest)
                {                    
                    if (distance > 1000) 
                    {
                        closest = distance;
                        startPosition = railCoordinate.copy();
                    }
                }
            }
        }
        
        if (startPosition == null)
        {
            startPosition = referenceCoordinate.copy();
        }

        return startPosition;
    }

    private Coordinate getConvoyDestination(Coordinate startPosition) throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Bridge destinationBridge = groupData.getBridgeFinder().findDestinationBridge(bridge.getPosition(), country.getSide(), campaign.getDate());
        Coordinate roadDestinationCoordinate = null;
       if (destinationBridge != null)
        {
            double closest = PositionFinder.ABSURDLY_LARGE_DISTANCE;
            for (Coordinate railCoordinate : PWCGContext.getInstance().getCurrentMap().getMapTransportRoads().getAllTransportCoordinates())
            {
                double distance = MathUtils.calcDist(destinationBridge.getPosition(), railCoordinate);
                if (distance < closest)
                {
                    closest = distance;
                    roadDestinationCoordinate = railCoordinate.copy();
                }
            }
        }
        
        if (roadDestinationCoordinate == null)
        {
            roadDestinationCoordinate = startPosition.copy();
        }
        
        return roadDestinationCoordinate;
    }
}

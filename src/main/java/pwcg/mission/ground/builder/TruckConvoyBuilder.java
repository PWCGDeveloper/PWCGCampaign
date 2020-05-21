package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Bridge;
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
import pwcg.mission.ground.unittypes.transport.GroundTruckAAConvoyUnit;
import pwcg.mission.ground.unittypes.transport.GroundTruckConvoyUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class TruckConvoyBuilder
{
    private Mission mission;
    private Campaign campaign;
    private Bridge bridge;
    private ICountry country;
    
    public TruckConvoyBuilder (Mission mission, Bridge bridge, ICountry country)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.bridge  = bridge;
        this.country  = country;
    }

    public IGroundUnitCollection createTruckConvoy () throws PWCGException
    {
        IGroundUnitCollection groundUnitCollection = createTrucks();
        registerBridgeInUse();
        return groundUnitCollection;
    }

    private IGroundUnitCollection createTrucks () throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit();
        IGroundUnit truckConvoy = new GroundTruckConvoyUnit(groundUnitInformation);
        truckConvoy.createGroundUnit();
        
        IGroundUnit aatruckConvoy = new GroundTruckAAConvoyUnit(groundUnitInformation);
        aatruckConvoy.createGroundUnit();
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Truck Convoy", 
                TargetType.TARGET_TRANSPORT,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));
        
        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection("Truck Convoy", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(truckConvoy);
        groundUnitCollection.addGroundUnit(aatruckConvoy);
        groundUnitCollection.setPrimaryGroundUnit(truckConvoy);
        groundUnitCollection.finishGroundUnitCollection();

        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit() throws PWCGException
    {
        Coordinate destination = getConvoyDestination();

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country, 
                TargetType.TARGET_TRANSPORT,
                bridge.getPosition(), 
                destination,
                bridge.getOrientation());

        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private Coordinate getConvoyDestination() throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Bridge destinationBridge = groupData.getBridgeFinder().findDestinationBridge(bridge.getPosition(), country.getSide(), campaign.getDate());
        if (destinationBridge != null)
        {
            return destinationBridge.getPosition();
        }
        else
        {
            return bridge.getPosition();
        }
    }
    
    private void registerBridgeInUse() throws PWCGException
    {        
        mission.getMissionGroundUnitManager().registerBridge(bridge);
    }
}

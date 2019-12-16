package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
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
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class TruckConvoyBuilder
{
    private Mission mission;
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public TruckConvoyBuilder (Mission mission, TargetDefinition targetDefinition)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.targetDefinition  = targetDefinition;
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
                TacticalTarget.TARGET_TRANSPORT,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));
        
        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection(groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(truckConvoy);
        groundUnitCollection.addGroundUnit(aatruckConvoy);
        groundUnitCollection.setPrimaryGroundUnit(truckConvoy);
        groundUnitCollection.finishGroundUnitCollection();

        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit() throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, targetDefinition);
        Coordinate destination = getConvoyDestination();
        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private Coordinate getConvoyDestination() throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Bridge destinationBridge = groupData.getBridgeFinder().findDestinationBridge(targetDefinition.getTargetPosition(), targetDefinition.getTargetCountry().getSide(), campaign.getDate());
        if (destinationBridge != null)
        {
            return destinationBridge.getPosition();
        }
        else
        {
            return targetDefinition.getTargetPosition();
        }
    }
    
    private void registerBridgeInUse() throws PWCGException
    {        
        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Bridge bridge = groupManager.getBridgeFinder().findClosestBridge(targetDefinition.getTargetPosition());
        mission.getMissionGroundUnitManager().registerBridge(bridge);
    }
}

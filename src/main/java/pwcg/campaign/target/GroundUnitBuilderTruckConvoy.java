package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.GrountUnitTruckFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderTruckConvoy
{
    public static GroundUnitCollection createTruckConvoy(Campaign campaign, Mission mission, TargetDefinition targetDefinition) throws PWCGException, PWCGMissionGenerationException
    {
        GrountUnitTruckFactory groundUnitFactory = new GrountUnitTruckFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetCountry(), campaign.getDate());
        GroundUnit targetUnit = groundUnitFactory.createTruckConvoy();

        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.TRANSPORT_UNIT, targetUnit);
        
        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        Bridge bridge = groupManager.getBridgeFinder().findClosestBridge(targetDefinition.getTargetLocation());
        mission.getMissionGroundUnitManager().registerBridge(bridge);

        return groundUnitCollection;
    }
}

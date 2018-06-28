package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.GrountUnitTrainFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderTrain
{
    public static GroundUnitCollection createTrainTarget(Campaign campaign, Mission mission, TargetDefinition targetDefinition) throws PWCGException, PWCGMissionGenerationException
    {
        GrountUnitTrainFactory groundUnitFactory = new GrountUnitTrainFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetCountry(), campaign.getDate());
        GroundUnit targetUnit = groundUnitFactory.createTrainUnit();
        
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.TRANSPORT_UNIT, targetUnit);
        
        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        Block trainStation = groupManager.getRailroadStationFinder().getClosestTrainPosition(targetDefinition.getTargetLocation());
        mission.getMissionGroundUnitManager().registerTrainStation(trainStation);
        return groundUnitCollection;
    }

}

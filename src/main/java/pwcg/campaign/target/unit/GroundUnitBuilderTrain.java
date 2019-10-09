package pwcg.campaign.target.unit;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.factory.TrainUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderTrain
{
    public static GroundUnitCollection createTrainTarget(Campaign campaign, Mission mission, TargetDefinition targetDefinition) throws PWCGException, PWCGMissionGenerationException
    {
        TrainUnitFactory groundUnitFactory = new TrainUnitFactory(campaign, targetDefinition);
        GroundUnit targetUnit = groundUnitFactory.createTrainUnit();
        
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.TRANSPORT_UNIT, targetUnit);
        
        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Block trainStation = groupManager.getRailroadStationFinder().getClosestTrainPosition(targetDefinition.getTargetPosition());
        mission.getMissionGroundUnitManager().registerTrainStation(trainStation);
        return groundUnitCollection;
    }

}

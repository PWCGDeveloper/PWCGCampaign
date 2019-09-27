package pwcg.mission.flight.intercept;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;

public class InterceptAiCoordinateGenerator
{
    private Mission mission;
    
    public InterceptAiCoordinateGenerator(Mission mission)
    {
        this.mission = mission;
    }
    
    public Coordinate createTargetCoordinates() throws PWCGException
    {
        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        double missionTargetRadius = (mission.getMissionBorders().getBoxWidth() / 2) + 5000;
        Block selectedTarget = groupManager.getBlockFinder().getBlockWithinRadius(mission.getMissionBorders().getCenter(), missionTargetRadius);
        return selectedTarget.getPosition();
    }
}

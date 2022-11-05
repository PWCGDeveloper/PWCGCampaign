package pwcg.mission.flight.intercept;

import pwcg.campaign.context.PWCGContext;
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
        GroupManager groupManager = PWCGContext.getInstance().getMap(mission.getCampaignMap()).getGroupManager();
        double missionTargetRadius = mission.getMissionBorders().getAreaRadius();
        Block selectedTarget = groupManager.getBlockFinder().getBlockWithinRadius(mission.getMissionBorders().getCenter(), missionTargetRadius);
        return selectedTarget.getPosition();
    }
}

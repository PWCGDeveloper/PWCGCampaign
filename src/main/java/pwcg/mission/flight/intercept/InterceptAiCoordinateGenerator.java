package pwcg.mission.flight.intercept;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class InterceptAiCoordinateGenerator
{
    private Campaign campaign;
    private Squadron squadron;
    
    public InterceptAiCoordinateGenerator(Campaign campaign, Squadron squadron)
    {
        this.campaign = campaign;
        this.squadron = squadron;
    }
    
    public Coordinate createTargetCoordinates() throws PWCGException
    {
        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();        
        Block selectedTarget = groupManager.getBlockFinder().getBlockWithinRadius(squadron.determineCurrentPosition(campaign.getDate()), 30000.0);
        return selectedTarget.getPosition();
    }
}

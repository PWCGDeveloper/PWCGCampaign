package pwcg.mission;

import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MissionCenterBuilderSkirmish implements IMissionCenterBuilder
{
    private Campaign campaign;
    private Skirmish skirmish;

    public MissionCenterBuilderSkirmish(Campaign campaign, Skirmish skirmish)
    {
        this.campaign = campaign;
        this.skirmish = skirmish;
    }

    @Override
    public Coordinate findMissionCenter(int missionBoxRadius) throws PWCGException
    {   
        FrontLinesForMap frontlines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> coordinatesInBox = frontlines.findFrontPositionsForSideInBox(skirmish.getCoordinateBox(), skirmish.getAttacker().getOppositeSide());
        if (!coordinatesInBox.isEmpty())
        {
            Collections.shuffle(coordinatesInBox);
            return coordinatesInBox.get(0).getPosition();
        }
        else
        {
            return skirmish.getCoordinateBox().getCenter();
        }        
    }
}

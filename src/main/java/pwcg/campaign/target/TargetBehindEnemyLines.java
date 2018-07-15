package pwcg.campaign.target;

import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class TargetBehindEnemyLines
{
    public static Coordinate getBehindLinesTargetPosition(TargetDefinition targetDefinition, int minDistanceBehindLines, int maxDistanceBehindLines) throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(targetDefinition.getDate());
        Coordinate behindLinesPosition = frontLinesForMap.findPositionBehindLinesForSide(
                targetDefinition.getTargetGeneralPosition(), 
                targetDefinition.getPreferredRadius(), 
                minDistanceBehindLines, 
                maxDistanceBehindLines, 
                targetDefinition.getTargetCountry().getSide());

        return behindLinesPosition;
    }
}

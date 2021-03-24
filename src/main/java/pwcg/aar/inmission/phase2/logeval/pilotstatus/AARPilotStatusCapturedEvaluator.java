package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.BehindEnemyLines;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;

public class AARPilotStatusCapturedEvaluator
{
    private BehindEnemyLines behindEnemyLines;

    public AARPilotStatusCapturedEvaluator (Date date)
	{
    	behindEnemyLines = new BehindEnemyLines(date);
	}
    
    public boolean isCrewMemberCaptured(FrontMapIdentifier mapId, Coordinate landingCoords, Side side) 
    {        
        try
        {
            if (landingCoords != null)
            {
                boolean isBehindEnemyLines = behindEnemyLines.isBehindEnemyLinesForCapture(mapId, landingCoords, side);

                if (isBehindEnemyLines)
                {
                    if (!didCrewMemberEscape(mapId, landingCoords, side))
                    {
                        return true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }

        return false;
    }

    private boolean didCrewMemberEscape(FrontMapIdentifier mapId, Coordinate landingCoords, Side side)
                    throws PWCGException
    {
        double distanceBehindEnemyLines = behindEnemyLines.getDistanceBehindLines(mapId, landingCoords, side);
        // 10% for every KM behind the lines.  Past 10Km and you're screwed
        double odds = distanceBehindEnemyLines * 10.0;
        double roll = RandomNumberGenerator.getRandom(100);
        if (roll < odds)
        {
            return false;
        }
        
        return true;
    }

    public void setBehindEnemyLines(BehindEnemyLines behindEnemyLines)
    {
        this.behindEnemyLines = behindEnemyLines;
    }
}

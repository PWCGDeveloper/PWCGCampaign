package pwcg.aar.inmission.phase1.parse;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase1.parse.event.AType;
import pwcg.core.exception.PWCGException;

public class AARLogKeeper 
{
    public static List<String> selectLogLinesToKeep(List<String> logLines) throws PWCGException 
    {
        List<String> keptLogLinesFromMission = new ArrayList<>();
        boolean endOfMissionFound = false;
        for (String logLine : logLines) 
        {
            if (logLine.contains(AType.ATYPE4.getAtypeLogIdentifier()))
            {
                endOfMissionFound = true;
            }

            if (!endOfMissionFound)
            {
                keptLogLinesFromMission.add(logLine);
            }
        }
        return keptLogLinesFromMission;
    }
}


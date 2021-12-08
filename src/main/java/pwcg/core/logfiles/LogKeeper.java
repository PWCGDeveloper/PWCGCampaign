package pwcg.core.logfiles;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.event.AType;

public class LogKeeper
{
    public static List<String> selectLogLinesToKeep(List<String> logLines) throws PWCGException
    {
        List<String> keptLogLinesFromMission = new ArrayList<>();
        boolean endOfMissionFound = false;

        int lastMissionEndLogIndex = logLines.size();
        for (int index = 0; index < logLines.size(); ++index)
        {
            String logLine = logLines.get(index);
            if (logLine.contains(AType.ATYPE4.getAtypeLogIdentifier()))
            {
                lastMissionEndLogIndex = index;
            }
        }

        for (int index = 0; index < logLines.size(); ++index)
        {
            String logLine = logLines.get(index);
            if (index == lastMissionEndLogIndex)
            {
                endOfMissionFound = true;
            }

            if (!endOfMissionFound)
            {
                keptLogLinesFromMission.add(logLine);
            }
            else if (!logLine.contains(AType.ATYPE3.getAtypeLogIdentifier()))
            {
                keptLogLinesFromMission.add(logLine);
            }
        }
        return keptLogLinesFromMission;
    }
}

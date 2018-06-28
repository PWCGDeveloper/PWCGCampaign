package pwcg.aar.inmission.phase1.parse;

import pwcg.core.exception.PWCGException;

public interface IAARLogParser
{

    AARLogEventData parseLogFilesForMission() throws PWCGException;

}
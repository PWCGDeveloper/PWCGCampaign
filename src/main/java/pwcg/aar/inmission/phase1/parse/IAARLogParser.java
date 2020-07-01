package pwcg.aar.inmission.phase1.parse;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public interface IAARLogParser
{

    AARLogEventData parseLogFilesForMission(Campaign campaign) throws PWCGException;

}
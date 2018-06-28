package pwcg.campaign.api;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

public interface IMissionFile
{

    /**
     * Write the mission to a mission file
     * @throws PWCGIOException 
     * 
     * @
     */
    void writeMission() throws PWCGException;
}
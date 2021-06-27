package pwcg.campaign.api;

import pwcg.core.exception.PWCGException;

public interface IMissionFile
{

    /**
     * Write the mission to a mission file
     * @throws PWCGException 
     * 
     * @
     */
    void writeMission() throws PWCGException;
}
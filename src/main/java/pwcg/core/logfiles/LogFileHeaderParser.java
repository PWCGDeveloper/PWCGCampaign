package pwcg.core.logfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.event.AType;
import pwcg.core.logfiles.event.IAType0;
import pwcg.core.logfiles.event.LogEventFactory;
import pwcg.core.utils.PWCGLogger;

public class LogFileHeaderParser
{
    public String parseHeaderOnly(String campaignName, String logFileName) throws PWCGException, PWCGException 
    {
        String missionFileName = LogFileSet.NOT_AVAILABLE;
        try
        {
            String logFileDirDir = LogFileLocationFinder.determineLogFileLocation(logFileName);
            File logFileWithHeader = new File(logFileDirDir + logFileName);
            BufferedReader reader = new BufferedReader(new FileReader(logFileWithHeader));
            String line;

            while ((line = reader.readLine()) != null) 
            {
                String missionFileNameFromLine = parseLine(campaignName, line);
                if (!missionFileNameFromLine.isEmpty())
                {
                    missionFileName = missionFileNameFromLine;
                    break;
                }
            }

            reader.close();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        
        return missionFileName;
    }
    
    public String parseLine(String campaignName, String line) throws PWCGException, PWCGException 
    {
        String atype0Tag = AType.ATYPE0.getAtypeLogIdentifier();
        if (line.contains(atype0Tag))
        {
            IAType0 missionHeader = LogEventFactory.createAType0(campaignName, line);
            String missionFileName = missionHeader.getMissionFileName();
            return missionFileName;
        }
        
        return "";
    }
}

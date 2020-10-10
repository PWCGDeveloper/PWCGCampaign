package pwcg.aar.prelim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import pwcg.aar.AARLogFileLocationFinder;
import pwcg.aar.inmission.phase1.parse.AARMissionLogFileSet;
import pwcg.aar.inmission.phase1.parse.event.AType;
import pwcg.aar.inmission.phase1.parse.event.IAType0;
import pwcg.aar.inmission.phase1.parse.event.LogEventFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class AARHeaderParser
{
    public AARHeaderParser()
    {
    }

    public String parseHeaderOnly(String campaignName, String logFileName) throws PWCGException, PWCGIOException 
    {
        String missionFileName = AARMissionLogFileSet.NOT_AVAILABLE;
        try
        {
            String logFileDirDir = AARLogFileLocationFinder.determineLogFileLocation(logFileName);
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
            throw new PWCGIOException(e.getMessage());
        }
        
        return missionFileName;
    }
    
    public String parseLine(String campaignName, String line) throws PWCGException, PWCGIOException 
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

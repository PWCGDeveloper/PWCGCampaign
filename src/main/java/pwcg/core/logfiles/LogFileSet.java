package pwcg.core.logfiles;

import pwcg.core.exception.PWCGException;

public class AARMissionLogFileSet 
{
    public static final String NOT_AVAILABLE = "NotAVailable";
    private String logFileName = NOT_AVAILABLE;

    public AARMissionLogFileSet()
    {
    }

    public String getFileSetTimeStamp() throws PWCGException
    {
        if (logFileName.equals(NOT_AVAILABLE))
        {
            throw new PWCGException("Attempt to get date from unavailable file set");
        }
        
        int openParen = logFileName.indexOf('(') + 1;
        int closeParen = logFileName.indexOf(')');
        String setDate = logFileName.substring(openParen, closeParen);
        
        return setDate;
    }

    public String getLogFileName()
    {
        return logFileName;
    }
    
    public void setLogFileName(String logFile)
    {
        this.logFileName = logFile;
    }
}

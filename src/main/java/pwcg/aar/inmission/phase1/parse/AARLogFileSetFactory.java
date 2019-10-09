package pwcg.aar.inmission.phase1.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;

public class AARLogFileSetFactory
{
    private Map<Integer, String> logFileSetsAvailable = new TreeMap<Integer, String>();
    private DirectoryReader directoryReader = new DirectoryReader();
    
    public AARLogFileSetFactory()
    {    
    }

    public void determineMissionResultsFileForRequestedFileSet(String logFileName) throws PWCGException 
    {
        logFileSetsAvailable.clear();
        getMissionResultsFileSetForDirectory(logFileName);
    }

    private void getMissionResultsFileSetForDirectory(String logFileNameForMission) throws PWCGException
    {
        String fileSetIdentifier = extractMissionDateFromMissionLogFileName(logFileNameForMission);
        
        String simulatorDataDir = PWCGContext.getInstance().getDirectoryManager().getSimulatorDataDir();
        directoryReader.sortilesInDir(simulatorDataDir);
        for (String logFileName : directoryReader.getFiles()) 
        {
            if (logFileName.contains(fileSetIdentifier))
            {
                addLogFileToResultSet( logFileName);
            }
        }
    }

    private void addLogFileToResultSet(String logFileName)
    {
        int fileIndex = extractMissionSequenceFromMissionLogFileName(logFileName);
        String simulatorDataDir = PWCGContext.getInstance().getDirectoryManager().getSimulatorDataDir();
        String filepath = simulatorDataDir + logFileName;
        logFileSetsAvailable.put(fileIndex, filepath);
    }

    private int extractMissionSequenceFromMissionLogFileName(String logFileName)
    {
        int indexStart = logFileName.indexOf("[");
        int indexEnd = logFileName.indexOf("]");
        String fileindexStr = logFileName.substring(indexStart + 1, indexEnd);
        int fileIndex = new Integer(fileindexStr);
        return fileIndex;
    }

    private String extractMissionDateFromMissionLogFileName(String logFileName)
    {
        int startIndex = logFileName.indexOf("(") + 1;
        int endIndex = logFileName.indexOf(")");
        
        String fileSetIdentifier = logFileName.substring(startIndex, endIndex);
        
        return fileSetIdentifier;
    }

    public List<String> getLogFileSets()
    {
        return new ArrayList<String>(logFileSetsAvailable.values());
    }

    public DirectoryReader getDirectoryReader()
    {
        return directoryReader;
    }

    public void setDirectoryReader(DirectoryReader directoryReader)
    {
        this.directoryReader = directoryReader;
    }
}

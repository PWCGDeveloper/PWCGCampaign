package pwcg.core.logfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;

public class LogFileSetFactory
{
    private Map<Integer, String> logFileSetsAvailable = new TreeMap<Integer, String>();
    private DirectoryReader directoryReader = new DirectoryReader();
    
    public LogFileSetFactory()
    {    
    }

    public void determineMissionResultsFileForRequestedFileSet(String rootLogFileName) throws PWCGException 
    {
        logFileSetsAvailable.clear();
        getMissionResultsFileSetForDirectory(rootLogFileName);
    }

    private void getMissionResultsFileSetForDirectory(String rootLogFileName) throws PWCGException
    {
        String fileSetIdentifier = extractMissionDateFromMissionLogFileName(rootLogFileName);
        String logFileDir = LogFileLocationFinder.determineLogFileLocation(rootLogFileName);
        directoryReader.sortFilesInDir(logFileDir);
        for (String logFileName : directoryReader.getFiles()) 
        {
            if (logFileName.contains(fileSetIdentifier))
            {
                addLogFileToResultSet( logFileName);
            }
        }
    }

    private void addLogFileToResultSet(String rootLogFileName) throws PWCGException
    {
        String logFileDir = LogFileLocationFinder.determineLogFileLocation(rootLogFileName);
        int fileIndex = extractMissionSequenceFromMissionLogFileName(rootLogFileName);
        String filepath = logFileDir + rootLogFileName;
        logFileSetsAvailable.put(fileIndex, filepath);
    }

    private int extractMissionSequenceFromMissionLogFileName(String rootLogFileName)
    {
        int indexStart = rootLogFileName.indexOf("[");
        int indexEnd = rootLogFileName.indexOf("]");
        String fileindexStr = rootLogFileName.substring(indexStart + 1, indexEnd);
        int fileIndex = Integer.valueOf(fileindexStr);
        return fileIndex;
    }

    private String extractMissionDateFromMissionLogFileName(String rootLogFileName)
    {
        int startIndex = rootLogFileName.indexOf("(") + 1;
        int endIndex = rootLogFileName.indexOf(")");
        
        String fileSetIdentifier = rootLogFileName.substring(startIndex, endIndex);
        
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

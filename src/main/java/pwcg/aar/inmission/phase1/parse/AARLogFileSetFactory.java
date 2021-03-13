package pwcg.aar.inmission.phase1.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.aar.AARLogFileLocationFinder;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;

public class AARLogFileSetFactory
{
    private Map<Integer, File> logFileSetsAvailable = new TreeMap<>();
    private DirectoryReader directoryReader = new DirectoryReader();
    
    public AARLogFileSetFactory()
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
        String logFileDir = AARLogFileLocationFinder.determineLogFileLocation(rootLogFileName);
        directoryReader.sortFilesInDir(logFileDir);
        for (File logFile : directoryReader.getFiles()) 
        {
            if (logFile.getName().contains(fileSetIdentifier))
            {
                addLogFileToResultSet(logFile);
            }
        }
    }

    private void addLogFileToResultSet(File logFile) throws PWCGException
    {
        int fileIndex = extractMissionSequenceFromMissionLogFileName(logFile.getName());
        logFileSetsAvailable.put(fileIndex, logFile);
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

    public List<File> getLogFileSets()
    {
        return new ArrayList<File>(logFileSetsAvailable.values());
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

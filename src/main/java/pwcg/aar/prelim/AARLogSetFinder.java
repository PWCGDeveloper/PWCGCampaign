package pwcg.aar.prelim;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;

public class AARLogSetFinder
{
    private DirectoryReader directoryReader;

    public AARLogSetFinder(DirectoryReader directoryReader)
    {
        this.directoryReader = directoryReader;
    }
    
    public List<File> getSortedLogFileSets() throws PWCGException
    {
        TreeMap<String, File> sortedLogSetsFromData = getLogFilesFromData();
        TreeMap<String, File> sortedLogSetsFromUserDefined = getLogFilesFromUserDefined();

        TreeMap<String, File> sortedLogSets = new TreeMap<>();
        sortedLogSets.putAll(sortedLogSetsFromData);
        sortedLogSets.putAll(sortedLogSetsFromUserDefined);
        
        return new ArrayList<File>(sortedLogSets.values());
    }

    private TreeMap<String, File> getLogFilesFromData() throws PWCGException
    {
        String simulatorDataDir = PWCGDirectorySimulatorManager.getInstance().getSimulatorDataDir();
        return getLogFilesFromDirectory(simulatorDataDir);
    }

    private TreeMap<String, File> getLogFilesFromUserDefined() throws PWCGException
    {
        String userLogDir = PWCGContext.getInstance().getMissionLogDirectory();
        return getLogFilesFromDirectory(userLogDir);
    }

    private TreeMap<String, File> getLogFilesFromDirectory(String logFileDirectoryName) throws PWCGException
    {
        if (!logFileDirectoryName.isEmpty())
        {
            directoryReader.sortFilesInDir(logFileDirectoryName);
            List<File> logSetsFromData = directoryReader.getSortedFilesWithFilter("[0].txt");
            return sortLogSets(logSetsFromData);
        }
        return new TreeMap<>();
    }

    private TreeMap<String, File> sortLogSets(List<File> logSetsFromData)
    {
        TreeMap<String, File> sortedLogSets = new TreeMap<>();
        for (File logSet : logSetsFromData)
        {
            sortedLogSets.put(logSet.getName(), logSet);
        }
        return sortedLogSets;
    }
}

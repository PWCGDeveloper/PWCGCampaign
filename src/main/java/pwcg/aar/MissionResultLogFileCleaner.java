package pwcg.aar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;

public class MissionResultLogFileCleaner 
{
    private DirectoryReader directoryReader;

    public MissionResultLogFileCleaner(DirectoryReader directoryReader)
    {
        this.directoryReader = directoryReader;
    }
    
    public int cleanMissionResultLogFiles() throws PWCGException 
    {
        List<String> filesToDelete = new ArrayList<String>();
        int deleteAll = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey);
        if (deleteAll == 1)
        {
            String simulatorDataDir = PWCGDirectorySimulatorManager.getInstance().getSimulatorDataDir();
            filesToDelete.addAll(cleanOldMissionLogs(simulatorDataDir));

            String userLogDir = PWCGContext.getInstance().getMissionLogDirectory();
            if (!userLogDir.isEmpty())
            {
                filesToDelete.addAll(cleanOldMissionLogs(userLogDir));
            }
        }
        
        return filesToDelete.size();
    }

    private List<String> cleanOldMissionLogs(String simulatorDataDir) throws PWCGException
    {
        List<String> filesToDelete;
        List<String> allMissionFiles = getAllMissionResultsFiles(simulatorDataDir);
        filesToDelete = selectOlderMissionLogFiles(allMissionFiles);
        FileUtils.deleteFilesByFileName (filesToDelete);
        return filesToDelete;
    }

    public  List<String> selectOlderMissionLogFiles(List<String> allMissionFiles) throws PWCGException 
    {
        List<String> filesToDelete = new ArrayList<String>();
        for (String pathname : allMissionFiles) 
        {
            File missionLogFile = new File(pathname);
            if (missionLogFile.exists())
            {
                long ageOfFile = FileUtils.ageOfFilesInMillis(pathname);
                long oneDayAgo = System.currentTimeMillis() - 86400000;
                if (ageOfFile < oneDayAgo)
                {
                    filesToDelete.add(pathname);
                }
            }
        }
        
        return filesToDelete;
    }

    private List<String> getAllMissionResultsFiles(String directory) throws PWCGException 
    {
        List<String> results = new ArrayList<String>();

        directoryReader.sortFilesInDir(directory);
        for (String filename : directoryReader.getFiles()) 
        {
            if (filename.contains("missionReport") && filename.contains(".txt"))
            {
                String filepath = directory + filename;
                results.add(filepath);
            }
        }

        return results;
    }
}


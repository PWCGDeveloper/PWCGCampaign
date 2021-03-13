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
        List<File> filesToDelete = new ArrayList<>();
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

    private List<File> cleanOldMissionLogs(String simulatorDataDir) throws PWCGException
    {
        List<File> filesToDelete;
        List<File> allMissionFiles = getAllMissionResultsFiles(simulatorDataDir);
        filesToDelete = selectOlderMissionLogFiles(allMissionFiles);
        FileUtils.deleteFiles (filesToDelete);
        return filesToDelete;
    }

    private  List<File> selectOlderMissionLogFiles(List<File> allMissionFiles) throws PWCGException 
    {
        List<File> filesToDelete = new ArrayList<>();
        for (File missionLogFile : allMissionFiles) 
        {
            if (missionLogFile.exists())
            {
                long ageOfFile = FileUtils.ageOfFilesInMillis(missionLogFile);
                long oneDayAgo = System.currentTimeMillis() - 86400000;
                if (ageOfFile < oneDayAgo)
                {
                    filesToDelete.add(missionLogFile);
                }
            }
        }
        
        return filesToDelete;
    }

    private List<File> getAllMissionResultsFiles(String directory) throws PWCGException 
    {
        List<File> results = new ArrayList<>();

        directoryReader.sortFilesInDir(directory);
        for (File file : directoryReader.getFiles()) 
        {
            if (file.getName().contains("missionReport") && file.getName().contains(".txt"))
            {
                results.add(file);
            }
        }

        return results;
    }
}


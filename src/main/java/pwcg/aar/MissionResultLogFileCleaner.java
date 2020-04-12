package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;

public class MissionResultLogFileCleaner 
{
    private FileUtils fileUtils;
    private DirectoryReader directoryReader;

    public MissionResultLogFileCleaner(DirectoryReader directoryReader, FileUtils fileUtils)
    {
        this.directoryReader = directoryReader;
        this.fileUtils = fileUtils;
    }
    
    public int cleanMissionResultLogFiles() throws PWCGException 
    {
        List<String> filesToDelete = new ArrayList<String>();
        int deleteAll = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey);
        if (deleteAll == 1)
        {
        	List<String> allMissionFiles = getAllMissionResultsFiles();
        	filesToDelete = selectOlderMissionLogFiles(allMissionFiles);
            FileUtils.deleteFilesByFileName (filesToDelete);
        }
        
        return filesToDelete.size();
    }

    public  List<String> selectOlderMissionLogFiles(List<String> allMissionFiles) throws PWCGException 
    {
        List<String> filesToDelete = new ArrayList<String>();
        for (String pathname : allMissionFiles) 
        {
            long ageOfFile = FileUtils.ageOfFilesInMillis(pathname);
            long oneDayAgo = System.currentTimeMillis() - 86400000;
            if (ageOfFile < oneDayAgo)
            {
                filesToDelete.add(pathname);
            }
        }
        
        return filesToDelete;
    }

    private List<String> getAllMissionResultsFiles() throws PWCGException 
    {
        List<String> results = new ArrayList<String>();
        String simulatorDataDir = PWCGContext.getInstance().getDirectoryManager().getSimulatorDataDir();

        directoryReader.sortilesInDir(simulatorDataDir);
        for (String filename : directoryReader.getFiles()) 
        {
            if (filename.contains("missionReport") && filename.contains(".txt"))
            {
                String filepath = simulatorDataDir + filename;
                results.add(filepath);
            }
        }

        return results;
    }

    public FileUtils getFileUtils()
    {
        return fileUtils;
    }

    public void setFileUtils(FileUtils fileUtils)
    {
        this.fileUtils = fileUtils;
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


package pwcg.aar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class MissionFileCleaner 
{
    private DirectoryReader directoryReader = new DirectoryReader();
    private FileUtils fileUtils = new FileUtils();

    public int cleanMissionFiles() throws PWCGException 
    {
        List<File> filesToDelete = getMissionFilesToDelete();
        FileUtils.deleteFiles(filesToDelete);
        
        return filesToDelete.size();
    }

    private List<File> getMissionFilesToDelete() throws PWCGException 
    {
        String singlePlayerMissionDir = PWCGDirectorySimulatorManager.getInstance().getSinglePlayerMissionFilePath();
        List<File> singlePlayerFilesToDelete = deleteOldMissionFiles(singlePlayerMissionDir);

        String coopMissionDir = PWCGDirectorySimulatorManager.getInstance().getCoopMissionFilePath();
        List<File> coopFilesToDelete = deleteOldMissionFiles(coopMissionDir);
        
        List<File> filesToDelete = new ArrayList<>();
        filesToDelete.addAll(singlePlayerFilesToDelete);
        filesToDelete.addAll(coopFilesToDelete);
        
        return filesToDelete;
    }

    private List<File> deleteOldMissionFiles(String missionDir) throws PWCGException
    {
        List<File> filesToDelete = new ArrayList<>();
        
        directoryReader.sortFilesInDir(missionDir);
        if (!directoryReader.getFiles().isEmpty())
        {
            List<String> fileNames = getMissionFileNameRootsForCampaign(directoryReader.getFiles());
            selectFilesToDelete(fileNames);
            filesToDelete = addExtensions(missionDir, fileNames);
        }
        return filesToDelete;
    }

    private List<String> getMissionFileNameRootsForCampaign(List<File> filesInDirectory)
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        
        List<String> missionFilesForCampaign = new ArrayList<>();
        for (File file : filesInDirectory) 
        {
            try
            {
                PWCGLogger.log(LogLevel.DEBUG, "Campaign = " + campaign.getCampaignData().getName() + "    " + file);
                if (file.getName().contains(campaign.getCampaignData().getName()))
                {
                    if (file.getName().contains(".mission"))
                    {
                        int index = file.getName().indexOf(".mission");
                        String fileNameRoot = file.getName().substring(0, index);
                        missionFilesForCampaign.add(fileNameRoot);
                    }
                }
            }
            catch (Exception e)
            {
                PWCGLogger.logException(e);
            }
        }
        return missionFilesForCampaign;
    }

    private void selectFilesToDelete(List<String> fileNames) throws PWCGException, PWCGException
    {
        int numMissionFilesToSave = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.SaveOldMissionsKey);
        if (fileNames.size() == 0 || fileNames.size() <= numMissionFilesToSave)
        {
            fileNames.clear();
        }
        else
        {
            for (int j = 0; j < numMissionFilesToSave; ++j)
            {
                int last = fileNames.size() - 1;
                fileNames.remove(last);
            }
        }
    }	

    private List<File> addExtensions(String missionDir, List<String> fileNames)
    {
        List<File> filesToDelete = new ArrayList<>();

        for (String filename : fileNames)
        {
            filesToDelete.add(new File(missionDir + "\\" + filename + ".mission"));
            filesToDelete.add(new File(missionDir + "\\" + filename + ".msnbin"));
            filesToDelete.add(new File(missionDir + "\\" + filename + ".list"));
            filesToDelete.add(new File(missionDir + "\\" + filename + ".spa"));
            filesToDelete.add(new File(missionDir + "\\" + filename + ".eng"));
            filesToDelete.add(new File(missionDir + "\\" + filename + ".fra"));
            filesToDelete.add(new File(missionDir + "\\" + filename + ".rus"));
            filesToDelete.add(new File(missionDir + "\\" + filename + ".ger"));
            filesToDelete.add(new File(missionDir + "\\" + filename + ".pol"));
            filesToDelete.add(new File(missionDir + "\\" + filename + ".chs"));
        }
        return filesToDelete;
    }

    public FileUtils getFileUtils()
    {
        return fileUtils;
    }

    public void setFileUtils(FileUtils fileUtils)
    {
        this.fileUtils = fileUtils;
    }
}


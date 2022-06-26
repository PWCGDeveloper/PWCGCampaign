package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
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
    private Campaign campaign;
    private DirectoryReader directoryReader = new DirectoryReader();
    private FileUtils fileUtils = new FileUtils();
    
    public MissionFileCleaner(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public int cleanMissionFiles() throws PWCGException 
    {
        List<String> filesToDelete = getMissionFilesToDelete();
        FileUtils.deleteFilesByFileName(filesToDelete);
        
        return filesToDelete.size();
    }

    private List<String> getMissionFilesToDelete() throws PWCGException 
    {
        List<String> results = new ArrayList<String>();

        String singlePlayerMissionDir = PWCGDirectorySimulatorManager.getInstance().getSinglePlayerMissionFilePath();
        deleteOldMissionFiles(results, singlePlayerMissionDir);

        String coopMissionDir = PWCGDirectorySimulatorManager.getInstance().getCoopMissionFilePath();
        deleteOldMissionFiles(results, coopMissionDir);
        
        return results;
    }

    private void deleteOldMissionFiles(List<String> results, String missionDir) throws PWCGException
    {
        directoryReader.sortFilesInDir(missionDir);
        if (!directoryReader.getFiles().isEmpty())
        {
            List<String> fileNames = getMissionFilesForCampaign(directoryReader.getFiles());
            selectFilesToDelete(fileNames);
            addExtensions(results, missionDir, fileNames);
        }
    }

    private List<String> getMissionFilesForCampaign(List<String> filesInDirectory)
    {
        List<String> missionFilesForCampaign = new ArrayList<String>();

        for (String filename : filesInDirectory) 
        {
            try
            {
                PWCGLogger.log(LogLevel.DEBUG, "Campaign = " + campaign.getCampaignData().getName() + "    " + filename);
                if (filename.contains(campaign.getCampaignData().getName()))
                {
                    if (filename.contains(".mission"))
                    {
                        int index = filename.indexOf(".mission");
                        filename = filename.substring(0, index);
                        missionFilesForCampaign.add(filename);
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

    private void addExtensions(List<String> results, String missionDir, List<String> fileNames)
    {
        for (String filename : fileNames)
        {
            results.add(missionDir + "\\" + filename + ".mission");
            results.add(missionDir + "\\" + filename + ".msnbin");
            results.add(missionDir + "\\" + filename + ".list");
            results.add(missionDir + "\\" + filename + ".spa");
            results.add(missionDir + "\\" + filename + ".eng");
            results.add(missionDir + "\\" + filename + ".fra");
            results.add(missionDir + "\\" + filename + ".rus");
            results.add(missionDir + "\\" + filename + ".ger");
            results.add(missionDir + "\\" + filename + ".pol");
            results.add(missionDir + "\\" + filename + ".chs");
        }
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


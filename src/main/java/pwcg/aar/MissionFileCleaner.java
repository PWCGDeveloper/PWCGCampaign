package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class MissionFileCleaner 
{
    private DirectoryReader directoryReader = new DirectoryReader();
    private FileUtils fileUtils = new FileUtils();

    public int cleanMissionFiles() throws PWCGException 
    {
        List<String> filesToDelete = getMissionFilesToDelete();
        fileUtils.deleteFilesByFileName(filesToDelete);
        
        return filesToDelete.size();
    }

    private List<String> getMissionFilesToDelete() throws PWCGException 
    {
        List<String> results = new ArrayList<String>();

        String missionDir = PWCGContext.getInstance().getDirectoryManager().getSimulatorDataDir() + "Missions\\";
        directoryReader.sortilesInDir(missionDir);
        if (!directoryReader.getFiles().isEmpty())
        {
            List<String> fileNames = getMissionFilesForCampaign(directoryReader.getFiles());
            selectFilesToDelete(fileNames);
            addExtensions(results, missionDir, fileNames);
        }
        
        return results;
    }

    private List<String> getMissionFilesForCampaign(List<String> filesInDirectory)
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        
        List<String> missionFilesForCampaign = new ArrayList<String>();

        for (String filename : filesInDirectory) 
        {
            try
            {
                Logger.log(LogLevel.DEBUG, "Campaign = " + campaign.getCampaignData().getName() + "    " + filename);
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
                Logger.logException(e);
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


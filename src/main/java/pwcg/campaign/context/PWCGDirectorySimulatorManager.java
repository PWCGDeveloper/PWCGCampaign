package pwcg.campaign.context;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class PWCGDirectorySimulatorManager
{
    private String simulatorRootDir = "";

    private static PWCGDirectorySimulatorManager instance = new PWCGDirectorySimulatorManager();
    
    public static PWCGDirectorySimulatorManager getInstance()
    {
        if (instance.simulatorRootDir.isEmpty())
        {
            instance.createSimulatorDir();
        }
        
        return instance;
    }

    private void createSimulatorDir()
    {
        String userDir = System.getProperty("user.dir");
        File simulatorDir = new File(userDir).getParentFile();
        simulatorRootDir = simulatorDir.getAbsolutePath() + "\\";
    }

    public String getMissionFilePath(Campaign campaign) throws PWCGException
    {
        String filepath = getSinglePlayerMissionFilePath();
        if (campaign.isCoop())
        {
            filepath = getCoopMissionFilePath();
        }

        return filepath;
    }

    public String getSinglePlayerMissionFilePath() throws PWCGException
    {
        String filepath = getSimulatorDataDir() + "Missions\\PWCG\\";
        FileUtils.createDirIfNeeded(filepath);
        return filepath;
    }

    public String getCoopMissionFilePath() throws PWCGException
    {
        String filepath = getSimulatorDataDir() + "Multiplayer\\Cooperative\\";
        FileUtils.createDirIfNeeded(filepath);
        return filepath;
    }

    public String getMissionBinPath() throws PWCGException
    {
        String filepath = getSimulatorRootDir() + "bin\\";
        return filepath;
    }

    public String getMissionRewritePath() throws PWCGException
    {
        String filepath = getSimulatorRootDir() + "bin\\resaver\\";
        return filepath;
    }

    public String getSimulatorRootDir()
    {
        return simulatorRootDir;
    }

    public String getSimulatorDataDir()
    {
        return simulatorRootDir + "data\\";
    }

    public String getSkinsDir()
    {
        return getSimulatorDataDir() + "graphics\\skins\\";
    }

}

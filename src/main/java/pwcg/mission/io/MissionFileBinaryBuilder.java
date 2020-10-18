package pwcg.mission.io;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.dialogs.HelpDialog;

public class MissionFileBinaryBuilder implements buildCommandPath
{
    public static void buildMissionBinaryFile(Campaign campaign, String fileName) throws PWCGException, InterruptedException
    {
        String fullCommand = "";
        try
        {
            if (canRunResaver())
            {
                fullCommand = createCommandPath(campaign, fileName);
                buildBinaryFile(fullCommand);
            }
        }
        catch (PWCGException pwcge)
        {
            new  HelpDialog(pwcge.getMessage());
        }
        catch (Exception e)
        {
            new  HelpDialog("Failed to create binary mission file for " + fullCommand);
        }
    }

    private static String createCommandPath(Campaign campaign, String fileName) throws PWCGException
    {
        String resaverExe = formResaverExeCommand();
        
        String listFileArg = formListFileArg();
        String dataDirArg = formDataDirArg(campaign);
        String missionFilePathArg = formMissionFilePathArg(campaign, fileName);

        String fullCommand = resaverExe + " " + listFileArg + " " + dataDirArg + " " + missionFilePathArg;
        PWCGLogger.log(LogLevel.INFO, fullCommand);
        return fullCommand;
    }
    
    private static void buildBinaryFile(String fullCommand) throws PWCGException
    {
        try
        {
            File workingDir = new File(PWCGDirectorySimulatorManager.getInstance().getMissionRewritePath());
            Process process = Runtime.getRuntime().exec(fullCommand, null, workingDir);
            int binaryBuildTimeout = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.BuildBinaryTimeoutKey);
            boolean status = process.waitFor(binaryBuildTimeout, TimeUnit.MINUTES);
            if (status == true)
            {
                PWCGLogger.log(LogLevel.INFO, "Succeeded creating binary mission file for: " + fullCommand);
            }
            else
            {
                PWCGLogger.log(LogLevel.INFO, "Failed to create binary mission file for: " + fullCommand);
                new  HelpDialog("Failed to create binary mission file for " + fullCommand);
            }
        }
        catch (IOException ioe)
        {
            new  HelpDialog("Failed to create binary mission file for " + fullCommand);
        }
        catch (InterruptedException ioe)
        {
            new  HelpDialog("Timed out trying to create binary mission file for " + fullCommand);
        }
    }

    private static String formListFileArg()
    {
        return " -t ";
    }

    public static boolean canRunResaver() throws PWCGException
    {
        String binPath = PWCGDirectorySimulatorManager.getInstance().getMissionBinPath();
        if (!FileUtils.findInDirectory(binPath, "resaver"))
        {
            return false;
        }
        
        String resaverPath = PWCGDirectorySimulatorManager.getInstance().getMissionRewritePath();
        if (!FileUtils.findInDirectory(resaverPath, "MissionResaver.exe"))
        {
            return false;
        }

        return true;
    }

    private static String formResaverExeCommand() throws PWCGException
    {
        String resaverExe = PWCGDirectorySimulatorManager.getInstance().getMissionRewritePath() + "MissionResaver.exe";
        resaverExe = "\"" + resaverExe + "\"";
        return resaverExe;
    }

    private static String formDataDirArg(Campaign campaign) throws PWCGException
    {
        String dataDir = PWCGDirectorySimulatorManager.getInstance().getSimulatorDataDir();
        if (dataDir.endsWith("\\"))
        {
            dataDir = dataDir.substring(0, dataDir.length() - 1);
        }

        String dataDirArg = " -d \"" + dataDir + "\"";
        return dataDirArg;
    }

    private static String formMissionFilePathArg(Campaign campaign, String fileName) throws PWCGException
    {
        String filepath = PWCGDirectorySimulatorManager.getInstance().getMissionFilePath(campaign);
        filepath = filepath + fileName + ".mission";
        String missionFilePathArg = " -f \"" + filepath + "\"";
        return missionFilePathArg;
    }

}

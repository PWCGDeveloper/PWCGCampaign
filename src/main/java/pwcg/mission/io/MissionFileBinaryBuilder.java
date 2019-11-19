package pwcg.mission.io;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.gui.dialogs.HelpDialog;

public class MissionFileBinaryBuilder implements buildCommandPath
{
    public static void buildMissionBinaryFile(Campaign campaign, String fileName) throws PWCGException, InterruptedException
    {
        String fullCommand = "";
        try
        {
            fullCommand = createCommandPath(campaign, fileName);
            buildBinaryFile(fullCommand);
            removeListFile(campaign, fileName);
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
        String missionDirArg = formMissionDirArg(campaign);
        String missionFilePathArg = formMissionFilePathArg(campaign, fileName);

        String fullCommand = resaverExe + " " + listFileArg + " " + missionDirArg + " " + missionFilePathArg;
        Logger.log(LogLevel.INFO, fullCommand);
        return fullCommand;
    }
    
    private static void buildBinaryFile(String fullCommand)
    {
        try
        {
            Process process = Runtime.getRuntime().exec(fullCommand);
            boolean status = process.waitFor(5, TimeUnit.MINUTES);
            if (status == true)
            {
                Logger.log(LogLevel.INFO, "Succeeded creating binary mission file for: " + fullCommand);
            }
            else
            {
                Logger.log(LogLevel.INFO, "Failed to create binary mission file for: " + fullCommand);
                new  HelpDialog("Failed to create binary mission file for " + fullCommand);
            }
        }
        catch (IOException ioe)
        {
            new  HelpDialog("Failed to create binary mission file for " + fullCommand);
        }
        catch (InterruptedException ioe)
        {
            new  HelpDialog("Timesd out trying to create binary mission file for " + fullCommand);
        }
    }

    private static void removeListFile(Campaign campaign, String fileName) throws PWCGException
    {
        String filepath = PWCGContext.getInstance().getDirectoryManager().getMissionFilePath(campaign);
        filepath = filepath + fileName + ".list";
        File listFile = new File(filepath);
        listFile.delete();
    }

    private static String formListFileArg()
    {
        return " -t ";
    }

    private static String formResaverExeCommand() throws PWCGException
    {
        String resaverExe = PWCGContext.getInstance().getDirectoryManager().getMissionRewritePath() + "MissionResaver.exe";
        resaverExe = "\"" + resaverExe + "\"";
        return resaverExe;
    }

    private static String formMissionDirArg(Campaign campaign) throws PWCGException
    {
        String missionDir = PWCGContext.getInstance().getDirectoryManager().getMissionFilePath(campaign);
        if (missionDir.endsWith("\\"))
        {
            missionDir = missionDir.substring(0, missionDir.length() - 1);
        }

        String missionDirArg = " -d \"" + missionDir + "\"";
        return missionDirArg;
    }

    private static String formMissionFilePathArg(Campaign campaign, String fileName) throws PWCGException
    {
        String filepath = PWCGContext.getInstance().getDirectoryManager().getMissionFilePath(campaign);
        filepath = filepath + fileName + ".mission";
        String missionFilePathArg = " -f \"" + filepath + "\"";
        return missionFilePathArg;
    }

}

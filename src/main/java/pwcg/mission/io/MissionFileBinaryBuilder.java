package pwcg.mission.io;

import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class MissionFileBinaryBuilder
{
    public static void buildMissionBinaryFile(Campaign campaign, String fileName) throws PWCGException, InterruptedException
    {
        String resaverExe = formResaverExeCommand();
        String missionDirArg = formMissionDirArg(campaign);
        String missionFilePathArg = formMissionFilePathArg(campaign, fileName);

        String fullCommand = resaverExe + " " + missionDirArg + " " + missionFilePathArg;
        Logger.log(LogLevel.INFO, fullCommand);
        try
        {            
            Process process = Runtime.getRuntime().exec(fullCommand);
            int status = process.waitFor();
            if (status == 0)
            {
                Logger.log(LogLevel.INFO, "Succeeded creating binary mission file for: " + fullCommand);
            }
            else
            {
                Logger.log(LogLevel.INFO, "Failed to create binary mission file for: " + fullCommand);
                throw new PWCGException("Failed to create binary mission file for: " + fullCommand);
            }
        }
        catch (IOException ioe)
        {
            throw new PWCGException("Error starting resaver " + ioe.getMessage());
        }
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
        filepath = filepath + fileName +  ".mission";
        String missionFilePathArg = " -f \"" + filepath + "\"";
        return missionFilePathArg;
    }

}

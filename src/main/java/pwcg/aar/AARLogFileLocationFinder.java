package pwcg.aar;

import java.io.File;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.core.exception.PWCGException;

public class AARLogFileLocationFinder
{
    public static String determineLogFileLocation(String rootLogFileName) throws PWCGException
    {
        String simulatorDataDir = PWCGDirectorySimulatorManager.getInstance().getSimulatorDataDir();
        File fileInSimulatorDir = new File(simulatorDataDir + rootLogFileName);
        if (fileInSimulatorDir.exists())
        {
            return simulatorDataDir;
        }
        
        String userLogDir = PWCGContext.getInstance().getMissionLogDirectory();
        if (!userLogDir.isEmpty())
        {
            File fileInUserLogFileDirDir = new File(userLogDir + rootLogFileName);
            if (fileInUserLogFileDirDir.exists())
            {
                return userLogDir;
            }
        }

        return simulatorDataDir;
    }

}

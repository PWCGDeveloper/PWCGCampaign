package pwcg.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import pwcg.campaign.context.PWCGDirectorySimulatorManager;

public class MissionLogFileValidator 
{
    private DirectoryReader directoryReader = new DirectoryReader();
    
    private boolean missionLoggingEnabled = false;
    private String missionLogPath = "";

 	public MissionLogFileValidator()
	{
	}

	public void analyzeStartupCfg()
	{
		try
		{
		    String simulatorDataDir = PWCGDirectorySimulatorManager.getInstance().getSimulatorDataDir();
            directoryReader.sortilesInDir(simulatorDataDir);
            for (String filename : directoryReader.getFiles()) 
            {
                if (filename.toLowerCase().contains("startup.cfg"))
                {
                    String configFilePath = simulatorDataDir + filename;
                    analyzeFileContents(configFilePath);
                }
            }
		}
		catch (Exception e)
		{
			 PWCGLogger.logException(e);
		}
		
	}

    private void analyzeFileContents(String configFilePath) 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath)))
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                if (line.contains("mission_text_log"))
                {
                    determineLoggingEnabled(line);
                }
                
                if (line.contains("text_log_folder"))
                {
                    parseMissionLogPath(line);
                }
            }
        }
        catch (Exception e)
        {
             PWCGLogger.logException(e);
        }        
    }

    private void determineLoggingEnabled(String line)
    {
        if (line.contains("= 1"))
        {
            missionLoggingEnabled = true;
        }
    }


    private void parseMissionLogPath(String line) 
    {
        String delimiter = "\"";
        int startPos = line.indexOf(delimiter) + 1;
        if (startPos > 0)
        {
            int endPos = line.substring(startPos).indexOf(delimiter) + startPos;
            if (endPos > startPos)
            {
                String simulatorDataDir = PWCGDirectorySimulatorManager.getInstance().getSimulatorDataDir();
                missionLogPath = simulatorDataDir + line.substring(startPos, endPos);
                makeMissionLogPathIfNeeded(missionLogPath);
            }
        }
    }
    
    private void makeMissionLogPathIfNeeded(String missionLogPath)
    {
        File missionLogPathFile = new File(missionLogPath);
        if (!missionLogPathFile.exists() || !missionLogPathFile.isDirectory())
        {
            missionLogPathFile.mkdirs();
        }
    }

    public boolean isMissionLoggingEnabled()
    {
        return missionLoggingEnabled;
    }

    public String getMissionLogPath()
    {
        return missionLogPath;
    }
}
	

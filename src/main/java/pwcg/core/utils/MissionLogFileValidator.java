package pwcg.core.utils;

import java.io.BufferedReader;
import java.io.FileReader;

import pwcg.campaign.context.PWCGDirectoryManager;

public class MissionLogFileValidator 
{
    private DirectoryReader directoryReader = new DirectoryReader();

 	public MissionLogFileValidator()
	{
	}

	public boolean validateMissionLogsEnabled()
	{
		try
		{
		    String simulatorDataDir = PWCGDirectoryManager.getInstance().getSimulatorDataDir();
            directoryReader.sortilesInDir(simulatorDataDir);
            for (String filename : directoryReader.getFiles()) 
            {
                if (filename.toLowerCase().contains("startup.cfg"))
                {
                    String configFilePath = simulatorDataDir + filename;
                    return validateMissionLogParam(configFilePath);
                }
            }
		}
		catch (Exception e)
		{
			 Logger.logException(e);
		}
		
		return false;
	}

	private boolean validateMissionLogParam(String configFilePath) 
	{
		boolean missionLoggingEnabled = false;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath)))
		{
			String line;
			while ((line = reader.readLine()) != null) 
			{
				if (line.contains("mission_text_log"))
				{
					if (line.contains("= 1"))
					{
						missionLoggingEnabled = true;
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			 Logger.logException(e);
		}
		
		return missionLoggingEnabled;
	}
}
	

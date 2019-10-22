package pwcg.dev.deploy;

import java.io.File;
import java.util.HashMap;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class FCDeploy extends DeployBase
{
	static String sourceRootDir = "E:\\PWCG\\workspacePWCGGradle\\PWCGCampaign";
	static String sourceRootGuiModDir = "E:\\PWCG\\workspacePWCGGradle\\GuiMod\\interface\\data";
	static String targetDir = "E:\\PWCG\\FCDeploy";
	
		
	
	static boolean reallyDoDeploy = true;

	static public void main (String[] args)
	{
        if (!reallyDoDeploy)
        {
            Logger.log(LogLevel.ERROR, "************  NO DEPLOY  **********");
            return;
        }
        
        if (TestDriver.getInstance().isEnabled())
        {
            Logger.log(LogLevel.ERROR, "************  NO DEPLOY - TEST DRIVER ENABLED  **********");
            return;
        }
        
		FCDeploy deploy = new FCDeploy();
		try
		{
			deleteExistingDeploy(targetDir);
 
			File targetRoot = new File(targetDir);
			File sourceRoot = new File(sourceRootDir);
			File sourceRootGuiMod = new File(sourceRootGuiModDir);

			deploy.cleanUnwanted(targetRoot);
			
			HashMap<String, Object> unwantedFiles = deploy.loadUnwantedFiles();
			HashMap<String, Object> unwantedFileTypes = deploy.loadUnwantedFileTypes();
			
			HashMap<String, Object> directoriesToCopy = deploy.loadDirectoriesToCopyPWCG();
			HashMap<String, Object> directoriesToMake = deploy.loadDirectoriesToMakePWCG();
			deploy.copyDirectory(sourceRoot, targetRoot, directoriesToCopy, directoriesToMake, unwantedFiles, unwantedFileTypes);
			
			
			HashMap<String, Object> directoriesToCopyGuiMod = deploy.loadDirectoriesToCopyGuiMod();
			HashMap<String, Object> directoriesToMakeGuiMod = deploy.loadDirectoriesToMakeGuiMod();
			deploy.copyDirectory(sourceRootGuiMod, targetRoot, directoriesToCopyGuiMod, directoriesToMakeGuiMod, unwantedFiles, unwantedFileTypes);
			
			Logger.log(LogLevel.INFO, "************  DONE  **********");
		}
		catch (Exception e)
		{
			Logger.log(LogLevel.ERROR, e.getMessage());
		}
	}

	public FCDeploy()
	{
	}

	protected HashMap<String, Object> loadDirectoriesToMakePWCG() 
	{
		// Directories to make
		HashMap<String, Object> directoriesToMake = new HashMap<String, Object>();
		directoriesToMake.put("Campaigns", null);
		directoriesToMake.put("Report", null);
		directoriesToMake.put("User", null);
        directoriesToMake.put("Personnel", null);

		return directoriesToMake;
	}

	protected HashMap<String, Object> loadDirectoriesToCopyPWCG() 
	{		
		super.loadDirectoriesToCopyPWCG();
		
        directoriesToCopy.put("FCData", null);

        // Service specific pilot images
        directoriesToCopy.put("GAS", null);
        directoriesToCopy.put("RFC", null);
        directoriesToCopy.put("RNAS", null);

        // Maps
        directoriesToCopy.put("Arras", null);

		// Moving Front Dates
    	directoriesToCopy.put("19170801", null);
        
		// From Input/Aces
		directoriesToCopy.put("Pictures", null);
		
		return directoriesToCopy;
	}

	protected HashMap<String, Object> loadDirectoriesToMakeGuiMod() 
	{
		// No directories to make
		HashMap<String, Object> directoriesToMake = new HashMap<String, Object>();
		
		return directoriesToMake;
	}

	protected HashMap<String, Object> loadUnwantedFiles() 
	{
		// No directories to make
		HashMap<String, Object> unwantedFiles = new HashMap<String, Object>();
		
		
		unwantedFiles.put(".classpath", null);
		unwantedFiles.put(".project", null);
		unwantedFiles.put("CopyImageFile.bat", null);
		unwantedFiles.put("PWCGBoS.ico", null);
		unwantedFiles.put("TODO.txt", null);

		return unwantedFiles;
	}

	protected HashMap<String, Object> loadDirectoriesToCopyGuiMod() 
	{
		HashMap<String, Object> directoriesToCopy = new HashMap<String, Object>();
		
        directoriesToCopy.put("data", null);
        directoriesToCopy.put("swf", null);
		
		return directoriesToCopy;
	}

}

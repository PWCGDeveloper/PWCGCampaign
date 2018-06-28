package pwcg.dev.deploy;

import java.io.File;
import java.util.HashMap;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class RoFDeploy extends DeployBase
{
	static String sourceRootDir = "E:\\PWCG\\workspacePWCGGradle\\PWCGCampaign";
	static String sourceRootGuiModDir = "E:\\PWCG\\workspacePWCGGradle\\GuiMod\\interface\\data";
	static String targetDir = "E:\\PWCG\\RoFDeploy";
	
		
	
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
        
		RoFDeploy deploy = new RoFDeploy();
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

	public RoFDeploy()
	{
	}


	/**
	 * Directories to make - we make the directory without copying the contents
	 * 
	 * @return
	 */
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

	/**
	 * Directories to copy - we copy the contents
	 * 
	 * @return
	 */
	protected HashMap<String, Object> loadDirectoriesToCopyPWCG() 
	{		
		super.loadDirectoriesToCopyPWCG();
		
        directoriesToCopy.put("RoFData", null);

        // Service specific pilot images
        directoriesToCopy.put("GAS", null);
        directoriesToCopy.put("MFJ", null);
        directoriesToCopy.put("RFC", null);
        directoriesToCopy.put("RNAS", null);

        // Maps
        directoriesToCopy.put("France", null);
        directoriesToCopy.put("Channel", null);
        directoriesToCopy.put("Galicia", null);

		// Moving Front Dates
    	directoriesToCopy.put("19160101", null);
        directoriesToCopy.put("19180326", null);
        directoriesToCopy.put("19180404", null);
        directoriesToCopy.put("19180419", null);
        directoriesToCopy.put("19180429", null);
        directoriesToCopy.put("19180604", null);
        directoriesToCopy.put("19180612", null);
        directoriesToCopy.put("19180717", null);
        directoriesToCopy.put("19180925", null);
        directoriesToCopy.put("19181005", null);
        directoriesToCopy.put("19181015", null);
        directoriesToCopy.put("19181025", null);
        directoriesToCopy.put("19181105", null);
        directoriesToCopy.put("19181110", null);
        
		// From Input/Aces
		directoriesToCopy.put("Pictures", null);
		
		return directoriesToCopy;
	}


	/**
	 * Directories to make - we make the directory without copying the contents
	 * 
	 * @return
	 */
	protected HashMap<String, Object> loadDirectoriesToMakeGuiMod() 
	{
		// No directories to make
		HashMap<String, Object> directoriesToMake = new HashMap<String, Object>();
		
		return directoriesToMake;
	}
	

	/**
	 * Project files that should not be deployed
	 * 
	 * @return
	 */
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


	/**
	 * Directories to copy - we copy the contents
	 * 
	 * @return
	 */
	protected HashMap<String, Object> loadDirectoriesToCopyGuiMod() 
	{
		HashMap<String, Object> directoriesToCopy = new HashMap<String, Object>();
		
        directoriesToCopy.put("data", null);
        directoriesToCopy.put("swf", null);
		
		return directoriesToCopy;
	}

}

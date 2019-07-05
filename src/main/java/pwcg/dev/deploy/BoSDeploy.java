package pwcg.dev.deploy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class BoSDeploy extends DeployBase
{
	static String sourceRootDir = "D:\\PWCG\\workspacePwcg6\\PWCGCampaign";
	static String targetDir = "D:\\PWCG\\BosDeploy";
	
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
        
		BoSDeploy deploy = new BoSDeploy();
		try
		{
			deleteExistingDeploy(targetDir);
 
			File targetRoot = new File(targetDir);
			File sourceRoot = new File(sourceRootDir);

			deploy.cleanUnwanted(targetRoot);
			
			Map<String, Object> unwantedFiles = deploy.loadUnwantedFiles();
			Map<String, Object> unwantedFileTypes = deploy.loadUnwantedFileTypes();
			
			Map<String, Object> directoriesToCopy = deploy.loadDirectoriesToCopyPWCG();
			Map<String, Object> directoriesToMake = deploy.loadDirectoriesToMakePWCG();
			deploy.copyDirectory(sourceRoot, targetRoot, directoriesToCopy, directoriesToMake, unwantedFiles, unwantedFileTypes);
			
			Logger.log(LogLevel.INFO, "************  DONE  **********");
		}
		catch (Exception e)
		{
			Logger.log(LogLevel.ERROR, e.getMessage());
		}
	}

	public BoSDeploy()
	{
	}


	/**
	 * Directories to copy - we copy the contents
	 * 
	 * @return
	 */
	protected HashMap<String, Object> loadDirectoriesToCopyPWCG() 
	{		
		super.loadDirectoriesToCopyPWCG();

        directoriesToCopy.put("BoSData", null);
        directoriesToCopy.put("Coop", null);

        // Maps
        directoriesToCopy.put("Stalingrad", null);
        directoriesToCopy.put("Moscow", null);
        directoriesToCopy.put("Kuban", null);
        directoriesToCopy.put("Bodenplatte", null);        

		// Moscow dates
        directoriesToCopy.put("19411001", null);
        directoriesToCopy.put("19411020", null);
        directoriesToCopy.put("19411110", null);
        directoriesToCopy.put("19411120", null);
        directoriesToCopy.put("19411215", null);
        directoriesToCopy.put("19420110", null);
        
		// Stalingrad dates
        directoriesToCopy.put("19420301", null);
        directoriesToCopy.put("19420801", null);
        directoriesToCopy.put("19420906", null);
        directoriesToCopy.put("19421011", null);
        directoriesToCopy.put("19421123", null);
        directoriesToCopy.put("19421223", null);
        directoriesToCopy.put("19430120", null);
        
		// Kuban dates
        directoriesToCopy.put("19420601", null);
        directoriesToCopy.put("19420624", null);
        directoriesToCopy.put("19420709", null);
        directoriesToCopy.put("19420721", null);
        directoriesToCopy.put("19430301", null);
        directoriesToCopy.put("19430330", null);
        directoriesToCopy.put("19430418", null);
        directoriesToCopy.put("19430918", null);
        directoriesToCopy.put("19430927", null);
        directoriesToCopy.put("19431004", null);
        directoriesToCopy.put("19431008", null);

        // Bodenplatte dates
        directoriesToCopy.put("19440801", null);

		return directoriesToCopy;
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
		unwantedFiles.put("PWCGRoF.ico", null);
		unwantedFiles.put("TODO.txt", null);

		return unwantedFiles;
	}

	
	/**
	 * Directories to make - we make the directory without copying the contents
	 * 
	 * @return
	 */
	protected Map<String, Object> loadDirectoriesToMakePWCG() 
	{
		// Directories to make
		Map<String, Object> directoriesToMake = new TreeMap<String, Object>();
		directoriesToMake.put("Campaigns", null);
		directoriesToMake.put("Report", null);
        directoriesToMake.put("User", null);
        directoriesToMake.put("Personnel", null);
        directoriesToMake.put("Pilots", null);
        directoriesToMake.put("Users", null);
		
		return directoriesToMake;
	}

}

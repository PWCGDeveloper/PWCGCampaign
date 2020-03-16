package pwcg.dev.deploy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public abstract class DeployBase 
{
	protected HashMap<String, Object> directoriesToCopy = new HashMap<String, Object>();
	protected HashMap<String, Object> unwantedFiles = new HashMap<String, Object>();

	protected String sourceRootDir = "D:\\PWCG\\workspacePwcg\\PWCGCampaign";
	protected String deployDir = "D:\\PWCG\\Deploy";
	protected String targetDir = "D:\\PWCG\\Deploy\\PWCGCampaign";
	protected String targetFinalDir = "";

    static boolean reallyDoDeploy = true;

    public void doDeploy()
    {
        if (!reallyDoDeploy)
        {
            PWCGLogger.log(LogLevel.ERROR, "************  NO DEPLOY  **********");
            return;
        }
        
        if (TestDriver.getInstance().isEnabled())
        {
            PWCGLogger.log(LogLevel.ERROR, "************  NO DEPLOY - TEST DRIVER ENABLED  **********");
            return;
        }
        
        try
        {
            deleteExistingDeploy(targetFinalDir);
 
            File deployRoot = new File(deployDir);
            File sourceRoot = new File(sourceRootDir);
            File target = new File(targetDir);
            File targetFinal = new File(targetFinalDir);

            cleanUnwanted(deployRoot);
            
            HashMap<String, Object> unwantedFiles = loadUnwantedFiles();
            HashMap<String, Object> unwantedFileTypes = loadUnwantedFileTypes();
             
            Map<String, Object> directoriesToCopy = loadDirectoriesToCopyPWCG();
            Map<String, Object> directoriesToMake = loadDirectoriesToMakePWCG();
            copyDirectory(sourceRoot, deployRoot, directoriesToCopy, directoriesToMake, unwantedFiles, unwantedFileTypes);
            
            target.renameTo(targetFinal);

            PWCGLogger.log(LogLevel.INFO, "************  DONE  **********");
        }
        catch (Exception e)
        {
            PWCGLogger.log(LogLevel.ERROR, e.getMessage());
        }
    }

    public void cleanUnwanted(File dirToClean)
    throws IOException 
    {
        String sourceDirName = dirToClean.getName();
        PWCGLogger.log(LogLevel.INFO, "Directory being cleaned is " + sourceDirName);

        File[] files = dirToClean.listFiles();
        
        if (files != null)
        {
	        for (int n = 0; n < files.length; ++n) 
	        {
	            if (files[n].getName().contains(".hprof"))
	            {
	                PWCGLogger.log(LogLevel.INFO, "Remove file " + files[n].getName());
	                files[n].delete();
	            }
	        }
        }
    }

	protected void copyDirectory(
			File source, 
			File destination, 
			Map<String, Object> directoriesToCopy, 
			Map<String, Object> directoriesToMake,
			Map<String, Object> filesToxclude,
			Map<String, Object> fileTypesToxclude) throws IOException 
	{
		File nextDirectory = new File(destination, source.getName());
		String sourceDirName = source.getName();
		PWCGLogger.log(LogLevel.INFO, "Directory being considered is " + sourceDirName);
		if (directoriesToMake.containsKey(sourceDirName)|| directoriesToCopy.containsKey(sourceDirName))
		{
			//
			// create the directory if necessary...
			//
			PWCGLogger.log(LogLevel.INFO, "Directory being made is " + sourceDirName);
			if (!nextDirectory.exists() && !nextDirectory.mkdirs()) 
			{
	 			String message = "DirCopyFailed: " + nextDirectory.getAbsolutePath() ;
				throw new IOException(message);
			}

			if (directoriesToCopy.containsKey(sourceDirName))
			{
				PWCGLogger.log(LogLevel.INFO, "Directory being copied is " + sourceDirName);
				File[] files = source.listFiles();
				
				for (int n = 0; n < files.length; ++n) 
				{
					if (files[n].isDirectory())
					{
						copyDirectory(files[n], nextDirectory, directoriesToCopy, directoriesToMake, filesToxclude, fileTypesToxclude);
					}
					else
					{
						boolean wanted = true;
						if (filesToxclude.containsKey(files[n].getName()))
						{
							wanted = false;
						}
						
						for (String unwantedFileType : fileTypesToxclude.keySet())
						{
							if (files[n].getName().endsWith(unwantedFileType))
							{
								wanted = false;
							}
						}

						if (wanted)
						{
						    FileUtils fileUtils = new FileUtils();
						    fileUtils.copyFile(files[n], nextDirectory);
						}
					}
				}
			}
		}
		else
		{
			PWCGLogger.log(LogLevel.INFO, "Directory not being copied is " + sourceDirName);
		}
	}

	protected HashMap<String, Object> loadUnwantedFileTypes() 
	{
		// No directories to make
		HashMap<String, Object> unwantedFileTypes = new HashMap<String, Object>();
		
		
        unwantedFileTypes.put(".zip", null);
		unwantedFileTypes.put(".xcf", null);
		unwantedFileTypes.put(".7z", null);
		unwantedFileTypes.put(".bat", null);
		unwantedFileTypes.put(".gradle", null);
        unwantedFileTypes.put("gradlew", null);
        unwantedFileTypes.put(".gitattributes", null);
        unwantedFileTypes.put(".gitignore", null);
        unwantedFileTypes.put(".ant", null);
		unwantedFileTypes.put(".classpath", null);
		unwantedFileTypes.put(".project", null);

		return unwantedFileTypes;
	}
	
	protected HashMap<String, Object> loadDirectoriesToCopyPWCG() 
	{		
        directoriesToCopy.put("PWCGCampaign", null);

        // From Data
        directoriesToCopy.put("Images", null);
        directoriesToCopy.put("Input", null);
        directoriesToCopy.put("Names", null);

		// From Images
        directoriesToCopy.put("Maps", null);
        directoriesToCopy.put("Medals", null);
        directoriesToCopy.put("Menus", null);
        directoriesToCopy.put("Misc", null);
        directoriesToCopy.put("National", null);
        directoriesToCopy.put("Newspaper", null);
        directoriesToCopy.put("PilotPictures", null);
        directoriesToCopy.put("Planes", null);
        directoriesToCopy.put("Profiles", null);

		// From Images - subdirectories
        directoriesToCopy.put("German", null);
        directoriesToCopy.put("Russian", null);
        directoriesToCopy.put("Italian", null);
        directoriesToCopy.put("French", null);
        directoriesToCopy.put("British", null);
        directoriesToCopy.put("Belgian", null);
        directoriesToCopy.put("Austrian", null);
        directoriesToCopy.put("American", null);

        directoriesToCopy.put("Main", null);
        directoriesToCopy.put("Generic", null);

        // Under Medals
		directoriesToCopy.put("Allied", null);
        directoriesToCopy.put("Axis", null);
		
        // From Input
        directoriesToCopy.put("Aces", null);
        directoriesToCopy.put("Aircraft", null);
        directoriesToCopy.put("Configuration", null);
        directoriesToCopy.put("Ranks", null);
        directoriesToCopy.put("Skins", null);
        directoriesToCopy.put("Squadron", null);
        directoriesToCopy.put("StaticObjects", null);
        directoriesToCopy.put("Vehicles", null);
        
        // From Input/Aces
        directoriesToCopy.put("Pictures", null);
        
        // From Input/Skins
        directoriesToCopy.put("Configured", null);

		return directoriesToCopy;
	}

    protected HashMap<String, Object> loadUnwantedFiles() 
    {
        unwantedFiles.put(".classpath", null);
        unwantedFiles.put(".project", null);
        unwantedFiles.put("CopyImageFile.bat", null);
        unwantedFiles.put("TODO.txt", null);
        unwantedFiles.put("PWCGErrorLog.txt", null);
        unwantedFiles.put("LICENSE", null);

        return unwantedFiles;
    }

	protected static void deleteExistingDeploy(String targetDir) {
		FileUtils fileUtils = new FileUtils();
		fileUtils.deleteRecursive(targetDir);
	}


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

package pwcg.dev.deploy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pwcg.core.utils.FileUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public abstract class DeployBase 
{
	protected HashMap<String, Object> directoriesToCopy = new HashMap<String, Object>();

    public void cleanUnwanted(File dirToClean)
    throws IOException 
    {
        String sourceDirName = dirToClean.getName();
        Logger.log(LogLevel.INFO, "Directory being cleaned is " + sourceDirName);

        File[] files = dirToClean.listFiles();
        
        if (files != null)
        {
	        for (int n = 0; n < files.length; ++n) 
	        {
	            if (files[n].getName().contains(".hprof"))
	            {
	                Logger.log(LogLevel.INFO, "Remove file " + files[n].getName());
	                files[n].delete();
	            }
	        }
        }
    }

	public void copyDirectory(
			File source, 
			File destination, 
			Map<String, Object> directoriesToCopy, 
			Map<String, Object> directoriesToMake,
			Map<String, Object> filesToxclude,
			Map<String, Object> fileTypesToxclude) throws IOException 
	{
		File nextDirectory = new File(destination, source.getName());
		String sourceDirName = source.getName();
		Logger.log(LogLevel.INFO, "Directory being considered is " + sourceDirName);
		if (directoriesToMake.containsKey(sourceDirName)|| directoriesToCopy.containsKey(sourceDirName))
		{
			//
			// create the directory if necessary...
			//
			Logger.log(LogLevel.INFO, "Directory being made is " + sourceDirName);
			if (!nextDirectory.exists() && !nextDirectory.mkdirs()) 
			{
	 			String message = "DirCopyFailed: " + nextDirectory.getAbsolutePath() ;
				throw new IOException(message);
			}

			if (directoriesToCopy.containsKey(sourceDirName))
			{
				Logger.log(LogLevel.INFO, "Directory being copied is " + sourceDirName);
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
			Logger.log(LogLevel.INFO, "Directory not being copied is " + sourceDirName);
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
        
		// From Input/Aces
		directoriesToCopy.put("Pictures", null);
		
        directoriesToCopy.put("Aces", null);
        directoriesToCopy.put("Aircraft", null);
        directoriesToCopy.put("Configuration", null);
        directoriesToCopy.put("Skins", null);
        directoriesToCopy.put("Squadron", null);
        directoriesToCopy.put("SquadronMovingFront", null);
        directoriesToCopy.put("Configured", null);
        directoriesToCopy.put("DoNotUse", null);
        directoriesToCopy.put("Ranks", null);

		return directoriesToCopy;
	}


	protected static void deleteExistingDeploy(String targetDir) {
		FileUtils fileUtils = new FileUtils();
		fileUtils.deleteRecursive(targetDir);
	}


}

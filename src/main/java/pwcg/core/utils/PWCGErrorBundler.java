package pwcg.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class PWCGErrorBundler
{
	static public final String ERROR_DIR_ROOT = "ErrorReport";
	
    private Campaign campaign;
    private String targetErrorFileName = "";
	
    private String programDataDir; 
    private String targetDataDir; 

	public PWCGErrorBundler(Campaign campaign)
	{
	    this.campaign = campaign;
	}

	public void bundleDebuggingData()
	{
		try
		{	        
			targetErrorFileName = campaign.getCampaignData().getName() + DateUtils.getDateStringYYYYMMDDHHMMSS(new Date());
		    programDataDir = createSourceCampaignDirPath(); 
		    targetDataDir = createTargetDirCampaignPath(); 

			// make sure the error dir is there
			createTargetDirs();
            copyMissionLogFiles();
            copySinglePlayerMissionFiles();
            copyCoopMissionFiles();
            copyCoopDataFiles();
			copyCampaignFiles();
			zipErrorFiles();
			cleanStaging();
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
		
	}

	private void createTargetDirs()
	{
		createErrorDir();
		createTargetDirRoot();
		createTargetDataDir();
	}

	private void createErrorDir()
	{
		String errorDirPath = createErrorDirPath(); 
		
		File errorDirFile = new File(errorDirPath); 
		if (!errorDirFile.exists())
		{
			errorDirFile.mkdir();
		}
	}

	private void createTargetDirRoot()
	{
		String campaignErrorDirPath = createTargetDirPath(); 
		File errorDir = new File(campaignErrorDirPath);
		if (!errorDir.exists())
		{
			errorDir.mkdir();
		}
	}

	private void createTargetDataDir()
	{		
		String campaignTargetDataDirPath = createTargetDirDataPath() ;
		File campaignTargetDataDir = new File(campaignTargetDataDirPath);
		
		if (!campaignTargetDataDir.exists())
		{
			campaignTargetDataDir.mkdir();
		}
	}

	private void copyMissionLogFiles() throws IOException, PWCGException
	{
		String simulatorDataDir = PWCGDirectorySimulatorManager.getInstance().getSimulatorDataDir(); 
		String targetDataDir = createTargetDirDataPath(); 
		copyDirectory(simulatorDataDir, targetDataDir, "missionReport");
		
        String userLogDir = PWCGContext.getInstance().getMissionLogDirectory();
        if (!userLogDir.isEmpty())
        {
            copyDirectory(userLogDir, targetDataDir, "missionReport");
        }
	}

	private void copySinglePlayerMissionFiles() throws IOException, PWCGException
    {
        String missionFileDir = PWCGDirectorySimulatorManager.getInstance().getSinglePlayerMissionFilePath(); 
        String targetDataDir = createTargetDirSinglePlayerMissionPath(); 
        copyDirectory(missionFileDir, targetDataDir, "*");
    }

	private void copyCoopMissionFiles() throws IOException, PWCGException
    {
        String coopMissionFileDir = PWCGDirectorySimulatorManager.getInstance().getCoopMissionFilePath(); 
        String targetDataDir = createTargetDirCoopMissionPath(); 
        copyDirectory(coopMissionFileDir, targetDataDir, "*");
    }
	
	private void copyCoopDataFiles() throws IOException, PWCGException
    {
        String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();
        String targetDataDir = createTargetDirCoopPath(); 

        copyDirectory(coopUserDir, targetDataDir, "*");
    }

	private void copyCampaignFiles() throws IOException, PWCGException
	{
        copyDirectory(programDataDir, targetDataDir, "*");
	}

	private String createErrorDirPath()
	{
		String errorDirPath = PWCGContext.getInstance().getDirectoryManager().getPwcgRootDir()  + ERROR_DIR_ROOT; 
		return errorDirPath;
	}

	private String createTargetDirPath() 
	{
		String campaignErrorDirPath = PWCGContext.getInstance().getDirectoryManager().getPwcgRootDir() + ERROR_DIR_ROOT + "\\" + targetErrorFileName; 
		return campaignErrorDirPath;
	}

    private String createTargetDirDataPath() 
    {
        String campaignTargetDataDirPath = PWCGContext.getInstance().getDirectoryManager().getPwcgRootDir() + ERROR_DIR_ROOT + "\\" + targetErrorFileName + "\\Data"; 
        return campaignTargetDataDirPath;
    }

    private String createTargetDirSinglePlayerMissionPath() 
    {
        String campaignTargetDataDirPath = PWCGContext.getInstance().getDirectoryManager().getPwcgRootDir() + ERROR_DIR_ROOT + "\\" + targetErrorFileName + "\\Data\\Mission\\PWCG"; 
        return campaignTargetDataDirPath;
    }

    private String createTargetDirCoopMissionPath() 
    {
        String campaignTargetDataDirPath = PWCGContext.getInstance().getDirectoryManager().getPwcgRootDir() + ERROR_DIR_ROOT + "\\" + targetErrorFileName + "\\Data\\Mission\\Cooperative"; 
        return campaignTargetDataDirPath;
    }

    private String createTargetDirCoopPath() 
    {
        String campaignTargetDataDirPath = PWCGContext.getInstance().getDirectoryManager().getPwcgRootDir() + ERROR_DIR_ROOT + "\\" + targetErrorFileName + "\\Coop"; 
        return campaignTargetDataDirPath;
    }

	private String createTargetDirCampaignPath() 
	{
		String errorDateDir = PWCGContext.getInstance().getDirectoryManager().getPwcgRootDir() + ERROR_DIR_ROOT + "\\" + targetErrorFileName + "\\" + campaign.getCampaignData().getName();
		return errorDateDir;
	}

	private String createSourceCampaignDirPath()
	{
		String campaignDirPath = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName(); 
		
		return campaignDirPath;
	}

	private void copyDirectory(
			String source, 
			String destination, 
			String prefix) throws IOException, PWCGException 
	{
	    File sourceDir = new File(source); 
	    File destinationDir = new File(destination); 

       if (!sourceDir.exists())
        {
           sourceDir.mkdirs();
        }
        
        if (!destinationDir.exists())
        {
            destinationDir.mkdirs();
        }
	                	    
		PWCGLogger.log(LogLevel.INFO, "Directory being copied is " + source);
		File[] files = sourceDir.listFiles();
				
		for (File file : files) 
		{
			if (file.isDirectory())
			{
                if (prefix.equalsIgnoreCase("*") || file.getName().startsWith(prefix))
                {
                    String newSource = source + "\\" + file.getName();
                    String newDestination = destination + "\\" + file.getName();
    			    copyDirectory(newSource, newDestination, "*");
                }
			}
			else
			{
				if (prefix.equalsIgnoreCase("*") || file.getName().startsWith(prefix))
				{
					copyFileOrDirectory(file, destinationDir);
				}
			}
		}
	}

	private void copyFileOrDirectory(File source, File destination) throws IOException 
	{
		//
		// if the destination is a dir, what we really want to do is create
		// a file with the same name in that dir
		//
		if (destination.isDirectory())
		{
			destination = new File(destination, source.getName());
		}
		
		FileInputStream input = new FileInputStream(source);
		copyFile(input, destination);
	}

	private void copyFile(InputStream input, File destination) throws IOException 
	{
		OutputStream output = null;

		output = new FileOutputStream(destination);

		byte[] buffer = new byte[1024];

		int bytesRead = input.read(buffer);

		while (bytesRead >= 0) {
			output.write(buffer, 0, bytesRead);
			bytesRead = input.read(buffer);
		}

		input.close();

		output.close();
	}

	private void zipErrorFiles() throws IOException 
	{
		String zipFile = createErrorDirPath()  + "\\" + targetErrorFileName + ".zip";
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        String srcDir = createTargetDirPath();
		
		compressDirectory(srcDir, "", zos);
		
        zos.close();
	}

	private void compressDirectory(String sourcePath, String targetDir, ZipOutputStream zos) throws IOException
	{
	    File fileToCompress = new File(sourcePath);
	    // list contents.
	    String[] contents = fileToCompress.list();
	    // iterate through directory and compress files.
	    for(int i = 0; i < contents.length; i++)
	    {
	        File f = new File(sourcePath, contents[i]);
	        // testing type. directories and files have to be treated separately.
	        if(f.isDirectory())
	        {
	            // initiate recursive call
	            String newTargetDir = f.getName();
	            if (!targetDir.isEmpty())
	            {
	                newTargetDir = targetDir + "\\" + newTargetDir;
	            }
	            compressDirectory(f.getPath(), newTargetDir,  zos);
	            // continue the iteration
	            continue;
	        }
	        else
	        {
	             // prepare stream to read file.
	             FileInputStream in = new FileInputStream(f);
	             // create ZipEntry and add to outputting stream.
	             String filename = targetDir + "\\" + f.getName();
	             ZipEntry zipEntry = new ZipEntry(filename);
	             zos.putNextEntry(zipEntry);
	             // write the data.
	             int len;
	             byte[] buffer = new byte[1024];
	             while((len = in.read(buffer)) > 0)
	             {
	                 zos.write(buffer, 0, len);
	             }
	             zos.flush();
	             zos.closeEntry();
	             in.close();
	         }
	     }
	 }

    private void cleanStaging() throws IOException 
    {
        String dirToCleanPath = createTargetDirPath();
        File dirToClean = new File(dirToCleanPath);
        
        deleteDirectory(dirToClean);
    }

    private static boolean deleteDirectory(File directory) 
    {
        if(directory.exists())
        {
            File[] files = directory.listFiles();
            if(files != null)
            {
                for(File file : files) 
                {
                    if(file.isDirectory()) 
                    {
                        deleteDirectory(file);
                    }
                    else 
                    {
                    	file.delete();
                    }
                }
            }
        }
        
        return(directory.delete());
    }
    
    public String getTargetErrorFileName()
    {
        return targetErrorFileName;
    }
    
    
}

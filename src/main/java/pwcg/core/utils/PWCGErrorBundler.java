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
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger.LogLevel;

public class PWCGErrorBundler
{
	static public final String ERROR_DIR_ROOT = "ErrorReport";
	
	String targetErrorFileName = "";
	

	public PWCGErrorBundler()
	{
	}
	
	/**
	 * 
	 */
	public void bundleDebuggingData()
	{
		try
		{
		    Campaign campaign = PWCGContextManager.getInstance().getCampaign();
	        
			targetErrorFileName = campaign.getCampaignData().getName() + DateUtils.getDateStringYYYYMMDDHHMMSS(new Date());
			
			// make sure the error dir is there
			createTargetDirs();
			copyMissionFiles();
			copyCampaignFiles();
			zipErrorFiles();
			cleanStaging();
		}
		catch (Exception e)
		{
			Logger.logException(e);
		}
		
	}

	/**
	 * 
	 */
	private void createTargetDirs()
	{
		// make sure the error dir is there
		createErrorDir();
		createTargetDirRoot();
		createTargetDataDir();
		createTargetCampaignDir();
	}
	
	/**
	 * 
	 */
	private void createErrorDir()
	{
		String errorDirPath = createErrorDirPath(); 
		
		File errorDirFile = new File(errorDirPath); 
		if (!errorDirFile.exists())
		{
			errorDirFile.mkdir();
		}
	}

	/**
	 * 
	 */
	private void createTargetDirRoot()
	{
		// Create the specific error dir for this occurrence
		String campaignErrorDirPath = createTargetDirPath(); 
		File errorDir = new File(campaignErrorDirPath);
		if (!errorDir.exists())
		{
			errorDir.mkdir();
		}
	}

	/**
	 * 
	 */
	private void createTargetDataDir()
	{
		
		// Create the specific error dir for this occurrence
		String campaignTargetDataDirPath = createTargetDirDataPath() ;
		File campaignTargetDataDir = new File(campaignTargetDataDirPath);
		
		if (!campaignTargetDataDir.exists())
		{
			campaignTargetDataDir.mkdir();
		}
	}


	/**
	 * 
	 */
	public void createTargetCampaignDir()
	{
		// Create the specific error dir for this occurrence
		String errorDateDir = createTargetDirCampaignPath(); 
		File campaignDir = new File(errorDateDir); 
		
		if (!campaignDir.exists())
		{
			campaignDir.mkdir();
		}
	}

	/**
	 * @throws IOException 
	 * @throws PWCGException 
	 * 
	 */
	public void copyMissionFiles() throws IOException, PWCGException
	{
		File programDataDir = new File(PWCGContextManager.getInstance().getDirectoryManager().getSimulatorDataDir()); 
		File targetDataDir = new File(createTargetDirDataPath()); 
		
		if (!programDataDir.exists())
		{
			throw new PWCGException ("Could not find 777/1C program data directory: " + programDataDir.getAbsolutePath());
		}
		
		if (!targetDataDir.exists())
		{
			throw new PWCGException ("Could not find target data directory: " + targetDataDir.getAbsolutePath());
		}
				
		copyDirectory(programDataDir, targetDataDir, "missionReport");
	}


	/**
	 * @throws IOException 
	 * @throws PWCGException 
	 * 
	 */
	public void copyCampaignFiles() throws IOException, PWCGException
	{
		File programDataDir = new File(createSourceCampaignDirPath()); 
		File targetDataDir = new File(createTargetDirCampaignPath()); 

		if (!programDataDir.exists())
		{
			throw new PWCGException ("Could not find PWCG campaign data directory: " + programDataDir.getAbsolutePath());
		}
		
		if (!targetDataDir.exists())
		{
			throw new PWCGException ("Could not find target campaign directory: " + targetDataDir.getAbsolutePath());
		}
				
		copyDirectory(programDataDir, targetDataDir, "*");
	}

	
	/**
	 * The root PWCG error directory
	 * 
	 * @return
	 */
	private String createErrorDirPath()
	{
		String errorDirPath = PWCGContextManager.getInstance().getDirectoryManager().getPwcgRootDir()  + ERROR_DIR_ROOT; 
		
		return errorDirPath;
	}


	/**
	 * The directory to be zipped.  Contains misison logs and campaign data
	 * 
	 * @return
	 */
	private String createTargetDirPath() 
	{
		String campaignErrorDirPath = PWCGContextManager.getInstance().getDirectoryManager().getPwcgRootDir() + ERROR_DIR_ROOT + "\\" + targetErrorFileName; 
		return campaignErrorDirPath;
	}

	
	/**
	 * Where we will copy mission data logs
	 * 
	 * @return
	 */
	private String createTargetDirDataPath() 
	{
		String campaignTargetDataDirPath = PWCGContextManager.getInstance().getDirectoryManager().getPwcgRootDir() + ERROR_DIR_ROOT + "\\" + targetErrorFileName + "\\Data"; 
		return campaignTargetDataDirPath;
	}
		

	/**
	 * Where we will copy campaign files
	 * 
	 * @return
	 */
	private String createTargetDirCampaignPath() 
	{
		Campaign campaign  = PWCGContextManager.getInstance().getCampaign();
		String errorDateDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgRootDir() + ERROR_DIR_ROOT + "\\" + targetErrorFileName + "\\" + campaign.getCampaignData().getName();
		return errorDateDir;
	}

	/**
	 * Source campaign files
	 * 
	 * @return
	 */
	public String createSourceCampaignDirPath()
	{
		// Create the specific error dir for this occurrence
		Campaign campaign  = PWCGContextManager.getInstance().getCampaign();
		String campaignDirPath = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName(); 
		
		return campaignDirPath;
	}
	/**
	 * @param source
	 * @param destination
	 * @throws IOException
	 */
	private void copyDirectory(
			File source, 
			File destination, 
			String prefix) throws IOException 
	{
		Logger.log(LogLevel.INFO, "Directory being copied is " + source.getAbsolutePath());
		File[] files = source.listFiles();
				
		for (File file : files) 
		{
			if (file.isDirectory())
			{
				continue;
			}
			else
			{
				if (prefix.equalsIgnoreCase("*") || file.getName().startsWith(prefix))
				{
					copyFileOrDirectory(file, destination);
				}
			}
		}
	}

	/**
     * Copy a file or directory from source to target
     * 
	 * @param source
	 * @param destination
	 * @throws IOException
	 */
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


	/**
	 * Copy a file from source to target
	 * 
	 * @param input
	 * @param destination
	 * @throws IOException
	 */
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


	/**
     * Initiate compression of the staging area
     * 
	 * @throws IOException 
	 */
	private void zipErrorFiles() throws IOException 
	{
		String zipFile = createErrorDirPath()  + "\\" + targetErrorFileName + ".zip";
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        String srcDir = createTargetDirPath();
		
		compressDirectory(srcDir, "", zos);
		
        zos.close();
	}

	/**
     * Compress the staging directory
     * 
	 * @param sourcePath
	 * @param zos
	 * @throws IOException
	 */
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
	            // add empty directory
	            zos.putNextEntry(new ZipEntry(f.getName() + "\\"));
	            // initiate recursive call
	            String newTargetDir = f.getName();
	            if (!targetDir.isEmpty())
	            {
	                newTargetDir = targetDir + "\\";
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
	
	
    /**
     * Initiate clean up of the staging area
     * 
     * @param dirToClean
     * @throws IOException
     */
    private void cleanStaging() throws IOException 
    {
        String dirToCleanPath = createTargetDirPath();
        File dirToClean = new File(dirToCleanPath);
        
        deleteDirectory(dirToClean);
    }
    
    /**
     * Delete the staging directory structure
     * 
     * @param directory
     * @return
     */
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

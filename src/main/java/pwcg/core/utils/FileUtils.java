package pwcg.core.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;

public class FileUtils
{
    public void deleteRecursive(String deleteDir)
    {
        File deleteRoot = new File(deleteDir);
        if (deleteRoot.exists())
        {
            deleteFile(deleteRoot.getAbsolutePath());
        }
    }
    
    public void deleteFilesInDirectory(String directoryName) throws PWCGException
    {
        deleteRecursive(directoryName);
        File directory = new File(directoryName);
        directory.mkdir();
    }

    public boolean fileExists(String filePath)
    {
        File file = new File(filePath);
        if (file.exists())
        {
            return true;
        }
        
        return false;
    }
    
    private void deleteFile(String sFilePath)
    {
        File oFile = new File(sFilePath);
        if(oFile.isDirectory())
        {
            File[] aFiles = oFile.listFiles();
            for(File oFileCur: aFiles)
            {
                deleteFile(oFileCur.getAbsolutePath());
            }
        }
        oFile.delete();

    }
    
    public void deleteFilesByFileName(List<String> filesToDelete)
    {
        for (String pathname : filesToDelete) 
        {
            File file = new File(pathname);
            if (file.exists())
            {
                file.delete();
            }
        }
    }
    
    public List<File> getFilesInDirectory(String directory) throws PWCGException
    {
    	List<File> filesInDirectory = new ArrayList<>();
    	File directoryFile = new File(directory);
    	if (directoryFile.exists())
    	{
    		if (directoryFile.isDirectory())
    		{
    			for (File file : directoryFile.listFiles())
    			{
    	    		if (!file.isDirectory())
    	    		{
    	    			filesInDirectory.add(file);
    	    		}
    			}
    		}
    	}
		
		return filesInDirectory;
    }

    public List<File> getFilesWithFilter(String directory, String filterString) throws PWCGException
    {
    	List<File> matchingFiles = new ArrayList<>();
    	File directoryFile = new File(directory);
    	if (directoryFile.exists())
    	{
    		if (directoryFile.isDirectory())
    		{
    			for (File file : directoryFile.listFiles())
    			{
        			FilenameFilter filter = new PwcgFileNameFilter(filterString);
    				if (filter.accept(directoryFile, file.getName()))
    				{
    					matchingFiles.add(file);
    				}
    			}
    		}
    	}
		
		return matchingFiles;
    }
    
    public List<File> getDirectories(String directory) throws PWCGException
    {
    	List<File> directoriesFound = new ArrayList<>();
    	File directoryFile = new File(directory);
    	if (directoryFile.exists())
    	{
    		if (directoryFile.isDirectory())
    		{
    			for (File file : directoryFile.listFiles())
    			{
    				if (file.isDirectory())
    				{
    					directoriesFound.add(file);
    				}
    			}
    		}
    	}
		
		return directoriesFound;
    }

    public File retrieveFile(String directory, String filename)
	{
		File file = new File(directory + filename);
		return file;
	}

    
    public static String stripFileExtension(String filename)
	{
    	if (filename.contains("."))
    	{
    		return filename.substring(0, filename.lastIndexOf('.'));
    	}
    	else
    	{
    		return filename;
    	}
	}
    
    public long ageOfFilesInMillis(String pathname) throws PWCGException
    {
        File file = new File(pathname);
        if (file.exists())
        {
            try
            {
                Path path = FileSystems.getDefault().getPath(pathname);
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                FileTime fileTime = attr.creationTime();
                return fileTime.toMillis();
            }
            catch (IOException e)
            {
                Logger.logException(e);
                throw new PWCGException("Could not get  file time for file " + pathname);
            }
        }
        else
        {
            throw new PWCGException("Could not get  file time.  File does not exist " + pathname);
        }
    }

}

package pwcg.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;

public class DirectoryReader
{
    private List<File> directories = new ArrayList<>();
    private List<File> files = new ArrayList<>();

    public void sortFilesInDir(String dirName) throws PWCGException
    {
        File dir = new File(dirName);
        if (dir.exists())
        {
            if (dir.isDirectory())
            {
                File[] children = dir.listFiles();
                if (children != null) 
                {
                    sortDirectoryContents(children);
                }
            }
        }
    }

    private List<File> sortDirectoryContents(File[] children)
    {
        for (File child : children)
        {
            if (child.isDirectory())
            {
                directories.add(child);
            }
            else
            {
                files.add(child);
            }
        }
        
        return directories;
    }

    public List<File> getSortedFilesWithFilter(String filter)
    {
        TreeMap <String, File> sortedFiles = new TreeMap<>();

        for (File file : files)
        {
            if (file.getName().contains(filter))
            {
            	sortedFiles.put(file.getName(), file);
            }
        }
        
        List<File> results = new ArrayList<>();
        results.addAll(sortedFiles.descendingMap().values());
        return results;
    }

    public List<File> getDirectories()
    {
        return directories;
    }

    public List<File> getFiles()
    {
        return files;
    }

    public List<File> getAllContent()
    {
        List<File> allContent = new ArrayList<>();
        allContent.addAll(directories);
        allContent.addAll(files);
        return allContent;
    }
}

package pwcg.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;

public class DirectoryReader
{
    private List<String> directories = new ArrayList<>();
    private List<String> files = new ArrayList<>();

    public void sortilesInDir(String dirName) throws PWCGException
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

    private List<String> sortDirectoryContents(File[] children)
    {
        for (File child : children)
        {
            if (child.isDirectory())
            {
                directories.add(child.getName());
            }
            else
            {
                files.add(child.getName());
            }
        }
        
        return directories;
    }

    public List<String> getSortedFilesWithFilter(String filter)
    {
        TreeMap <String, String> sortedFiles = new TreeMap<>();

        for (String filename : files)
        {
            if (filename.contains(filter))
            {
            	sortedFiles.put(filename, filename);
            }
        }
        
        List<String> results = new ArrayList<>();
        results.addAll(sortedFiles.descendingMap().values());
        return results;
    }

    public List<String> getDirectories()
    {
        return directories;
    }

    public List<String> getFiles()
    {
        return files;
    }

    public List<String> getAllContent()
    {
        List<String> allContent = new ArrayList<>();
        allContent.addAll(directories);
        allContent.addAll(files);
        return allContent;
    }
}

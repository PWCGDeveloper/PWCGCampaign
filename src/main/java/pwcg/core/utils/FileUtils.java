package pwcg.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public static boolean createDirIfNeeded(String campaignConfigDir)
    {
        boolean exists = true;
        File dir = new File(campaignConfigDir);
        if (!dir.exists())
        {
            exists = dir.mkdirs();
        }
        return exists;
    }

    public static void deleteRecursive(String deleteDir)
    {
        File deleteRoot = new File(deleteDir);
        if (deleteRoot.exists())
        {
            deleteFile(deleteRoot.getAbsolutePath());
        }
    }

    public static void deleteFilesInDirectory(String directoryName) throws PWCGException
    {
        deleteRecursive(directoryName);
        File directory = new File(directoryName);
        directory.mkdir();
    }

    public static boolean findInDirectory(String directoryName, String lookForFileName) throws PWCGException
    {
        File directory = new File(directoryName);
        lookForFileName = lookForFileName.toLowerCase();
        for (String fileName : directory.list())
        {
            fileName = fileName.toLowerCase();
            if (fileName.contains(lookForFileName))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean fileExists(String filePath)
    {
        File file = new File(filePath);
        if (file.exists())
        {
            return true;
        }

        return false;
    }

    public static void deleteFile(String sFilePath)
    {
        File oFile = new File(sFilePath);
        if (oFile.isDirectory())
        {
            File[] aFiles = oFile.listFiles();
            for (File oFileCur : aFiles)
            {
                deleteFile(oFileCur.getAbsolutePath());
            }
        }
        oFile.delete();

    }

    public static void deleteFiles(List<File> filesToDelete)
    {
        for (File file : filesToDelete)
        {
            if (file.exists())
            {
                file.delete();
            }
        }
    }

    public static List<File> getFilesInDirectory(String directory) throws PWCGException
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

    public static List<File> getFilesWithFilter(String directory, String filterString) throws PWCGException
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

    public static List<File> getDirectories(String directory) throws PWCGException
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

    public static File retrieveFile(String directory, String filename)
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

    public static long ageOfFilesInMillis(File file) throws PWCGException
    {
        if (file.exists())
        {
            try
            {
                Path path = FileSystems.getDefault().getPath(file.getPath());
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                FileTime fileTime = attr.creationTime();
                return fileTime.toMillis();
            }
            catch (IOException e)
            {
                PWCGLogger.logException(e);
                throw new PWCGException("Could not get  file time for file " + file.getPath());
            }
        }
        else
        {
            throw new PWCGException("Could not get  file time.  File does not exist " + file.getPath());
        }
    }

    public static void copyFile(File source, File destination) throws IOException
    {
        if (destination.isDirectory())
        {
            destination = new File(destination, source.getName());
        }

        FileInputStream input = new FileInputStream(source);
        copyFile(input, destination);
    }

    public static void copyFile(InputStream input, File destination) throws IOException
    {
        OutputStream output = null;

        output = new FileOutputStream(destination);

        byte[] buffer = new byte[1024];

        int bytesRead = input.read(buffer);

        while (bytesRead >= 0)
        {
            output.write(buffer, 0, bytesRead);
            bytesRead = input.read(buffer);
        }

        input.close();

        output.close();
    }

    public static void copyDirectory(File sourceDir, File targetDir) throws IOException
    {
        if (!sourceDir.exists())
        {
            return;
        }
        
        if (sourceDir.isDirectory())
        {
            copyDirectoryRecursively(sourceDir, targetDir);
        }
        else
        {
            Files.copy(sourceDir.toPath(), targetDir.toPath());
        }
    }

    private static void copyDirectoryRecursively(File source, File target) throws IOException
    {
        if (!target.exists())
        {
            target.mkdir();
        }

        for (String child : source.list())
        {
            copyDirectory(new File(source, child), new File(target, child));
        }
    }
}


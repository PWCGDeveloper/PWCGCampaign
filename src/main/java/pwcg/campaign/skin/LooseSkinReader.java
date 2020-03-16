package pwcg.campaign.skin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.Parsers;

public class LooseSkinReader
{
    private Map<String, List<String>> looseSkins = new HashMap<>();

    public Map<String, List<String>> readLooseSkins(String skinDirName)
    {
        try
        {
            File dir = new File(skinDirName);
            if (dir.exists())
            {
                if (dir.isDirectory())
                {
                    readSkinDirs(dir);
                }
            }
        }
        catch (Exception exp)
        {
             PWCGLogger.logException(exp);;
        }
        
        return looseSkins;
    }

    private void readSkinDirs(File dir)
    {
        File[] planeDirs = dir.listFiles();
        for (File planeDir : planeDirs)
        {
            if (planeDir.isDirectory())
            {
                readSkinsFromDir(planeDir);
            }            
        }
    }

    private void readSkinsFromDir(File planeDir)
    {
        List<String> skinFiles = new ArrayList<String>();
        File[] skinFilesForPlane = planeDir.listFiles();
        for (File skinFileForPlane : skinFilesForPlane)
        {
            if (skinFileForPlane.isFile() && skinFileForPlane.getName().contains(".dds"))
            {
                String fileName = Parsers.removeExtension(skinFileForPlane.getName());
                skinFiles.add(fileName);
            }
        }
        
        looseSkins.put(planeDir.getName().toLowerCase(), skinFiles);
    }



}

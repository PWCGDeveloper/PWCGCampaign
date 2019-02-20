package pwcg.aar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;

public abstract class TemplatedSkinCleaner
{
    public static int cleanSkinFiles() throws PWCGException
    {
        List<String> filesToDelete = getSkinFilesToDelete();
        FileUtils.deleteFilesByFileName(filesToDelete);

        return filesToDelete.size();
    }

    private static List<String> getSkinFilesToDelete() throws PWCGException
    {
        Set<String> usedSkins = new TreeSet<>();
        Set<String> allGeneratedSkins = new TreeSet<>();
        Pattern pattern = Pattern.compile("  Skin = \"([^\"]+pwcg_[^\"]+.dds)\";", Pattern.CASE_INSENSITIVE);

        for (String dir : new String[] {"Missions\\", "Multiplayer\\Cooperative\\"})
        {
            String missionDir = PWCGContext.getInstance().getDirectoryManager().getSimulatorDataDir() + dir;
            DirectoryReader directoryReader = new DirectoryReader();
            directoryReader.sortilesInDir(missionDir);
            for (String filename : directoryReader.getFiles())
            {
                if (filename.endsWith(".mission"))
                {
                    try (BufferedReader reader = new BufferedReader(new FileReader(missionDir + "\\" + filename)))
                    {
                        for (String line : reader.lines().filter(pattern.asPredicate()).collect(Collectors.toList())) {
                            Matcher m = pattern.matcher(line);
                            if (m.matches())
                                usedSkins.add(m.group(1).toLowerCase());
                        }
                    } catch (IOException e) {
                        PWCGLogger.logException(e);
                    }
                }
            }
        }
        File skinDir = new File(PWCGContext.getInstance().getDirectoryManager().getSimulatorDataDir() + "graphics\\skins\\");
        if (skinDir.isDirectory())
        {
            for (File plane : skinDir.listFiles())
            {
                if (plane.isDirectory())
                {
                    for (File skin : plane.listFiles())
                    {
                        if (skin.getName().toLowerCase().startsWith("pwcg_"))
                            allGeneratedSkins.add(plane.getName().toLowerCase() + "/" + skin.getName().toLowerCase());
                    }
                }
            }
        }
        Set<String> skinsToDelete = new TreeSet<>(allGeneratedSkins);
        skinsToDelete.removeAll(usedSkins);
        return skinsToDelete.stream().map(skin -> skinDir.getPath() + "\\" + skin.replace("/",  "\\")).collect(Collectors.toList());
    }
}


package pwcg.gui.maingui;

import java.io.File;
import java.io.IOException;

import pwcg.campaign.context.PWCGDirectoryProductManager;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.utils.FileUtils;

public class PwcgDirectoryRestructure
{
    public static void main(String[] args)
    {
        try
        {
            PwcgDirectoryRestructure.restructureDirectories(PWCGProduct.BOS);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void restructureDirectories(PWCGProduct product) throws IOException
    {
        String userDir = System.getProperty("user.dir");
        String pwcgRootDir = userDir + "\\";
        File newUserDir = new File(pwcgRootDir + "User\\");
        if (newUserDir.exists())
        {
            return;
        }
        createNewUserDirectories();

        moveCampaignContents();
        moveUserConfigContents(product);
        moveCoopDirectoryContents();
        moveAudioFiles();
    }

    private static void createNewUserDirectories()
    {
        PWCGDirectoryUserManager.getInstance();
    }

    private static void moveCampaignContents() throws IOException
    {
        String userDir = System.getProperty("user.dir");
        String pwcgRootDir = userDir + "\\";
        String oldDir = pwcgRootDir + "Campaigns\\";
        String newDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir();

        FileUtils.copyDirectory(new File(oldDir), new File(newDir));
        FileUtils.deleteRecursive(oldDir);
    }

    private static void moveUserConfigContents(PWCGProduct product) throws IOException
    {
        PWCGDirectoryProductManager directoryProductManager = new PWCGDirectoryProductManager(product);
        String oldDir = directoryProductManager.getPwcgProductDataDir() + "User\\";
        String newDir = PWCGDirectoryUserManager.getInstance().getPwcgUserConfigDir();

        FileUtils.copyDirectory(new File(oldDir), new File(newDir));
        FileUtils.deleteRecursive(oldDir);
    }

    private static void moveCoopDirectoryContents() throws IOException
    {
        String userDir = System.getProperty("user.dir");
        String pwcgRootDir = userDir + "\\";
        String oldDir = pwcgRootDir + "Coop\\Users\\";
        String newDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();

        FileUtils.copyDirectory(new File(oldDir), new File(newDir));
        String oldCoopDir = pwcgRootDir + "Coop\\";
        FileUtils.deleteRecursive(oldCoopDir);
    }

    private static void moveAudioFiles() throws IOException
    {
        String userDir = System.getProperty("user.dir");
        String pwcgRootDir = userDir + "\\";
        String oldDir = pwcgRootDir + "Audio\\";
        String newDir = PWCGDirectoryUserManager.getInstance().getPwcgAudioDir();

        FileUtils.copyDirectory(new File(oldDir), new File(newDir));
        FileUtils.deleteRecursive(oldDir);
    }
}

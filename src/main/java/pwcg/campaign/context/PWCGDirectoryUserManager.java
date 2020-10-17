package pwcg.campaign.context;

import pwcg.core.utils.FileUtils;

public class PWCGDirectoryUserManager
{
    private String pwcgUserDir = "";
    private String pwcgCampaignDir = "";
    private String pwcgUserConfigDir = "";
    private String pwcgCoopDir = "";
    private String pwcgAudioDir = "";

    private static PWCGDirectoryUserManager instance = new PWCGDirectoryUserManager();
    
    public static PWCGDirectoryUserManager getInstance()
    {
        if (instance.pwcgUserDir.isEmpty())
        {
            instance.createUserDirs();
        }
        
        return instance;
    }


    private void createUserDirs()
    {
        String userDir = System.getProperty("user.dir");
        String pwcgRootDir = userDir + "\\";

        pwcgUserDir = pwcgRootDir + "User\\";
        pwcgCampaignDir = pwcgUserDir + "Campaigns\\";
        pwcgUserConfigDir = pwcgUserDir + "Config\\";
        pwcgCoopDir = pwcgUserDir + "Coop\\";
        pwcgAudioDir = pwcgUserDir + "Audio\\";
        
        FileUtils.createDirIfNeeded(pwcgUserDir);
        FileUtils.createDirIfNeeded(pwcgCampaignDir);
        FileUtils.createDirIfNeeded(pwcgUserConfigDir);
        FileUtils.createDirIfNeeded(pwcgCoopDir);
        FileUtils.createDirIfNeeded(pwcgAudioDir);
    }

    public String getPwcgCampaignsDir()
    {
        return pwcgCampaignDir;
    }

    public String getPwcgAudioDir()
    {
        return pwcgAudioDir;
    }

    public String getPwcgCoopDir()
    {
        return pwcgCoopDir;
    }

    public String getPwcgUserConfigDir()
    {
        return pwcgUserConfigDir;
    }
}

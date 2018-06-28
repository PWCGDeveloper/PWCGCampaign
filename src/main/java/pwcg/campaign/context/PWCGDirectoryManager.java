package pwcg.campaign.context;

public class PWCGDirectoryManager
{
    private static PWCGDirectoryManager instance = null;
    private String simulatorRootDir = "";
    private String pwcgRootDir;
    private String pwcgCampaignDir;

    private PWCGDirectoryManager()
    {
        
    }
    
    public static PWCGDirectoryManager getInstance()
    {
        if (instance == null)
        {
            instance = new PWCGDirectoryManager();
            instance.initialize(); 
        }
        
        return instance;
    }

    private void initialize()
    {
        String userDir = System.getProperty("user.dir");
        simulatorRootDir = userDir + "\\..\\";
        pwcgRootDir = userDir + "\\";
        pwcgCampaignDir = instance.pwcgRootDir + "Campaigns\\";
    }
    
    public String getSimulatorDataDir()
    {
        return simulatorRootDir + "data\\";
    }

    public String getPwcgCampaignsDir()
    {
        return pwcgCampaignDir;
    }
    

    public String getPwcgDataDir()
    {
        if (PWCGContextManager.isRoF())
        {
            return pwcgRootDir + "RoFData\\";
        }
        else
        {
            return pwcgRootDir + "BoSData\\";
        }
    }
    
    public String getPwcgAudioDir()
    {
        return pwcgRootDir + "Audio\\";
    }
    
    public String getPwcgInputDir()
    {
        return getPwcgDataDir() + "Input\\";
    }
    
    public String getPwcgAircraftInfoDir()
    {
        return getPwcgInputDir() + "Aircraft\\";
    }
    
    public String getPwcgAirfieldHotSpotsDir()
    {
        return getPwcgInputDir() + "AirfieldHotSpots\\";
    }
    
    public String getPwcgConfigurationDir()
    {
        return getPwcgInputDir() + "Configuration\\";
    }
    
    public String getPwcgSquadronDir()
    {
        return getPwcgInputDir() + "Squadron\\";
    }
    
    public String getPwcgSquadronMovingFrontDir()
    {
        return getPwcgInputDir() + "SquadronMovingFront\\";
    }
    
    public String getPwcgImagesDir()
    {
        return getPwcgDataDir() + "Images\\";
    }
    
    public String getPwcgNamesDir()
    {
        return getPwcgDataDir() + "Names\\";
    }
    
    public String getPwcgUserDir()
    {
        return getPwcgDataDir() + "User\\";
    }

	public String getPwcgSkinsDir()
	{
        return getPwcgInputDir() + "Skins\\";
	}

    public String getPwcgAcesDir()
    {
        return getPwcgInputDir() + "Aces\\";
    }


    public String getSimulatorRootDir()
    {
        return simulatorRootDir;
    }

    public void setSimulatorRootDir(String simulatorRootDir)
    {
        this.simulatorRootDir = simulatorRootDir;
    }

    public String getPwcgRootDir()
    {
        return pwcgRootDir;
    }

    public void setPwcgRootDir(String pwcgRootDir)
    {
        this.pwcgRootDir = pwcgRootDir;
    }

    public void setPwcgCampaignsDir(String pwcgCampaignDir)
    {
        this.pwcgCampaignDir = pwcgCampaignDir;
    }

}


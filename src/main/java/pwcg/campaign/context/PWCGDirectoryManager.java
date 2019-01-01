package pwcg.campaign.context;

public class PWCGDirectoryManager
{
    private String simulatorRootDir = "";
    private String pwcgRootDir;
    private String pwcgDataDir;
    private String pwcgCampaignDir;

    public PWCGDirectoryManager(boolean isRoF)
    {
        createRootDir();
        createPwcgDataDir(isRoF);
        createCampaignDir();
    }

    private void createRootDir()
    {
        String userDir = System.getProperty("user.dir");
        simulatorRootDir = userDir + "\\..\\";
        pwcgRootDir = userDir + "\\";
    }
    
    private void createPwcgDataDir(boolean isRoF)
    {
        if (isRoF)
        {
            pwcgDataDir = pwcgRootDir + "RoFData\\";
        }
        else
        {
            pwcgDataDir = pwcgRootDir + "BoSData\\";
        }
    }

    private void createCampaignDir()
    {
        pwcgCampaignDir = pwcgRootDir + "Campaigns\\";
    }

    public String getSimulatorDataDir()
    {
        return simulatorRootDir + "data\\";
    }

    public String getPwcgCampaignsDir()
    {
        return pwcgCampaignDir;
    }
    
    public String getPwcgAudioDir()
    {
        return pwcgRootDir + "Audio\\";
    }
    
    public String getPwcgCoopDir()
    {
        return pwcgRootDir + "Coop\\";
    }
    
    public String getPwcgInputDir()
    {
        return pwcgDataDir + "Input\\";
    }
    
    public String getPwcgReportDir()
    {
        return pwcgRootDir + "Report\\";
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
        return pwcgDataDir + "Images\\";
    }
    
    public String getPwcgNamesDir()
    {
        return pwcgDataDir + "Names\\";
    }
    
    public String getPwcgUserDir()
    {
        return pwcgDataDir + "User\\";
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


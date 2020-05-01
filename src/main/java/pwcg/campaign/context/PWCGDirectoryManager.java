package pwcg.campaign.context;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class PWCGDirectoryManager
{
    private String simulatorRootDir = "";
    private String pwcgRootDir;
    private String pwcgDataDir;
    private String pwcgCampaignDir;

    public PWCGDirectoryManager(PWCGProduct product)
    {
        createRootDir();
        createPwcgDataDir(product);
        createCampaignDir();
    }

    private void createRootDir()
    {
        String userDir = System.getProperty("user.dir");
        pwcgRootDir = userDir + "\\";

        File simulatorDir = new File(userDir).getParentFile();
        simulatorRootDir = simulatorDir.getAbsolutePath() + "\\";

    }
    
    private void createPwcgDataDir(PWCGProduct product)
    {
        if (product == PWCGProduct.FC)
        {
            pwcgDataDir = pwcgRootDir + "FCData\\";
        }
        else
        {
            pwcgDataDir = pwcgRootDir + "BoSData\\";
        }
    }

    public String getMissionFilePath(Campaign campaign) throws PWCGException 
    {
        String filepath = getSimulatorDataDir() + "Missions\\";
        if (campaign.isCoop())
        {
            filepath = getSimulatorDataDir() + "Multiplayer\\Cooperative\\";
        }
        
        return filepath;
    }

    public String getMissionRewritePath() throws PWCGException 
    {
        String filepath = getSimulatorRootDir() + "bin\\resaver\\";
        return filepath;
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

    public String getPwcgVehiclesDir()
    {
        return getPwcgInputDir() + "Vehicles\\";
    }

    public String getPwcgStaticObjectDir()
    {
        return getPwcgInputDir() + "StaticObjects\\";
    }

    public String getPwcgAcesDir()
    {
        return getPwcgInputDir() + "Aces\\";
    }

    public String getPwcgFontsDir()
    {
        return pwcgDataDir + "Fonts\\";
    }

    public String getPwcgSkinTemplatesDir()
    {
        return pwcgRootDir + "SkinTemplates\\";
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


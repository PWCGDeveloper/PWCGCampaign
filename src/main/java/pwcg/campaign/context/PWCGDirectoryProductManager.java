package pwcg.campaign.context;

public class PWCGDirectoryProductManager
{
    private String pwcgRootDir;
    private String pwcgDataDir;

    public PWCGDirectoryProductManager(PWCGProduct product)
    {
        createPwcgDataDir(product);
    }

    private void createPwcgDataDir(PWCGProduct product)
    {
        String userDir = System.getProperty("user.dir");
        pwcgRootDir = userDir + "\\";
        pwcgDataDir = pwcgRootDir + "BoSData\\";
    }

    public String getPwcgRootDir()
    {
        return pwcgRootDir;
    }

    public String getPwcgProductDataDir()
    {
        return pwcgDataDir;
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

    public String getPwcgTankInfoDir()
    {
        return getPwcgInputDir() + "Tanks\\";
    }

    public String getPwcgAirfieldHotSpotsDir()
    {
        return getPwcgInputDir() + "AirfieldHotSpots\\";
    }

    public String getPwcgConfigurationDir()
    {
        return getPwcgInputDir() + "Configuration\\";
    }

    public String getPwcgCompanyDir()
    {
        return getPwcgInputDir() + "Company\\";
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

    public String getPwcgNewspaperDir()
    {
        return getPwcgInputDir() + "Newspapers\\";
    }

    public String getPwcgInternationalizationDir()
    {
        return getPwcgInputDir() + "International\\";
    }
}

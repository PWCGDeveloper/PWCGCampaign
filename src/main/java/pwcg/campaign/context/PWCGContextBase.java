package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.company.SkirmishProfileManager;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.staticobject.StaticObjectDefinitionManager;
import pwcg.campaign.newspapers.NewspaperManager;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.skin.SkinManager;
import pwcg.campaign.tank.TankTypeFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public abstract class PWCGContextBase implements IPWCGContextManager
{
    BoSContext bosContextManager;

    protected Map<FrontMapIdentifier, PWCGMap> pwcgMaps = new HashMap<FrontMapIdentifier, PWCGMap>();
    protected FrontMapIdentifier currentMap = null;
    protected Campaign campaign = null;
    protected AceManager aceManager = new AceManager();
    protected CompanyManager squadronManager = new CompanyManager();
    protected NewspaperManager newspaperManager = new NewspaperManager();
    protected SkinManager skinManager = new SkinManager();
    protected SkirmishProfileManager skirmishProfileManager = new SkirmishProfileManager();
    protected VehicleDefinitionManager vehicleDefinitionManager = new VehicleDefinitionManager();
    protected StaticObjectDefinitionManager staticObjectDefinitionManager = new StaticObjectDefinitionManager();
    protected boolean testMode = false;
    protected String missionLogPath = "";
    protected PlaneTypeFactory planeTypeFactory = new PlaneTypeFactory();
    protected TankTypeFactory tankTypeFactory = new TankTypeFactory();

    protected List<String> campaignStartDates = new ArrayList<String>();
    
    @Override
    public void configurePwcgMaps() throws PWCGException
    {        
        for (PWCGMap map : pwcgMaps.values())
        {
            map.configure();
        }

        squadronManager.initialize();
        newspaperManager.initialize();
    }

    protected void initialize() throws PWCGException 
    {
        initializeMap();
        
        planeTypeFactory.initialize();
        tankTypeFactory.initialize();
        aceManager.configure();
        squadronManager.initialize();
        skirmishProfileManager.initialize();
        skinManager.initialize();
        vehicleDefinitionManager.initialize();
        staticObjectDefinitionManager.initialize();
    }

    @Override
    public void changeContext(FrontMapIdentifier frontMapIdentifier) throws PWCGException  
    {
        frontMapIdentifier = StalingradMapResolver.resolveStalingradMap(campaign, frontMapIdentifier);
        currentMap = frontMapIdentifier;
    }

    @Override
    public void setCampaign(Campaign campaign) throws PWCGException  
    {
        this.campaign = campaign;
        if (campaign != null)
        {
        	setMapForCampaign(campaign);
        }
    }

    @Override
    public void setMapForCampaign(Campaign campaign) throws PWCGException
    {
        FrontMapIdentifier mapIdentifier = campaign.getCampaignMap();
        if (mapIdentifier != null && mapIdentifier != FrontMapIdentifier.NO_MAP)
        {
            changeContext(mapIdentifier);
            configurePwcgMaps();
        }
    }

    @Override
    public Date getEarliestPwcgDate() throws PWCGException 
    {
        Date earliesDateForPWCG = null;        
        for (PWCGMap map : pwcgMaps.values())
        {
            Date earliestMapDate = map.getFrontDatesForMap().getEarliestMapDate();
            if (earliesDateForPWCG == null)
            {
                earliesDateForPWCG = earliestMapDate;
            }
            else
            {
                if (earliestMapDate.before(earliesDateForPWCG))
                {
                    earliesDateForPWCG = earliestMapDate;
                }
            }
        }
        
        return earliesDateForPWCG;
    }

    @Override
    public Campaign getCampaign()
    {
        return campaign;
    }

    @Override
    public PWCGMap getCurrentMap()
    {
        return pwcgMaps.get(currentMap);
    }

    @Override
    public PWCGMap getMapByMapName(String mapName)
    {
        FrontMapIdentifier mapId = FrontMapIdentifier.getFrontMapIdentifierForName(mapName);
        return pwcgMaps.get(mapId);
    }

    @Override
    public PWCGMap getMapByMapId(FrontMapIdentifier mapId)
    {
        return pwcgMaps.get(mapId);
    }

    @Override
    public AceManager getAceManager()
    {
        return aceManager;
    }

    @Override
    public boolean isTestMode()
    {
        return testMode;
    }

    @Override
    public void setTestMode(boolean testMode)
    {
        this.testMode = testMode;
    }

    @Override
    public List<String> getCampaignStartDates()
    {
        return campaignStartDates;
    }

    @Override
    public List<PWCGMap> getAllMaps()
    {
        List<PWCGMap> allMaps = new ArrayList<PWCGMap>();
        allMaps.addAll(pwcgMaps.values());
        
        return allMaps;
    }

    @Override
    public Airfield getAirfieldAllMaps(String airfieldName)
    {
        Airfield airfield = null;
        
        for (PWCGMap map : pwcgMaps.values())
        {
            AirfieldManager airfieldManager = map.getAirfieldManager();
            if (airfieldManager != null)
            {
                airfield = airfieldManager.getAirfield(airfieldName);
                
                if (airfield != null)
                {
                    return airfield;
                }
            }
        }

        return airfield;
    }

    @Override
    public void setCurrentMap(FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        changeContext(mapIdentifier);        
    }

    @Override
    public CompanyManager getCompanyManager()
    {
        return squadronManager;
    }


    @Override
    public SkirmishProfileManager getSkirmishProfileManager()
    {
        return skirmishProfileManager;
    }

    @Override
    public SkinManager getSkinManager()
    {
        return skinManager;
    }

    @Override
    public TankTypeFactory getTankTypeFactory()
    {
        return tankTypeFactory;
    }

    @Override
    public PlaneTypeFactory getPlaneTypeFactory()
    {
        return planeTypeFactory;
    }

    @Override
    public List<PWCGMap> getMaps()
    {
        return new ArrayList<PWCGMap>(pwcgMaps.values());
    }

    @Override
    public VehicleDefinitionManager getVehicleDefinitionManager()
    {
        return vehicleDefinitionManager;
    }

    @Override
    public StaticObjectDefinitionManager getStaticObjectDefinitionManager()
    {
        return staticObjectDefinitionManager;
    }

    @Override
    public void setMissionLogDirectory(String missionLogPath)
    {
        this.missionLogPath = missionLogPath + "\\";        
    }

    @Override
    public String getMissionLogDirectory()
    {
        return missionLogPath;        
    }
    
    @Override
    public NewspaperManager getNewspaperManager()
    {
        return newspaperManager;
    }

    @Override
    public abstract void initializeMap() throws PWCGException;    

}

package pwcg.campaign.context;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.company.SkirmishProfileManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.staticobject.StaticObjectDefinitionManager;
import pwcg.campaign.newspapers.NewspaperManager;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.skin.SkinManager;
import pwcg.campaign.tank.TankTypeFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public interface IPWCGContextManager
{
    void changeContext(FrontMapIdentifier frontMapIdentifier) throws PWCGException;

    void setCampaign(Campaign campaign) throws PWCGException;

    Date getEarliestPwcgDate() throws PWCGException;

    Campaign getCampaign();

    PWCGMap getCurrentMap();

    PWCGMap getMapByMapName(String mapName);

    PWCGMap getMapByMapId(FrontMapIdentifier mapId);

    AceManager getAceManager();

    boolean isTestMode();

    void setTestMode(boolean testMode);

    List<String> getCampaignStartDates();

    List<PWCGMap> getAllMaps();

    Airfield getAirfieldAllMaps(String airfieldName);

    void initializeMap() throws PWCGException;
    
    CompanyManager getCompanyManager();

    SkirmishProfileManager getSkirmishProfileManager();
    
    SkinManager getSkinManager();

    TankTypeFactory getTankTypeFactory();

    PWCGDirectoryProductManager getDirectoryManager();

    List<PWCGMap> getMaps();

    VehicleDefinitionManager getVehicleDefinitionManager();

    StaticObjectDefinitionManager getStaticObjectDefinitionManager();

    void setMapForCampaign(Campaign campaign) throws PWCGException;

    void configurePwcgMaps() throws PWCGException;

    void setMissionLogDirectory(String missionLogPath);

    String getMissionLogDirectory();

    void setCurrentMap(FrontMapIdentifier map) throws PWCGException;

    NewspaperManager getNewspaperManager();

    PlaneTypeFactory getPlaneTypeFactory();
}

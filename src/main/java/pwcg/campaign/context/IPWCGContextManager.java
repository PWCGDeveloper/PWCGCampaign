package pwcg.campaign.context;

import java.util.Date;
import java.util.List;

import pwcg.campaign.BattleManager;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.group.airfield.staticobject.StaticObjectDefinitionManager;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.skin.SkinManager;
import pwcg.campaign.squadron.SquadronManager;
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

    IAirfield getAirfieldAllMaps(String airfieldName);

    IPayloadFactory getPayloadFactory() throws PWCGException;

    void initializeMap() throws PWCGException;
    
    SquadronManager getSquadronManager();

    BattleManager getBattleManager();

    SkinManager getSkinManager();

    PlaneTypeFactory getPlaneTypeFactory();

    PWCGDirectoryProductManager getDirectoryManager();

    List<PWCGMap> getMaps();

    VehicleDefinitionManager getVehicleDefinitionManager();

    StaticObjectDefinitionManager getStaticObjectDefinitionManager();

    void setMapForCampaign(Campaign campaign) throws PWCGException;

    void resetForMovingFront() throws PWCGException;

    void setMissionLogDirectory(String missionLogPath);

    String getMissionLogDirectory();

    void setCurrentMap(FrontMapIdentifier bodenplatteMap) throws PWCGException;
}
package pwcg.campaign.context;

import java.util.Date;
import java.util.List;

import pwcg.campaign.BattleManager;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.skin.SkinManager;
import pwcg.core.exception.PWCGException;

public interface IPWCGContextManager
{

    void resetForMovingFront() throws PWCGException;

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

    boolean isTestUseMovingFront();

    void setTestUseMovingFront(boolean newTestUseMovingFront) throws PWCGException;

    List<PWCGMap> getAllMaps();

    IAirfield getAirfieldAllMaps(String airfieldName);

    List<FrontMapIdentifier> getMapForAirfield(String airfieldName);

    IPayloadFactory getPayloadFactory() throws PWCGException;

    void initializeMap() throws PWCGException;
    
    boolean determineUseMovingFront()  throws PWCGException;

    SquadronManager getSquadronManager();

    BattleManager getBattleManager();

    SkinManager getSkinManager();

    PlaneTypeFactory getPlaneTypeFactory();

}
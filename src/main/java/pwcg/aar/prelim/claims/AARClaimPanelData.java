package pwcg.aar.prelim.claims;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;

public class AARClaimPanelData
{
    private List<String> enemyPlaneTypesInMission = new ArrayList<>();
    private FrontMapIdentifier mapId;
    
    public List<String> getEnemyPlaneTypesInMission()
    {
        return enemyPlaneTypesInMission;
    }
    
    public void setEnemyPlaneTypesInMission(List<String> enemyPlanesInMission)
    {
        this.enemyPlaneTypesInMission = enemyPlanesInMission;
    }
    
    public FrontMapIdentifier getMapId()
    {
        return mapId;
    }
    
    public void setMapId(FrontMapIdentifier mapId)
    {
        this.mapId = mapId;
    }

    
}

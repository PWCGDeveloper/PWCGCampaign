package pwcg.aar.prelim.claims;

import java.util.ArrayList;
import java.util.List;

public class AARClaimPanelData
{
    private List<String> enemyPlaneTypesInMission = new ArrayList<>();
    
    public List<String> getEnemyPlaneTypesInMission()
    {
        return enemyPlaneTypesInMission;
    }
    
    public void setEnemyPlaneTypesInMission(List<String> enemyPlanesInMission)
    {
        this.enemyPlaneTypesInMission = enemyPlanesInMission;
    }
}

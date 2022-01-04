package pwcg.aar.prelim.claims;

import java.util.ArrayList;
import java.util.List;

public class AARClaimPanelData
{
    private List<String> enemyTankTypesInMission = new ArrayList<>();
    
    public List<String> getEnemyTankTypesInMission()
    {
        return enemyTankTypesInMission;
    }
    
    public void setEnemyTankTypesInMission(List<String> enemyPlanesInMission)
    {
        this.enemyTankTypesInMission = enemyPlanesInMission;
    }
}

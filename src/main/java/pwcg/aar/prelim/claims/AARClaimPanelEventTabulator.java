package pwcg.aar.prelim.claims;

import java.util.List;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;

public class AARClaimPanelEventTabulator 
{
    private Campaign campaign;
    
    private AARClaimPanelData claimPanelData = new AARClaimPanelData();
    private AARPreliminaryData aarPreliminarytData;
    private Side side;

    public AARClaimPanelEventTabulator (Campaign campaign, AARPreliminaryData aarPreliminarytData, Side side)
    {
        this.campaign = campaign;
        this.aarPreliminarytData = aarPreliminarytData;
        this.side = side;
    }
        
    public AARClaimPanelData tabulateForAARClaimPanel() throws PWCGException
    {
        makeEnemyAircraftList();
        return claimPanelData;
    }

    private void makeEnemyAircraftList() throws PWCGException
    {
        PwcgMissionDataEvaluator missionDatavaluator = new PwcgMissionDataEvaluator(campaign, aarPreliminarytData);
        List<String> enemyPlanesInMission = missionDatavaluator.determineAxisTankTypesInMission();
        if (side == Side.AXIS)
        {
            enemyPlanesInMission = missionDatavaluator.determineAlliedTankTypesInMission();
        }
        
        claimPanelData.setEnemyTankTypesInMission(enemyPlanesInMission);
    }
}

package pwcg.aar.prelim.claims;

import java.util.List;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class AARClaimPanelEventTabulator 
{
    private Campaign campaign;
    
    private AARClaimPanelData claimPanelData = new AARClaimPanelData();
    private AARPreliminaryData aarPreliminarytData;

    public AARClaimPanelEventTabulator (Campaign campaign, AARPreliminaryData aarPreliminarytData)
    {
        this.campaign = campaign;
        this.aarPreliminarytData = aarPreliminarytData;
    }
        
    public AARClaimPanelData tabulateForAARClaimPanel() throws PWCGException
    {
        makeEnemyAircraftList();
        getMissionMapIdentifier();

        return claimPanelData;
    }

    private void getMissionMapIdentifier()
    {
        String mapName = aarPreliminarytData.getPwcgMissionData().getMissionHeader().getMapName();
        FrontMapIdentifier mapId = PWCGMap.getFrontMapIdentifierForName(mapName);
        claimPanelData.setMapId(mapId);
    }

    private void makeEnemyAircraftList() throws PWCGException
    {
        PwcgMissionDataEvaluator missionDatavaluator = new PwcgMissionDataEvaluator(campaign, aarPreliminarytData);
        List<String> enemyPlanesInMission = missionDatavaluator.determineAxisPlaneTypesInMission();
        SquadronMember singlePlayer = PWCGContextManager.getInstance().getReferencePlayer();
        if (singlePlayer.determineSquadron().determineSquadronCountry(campaign.getDate()).getSide() == Side.AXIS)
        {
            enemyPlanesInMission = missionDatavaluator.determineAlliedPlaneTypesInMission();
        }
        
        claimPanelData.setEnemyPlaneTypesInMission(enemyPlanesInMission);
    }
}

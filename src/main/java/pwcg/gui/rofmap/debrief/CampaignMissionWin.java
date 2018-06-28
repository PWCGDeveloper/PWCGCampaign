package pwcg.gui.rofmap.debrief;

import java.util.List;
import java.util.Map;

import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;

public class CampaignMissionWin
{
    private AARCombatReportPanelData combatResultsForMission;

    public CampaignMissionWin (AARCombatReportPanelData campaignUpdateResults)
    {
        this.combatResultsForMission = campaignUpdateResults;
    }
    
    public boolean isMissionAWin()
    {
        Map<Integer, PilotStatusEvent> squadronMembersLost = combatResultsForMission.getSquadronMembersLostInMission();
        List<VictoryEvent> victoriesScored = combatResultsForMission.getVictoriesForSquadronMembersInMission();
        
        if (squadronMembersLost.size() == 0)
        {
            return true;
        }
        
        if (squadronMembersLost.size() > victoriesScored.size())
        {
            return false;
        }

        return true;
    }
}

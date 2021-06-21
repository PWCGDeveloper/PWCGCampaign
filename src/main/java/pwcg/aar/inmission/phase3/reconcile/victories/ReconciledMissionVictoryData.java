package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.squadmember.Victory;

public class ReconciledMissionVictoryData
{
    private Map<Integer, List<Victory>> victoryAwardByPilot = new HashMap<>();
    private List<ClaimDeniedEvent> playerClaimsDenied = new ArrayList<>();
    
    public void addVictoryAwards(Map<Integer, List<Victory>> victoryAwards)
    {
        this.victoryAwardByPilot.putAll(victoryAwards);
    }

    public void addClaimDenied(ClaimDeniedEvent deniedClaim)
    {
        playerClaimsDenied.add(deniedClaim);
    }

    public Map<Integer, List<Victory>> getVictoryAwardsByPilot()
    {
        return victoryAwardByPilot;
    }

    public List<Victory> getVictoryAwardsForPilot(Integer serialNumber)
    {
        List<Victory> victoriesForPilot = new ArrayList<Victory>();
        if (victoryAwardByPilot.containsKey(serialNumber))
        {
            victoriesForPilot = victoryAwardByPilot.get(serialNumber);
        }
        return victoriesForPilot;
    }

    public List<ClaimDeniedEvent> getPlayerClaimsDenied()
    {
        return playerClaimsDenied;
    }
}

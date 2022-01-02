package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.crewmember.Victory;

public class ReconciledMissionVictoryData
{
    private Map<Integer, List<Victory>> victoryAwardByCrewMember = new HashMap<>();
    private List<ClaimDeniedEvent> playerClaimsDenied = new ArrayList<>();
    
    public void addVictoryAwards(Map<Integer, List<Victory>> victoryAwards)
    {
        this.victoryAwardByCrewMember.putAll(victoryAwards);
    }

    public void addClaimDenied(ClaimDeniedEvent deniedClaim)
    {
        playerClaimsDenied.add(deniedClaim);
    }

    public Map<Integer, List<Victory>> getVictoryAwardsByCrewMember()
    {
        return victoryAwardByCrewMember;
    }

    public List<ClaimDeniedEvent> getPlayerClaimsDenied()
    {
        return playerClaimsDenied;
    }
}

package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.squadron.Squadron;

public class MissionSquadronRecorder
{
    private Map<Integer, Squadron> squadronsInUse = new HashMap<>();
    
    public void registerSquadronInUse(Squadron squadron)
    {
        squadronsInUse.put(squadron.getSquadronId(), squadron);
    }

    public boolean isSquadronInUse(int squadronId)
    {
        return squadronsInUse.containsKey(squadronId);
    }

    public List<Squadron> getSquadronsInUse()
    {
        return new ArrayList<Squadron>(squadronsInUse.values());
    }
}

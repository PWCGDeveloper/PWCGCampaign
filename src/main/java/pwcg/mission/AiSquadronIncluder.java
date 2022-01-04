package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

public class AiSquadronIncluder
{
    private Campaign campaign;
    private Mission mission;
    private List<Company> squadronsForMission = new ArrayList<>();

    AiSquadronIncluder (Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public List<Company> decideSquadronsForMission() throws PWCGException
    {
        MissionAiSquadronFinder missionSquadronFinder = new MissionAiSquadronFinder(campaign, mission);
        missionSquadronFinder.findAiSquadronsForMission();
        decideSquadronsFromSquadronSet(missionSquadronFinder.getAxisSquads());
        decideSquadronsFromSquadronSet(missionSquadronFinder.getAlliedSquads());
        return squadronsForMission;
    }
    
    private void decideSquadronsFromSquadronSet(List<Company> squads) throws PWCGException 
    {
        for (Company squadron : squads)
        {
            squadronsForMission.add(squadron);
        }
    }
}

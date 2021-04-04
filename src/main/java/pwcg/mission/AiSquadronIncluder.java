package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class AiSquadronIncluder
{
    private Campaign campaign;
    private Mission mission;
    private List<Squadron> squadronsForMission = new ArrayList<>();

    AiSquadronIncluder (Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public List<Squadron> decideSquadronsForMission() throws PWCGException
    {
        MissionAiSquadronFinder missionSquadronFinder = new MissionAiSquadronFinder(campaign, mission);
        missionSquadronFinder.findAiSquadronsForMission();
        decideSquadronsFromSquadronSet(missionSquadronFinder.getAxisSquads());
        decideSquadronsFromSquadronSet(missionSquadronFinder.getAlliedSquads());
        return squadronsForMission;
    }
    
    private void decideSquadronsFromSquadronSet(List<Squadron> squads) throws PWCGException 
    {
        for (Squadron squadron : squads)
        {
            if (squadronWillGenerateAFlight(squadron))
            {
                squadronsForMission.add(squadron);
            }
        }
    }

    private boolean squadronWillGenerateAFlight(Squadron squadron) throws PWCGException
    {
        if (!squadronIsInRange(squadron))
        {
            return false;
        }

        return true;
    }

    private boolean squadronIsInRange(Squadron squadron) throws PWCGException 
    {
        return SquadronRange.positionIsInRange(campaign, squadron, mission.getMissionBorders().getCenter());
    }
}

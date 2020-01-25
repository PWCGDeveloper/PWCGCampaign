package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

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
        MissionSquadronFinder missionSquadronFinder = new MissionSquadronFinder(campaign, mission);
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
        if (squadronHasPlayerFlight(squadron.getSquadronId()))
        {
            return false;
        }

        if (mission.isNightMission() && squadron.getNightOdds(campaign.getDate()) == 0)
        {
            return false;
        }
        
        if (!squadronIsInRange(squadron))
        {
            return false;
        }

        if (!squadron.isSquadronViable(campaign))
        {
            return false;
        }

        return true;
    }
    
    private boolean squadronHasPlayerFlight(int squadronId)
    {
        for (IFlight playerFlight: mission.getMissionFlightBuilder().getPlayerFlights())
        {
            if (playerFlight.getSquadron().getSquadronId() == squadronId)
            {
                return true;
            }

        }

        return false;
    }

    private boolean squadronIsInRange(Squadron squadron) throws PWCGException 
    {
        return SquadronRange.positionIsInRange(campaign, squadron, mission.getMissionBorders().getCenter());
    }
}

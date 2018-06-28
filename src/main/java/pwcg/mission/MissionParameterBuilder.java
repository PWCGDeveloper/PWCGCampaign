package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class MissionParameterBuilder
{
    public  MissionSquadronFinder createMissionParameters(Campaign campaign) throws PWCGException
    {
        MissionSquadronFinder missionSquadronFinder = new MissionSquadronFinder(campaign);

        Squadron squad =  campaign.determineSquadron();

        if (squad.determineIsNightSquadron())
        {
            missionSquadronFinder.findSquadronsForNightMission();
        }
        else if (squad.isSquadronThisRole(campaign.getDate(), Role.ROLE_SEA_PLANE))
        {
            missionSquadronFinder.findSquadronsForSeaPlaneMission(campaign.getDate());
        }
        else if (squad.isSquadronThisRole(campaign.getDate(), Role.ROLE_STRAT_BOMB))
        {
            missionSquadronFinder.findSquadronsForStrategicMission(campaign.getDate());
        }
        else
        {
            missionSquadronFinder.findSquadronsForMission(campaign.getDate());
        }
        
        return missionSquadronFinder;
    }


}

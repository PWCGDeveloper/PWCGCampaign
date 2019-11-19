package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionProfileGenerator
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    
    public MissionProfileGenerator(Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }
    
    public MissionProfile generateMissionProfile() throws PWCGException
    {
        MissionProfile missionProfile = MissionProfile.DAY_TACTICAL_MISSION;
        if (isMissionNightMission()) 
        {
            missionProfile = MissionProfile.NIGHT_TACTICAL_MISSION;
        }
        return missionProfile;
    }

    private boolean isMissionNightMission() throws PWCGException
    {
        int nightMissionOdds = getNightMissionOdds();        
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < nightMissionOdds)
        {
            return true;
        }

        return false;
    }

    private int getNightMissionOdds() throws PWCGException
    {
        int nightMissionOdds = 100;
        for (Integer squadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            int squadronNightOdds = squadron.getNightOdds(campaign.getDate());
            if (squadronNightOdds < nightMissionOdds)
            {
                nightMissionOdds = squadronNightOdds;
            }
        }
        return nightMissionOdds;
    }
}

package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Company;
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
        MissionProfile missionProfile =  determineProfile();
        missionProfile = convertToNightMission(missionProfile);
        return missionProfile;
    }

    private MissionProfile determineProfile() throws PWCGException
    {
        return MissionProfile.DAY_TACTICAL_MISSION;
    }

    private MissionProfile convertToNightMission(MissionProfile missionProfile) throws PWCGException
    {
        if (isMissionNightMission()) 
        {
            if (missionProfile == MissionProfile.DAY_TACTICAL_MISSION)
            {
                return MissionProfile.NIGHT_TACTICAL_MISSION;
            }
            
            if (missionProfile == MissionProfile.DAY_STRATEGIC_MISSION)
            {
                return MissionProfile.NIGHT_STRATEGIC_MISSION;
            }
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
            Company squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            int squadronNightOdds = squadron.getNightOdds(campaign.getDate());
            if (squadronNightOdds < nightMissionOdds)
            {
                nightMissionOdds = squadronNightOdds;
            }
        }
        return nightMissionOdds;
    }
}

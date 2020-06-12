package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
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
        MissionProfile missionProfile =  determineProfile();
        missionProfile = convertToNightMission(missionProfile);
        return missionProfile;
    }

    private MissionProfile determineProfile() throws PWCGException
    {
        boolean useTactical = useTacticalProfile();
        if (useTactical)
        {
            return MissionProfile.DAY_TACTICAL_MISSION;
        }
        else 
        {
            return MissionProfile.DAY_STRATEGIC_MISSION;
        }
    }

    private boolean useTacticalProfile() throws PWCGException
    {
        boolean useTactical = false;
        for (SquadronMember player : participatingPlayers.getAllParticipatingPlayers())
        {
            Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            Role squadronPrimaryRole = playerSquadron.determineSquadronPrimaryRole(campaign.getDate());
            if (!(squadronPrimaryRole == Role.ROLE_STRATEGIC_INTERCEPT || squadronPrimaryRole == Role.ROLE_STRAT_BOMB))
            {
                useTactical = true;
            }
        }
        return useTactical;
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

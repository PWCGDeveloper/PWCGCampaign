package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.preference.TargetPreference;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.factory.AntiShippingCapability;
import pwcg.mission.target.TargetType;

public class MissionProfileGenerator
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    private MissionInfo missionInfo = new MissionInfo();
    
    public MissionProfileGenerator(Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }
    
    public MissionProfile generateMissionProfile() throws PWCGException
    {
        createMissionParticipantProfile();
        MissionProfile missionProfile =  determineProfile();
        missionProfile = convertToNightMission(missionProfile);
        return missionProfile;
    }

    private void createMissionParticipantProfile() throws PWCGException
    {

        for (SquadronMember player : participatingPlayers.getAllParticipatingPlayers())
        {
            Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            Role squadronPrimaryRole = playerSquadron.determineSquadronPrimaryRole(campaign.getDate());
            if (squadronPrimaryRole == Role.ROLE_STRATEGIC_INTERCEPT)
            {
                missionInfo.shouldUseStrategicBox = true;
            }
            else if (squadronPrimaryRole == Role.ROLE_STRAT_BOMB || squadronPrimaryRole == Role.ROLE_BOMB)
            {
                missionInfo.canUseStrategicBox = true;
            }
            else if (AntiShippingCapability.canFlyAntiShipping(campaign, playerSquadron))
            {
                missionInfo.canUseAntiShippingBox = true;
            }
            else
            {
                missionInfo.isNotSpecialized = true;
            }
            
            if (playerSquadron.determineSide() == Side.ALLIED)
            {
                missionInfo.missionIsAllied = true;
            }
            else
            {
                missionInfo.missionIsAxis = true;
            }
        }
    }
    
    private MissionProfile determineProfile() throws PWCGException
    {
        if ((missionInfo.missionIsAllied && missionInfo.missionIsAxis))
        {
            return MissionProfile.DAY_TACTICAL_MISSION;
        }

        Side side = Side.AXIS;
        if (missionInfo.missionIsAllied)
        {
            side = Side.ALLIED;
        }

        TargetPreference targetPreference = PWCGContext.getInstance().getCurrentMap().getTargetPreferenceManager().getTargetPreference(campaign, side);
        if (missionInfo.canUseAntiShippingBox && !missionInfo.isNotSpecialized)
        {
            if (targetPreference.getTargetType() == TargetType.TARGET_SHIPPING)
            {
                int roll = RandomNumberGenerator.getRandom(100);
                if (roll < targetPreference.getOddsOfUse())
                {
                    return MissionProfile.ANTI_SHIPPING_MISSION;
                }
            }
        }
        
        if (missionInfo.shouldUseStrategicBox)
        {
            return MissionProfile.DAY_STRATEGIC_MISSION;
        }
        
        if (missionInfo.canUseStrategicBox)
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 20)
            {
                return MissionProfile.DAY_STRATEGIC_MISSION;
            }
        }
        
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
        int nightMissionOdds = 0;
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
    
    private class MissionInfo
    {
        boolean shouldUseStrategicBox = false;
        boolean canUseStrategicBox = false;
        boolean canUseAntiShippingBox = false;
        boolean isNotSpecialized = false;
        
        boolean missionIsAllied = false;
        boolean missionIsAxis = false;
    }
}

package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;

public class FCFlightTypeFactory implements IFlightTypeFactory
{
    protected Campaign campaign;
    
    public FCFlightTypeFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    @Override
    public FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        Role missionRole = squadron.getSquadronRoles().selectRoleForMission(campaign.getDate());

        if (missionRole == Role.ROLE_BOMB || missionRole == Role.ROLE_ARTILLERY_SPOT || missionRole == Role.ROLE_RECON)
        {
            return getBomberFlightType();
        }
        else if (missionRole == Role.ROLE_FIGHTER)
        {
            return getFighterFlightType(squadron, isPlayerFlight);
        }
        else if (missionRole == Role.ROLE_ATTACK)
        {
            return getAttackFlightType();
        }
        else
        {
            throw new PWCGMissionGenerationException("No valid role for squadron: " + squadron.determineDisplayName(campaign.getDate()));
        }
    }

    private FlightTypes getFighterFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        FlightTypes flightType = FlightTypes.PATROL;

        int offensiveMissionOdds = 0;
        int interceptMissionOdds = 0;
        int balloonBustMissionOdds = 0;
        int balloonDefenseMissionOdds = 0;
        int escortMissionOdds = 0;
        int patrolMissionOdds = 0;
        int lowAltPatrolMissionOdds = 0;
        int lowAltCapMissionOdds = 0;

        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            offensiveMissionOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedOffensiveMissionKey);
            interceptMissionOdds = offensiveMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedInterceptMissionKey);
            balloonBustMissionOdds = interceptMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedBalloonBustMissionKey);
            balloonDefenseMissionOdds = balloonBustMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedBalloonDefenseMissionKey);
            escortMissionOdds = balloonDefenseMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedEscortMissionKey);
            patrolMissionOdds = escortMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedPatrolMissionKey);
            lowAltPatrolMissionOdds = patrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedLowAltPatrolMissionKey);
            lowAltCapMissionOdds = lowAltPatrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedLowAltCapMissionKey);
        }
        else
        {
            offensiveMissionOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisOffensiveMissionKey);
            interceptMissionOdds = offensiveMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisInterceptMissionKey);
            balloonBustMissionOdds = interceptMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisBalloonBustMissionKey);
            balloonDefenseMissionOdds = balloonBustMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisBalloonDefenseMissionKey);
            escortMissionOdds = balloonDefenseMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisEscortMissionKey);
            patrolMissionOdds = escortMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisPatrolMissionKey);
            lowAltPatrolMissionOdds = patrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisLowAltPatrolMissionKey);
            lowAltCapMissionOdds = lowAltPatrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisLowAltCapMissionKey);
        }

        int missionOdds = RandomNumberGenerator.getRandom(lowAltCapMissionOdds);

        if (missionOdds < offensiveMissionOdds)
        {
            flightType = FlightTypes.OFFENSIVE;
        }
        else if (missionOdds < interceptMissionOdds)
        {
            flightType = FlightTypes.INTERCEPT;
        }
        else if (missionOdds < balloonBustMissionOdds)
        {
            flightType = FlightTypes.BALLOON_BUST;
        }
        else if (missionOdds < balloonDefenseMissionOdds)
        {
            flightType = FlightTypes.BALLOON_DEFENSE;
        }
        else if (missionOdds < escortMissionOdds)
        {
            if (isPlayerFlight)
            {
                flightType = FlightTypes.ESCORT;
            }
            else
            {
                flightType = FlightTypes.PATROL;
            }
        }
        else if (missionOdds < patrolMissionOdds)
        {
            flightType = FlightTypes.PATROL;
        }
        else if (missionOdds < lowAltPatrolMissionOdds)
        {
            flightType = FlightTypes.LOW_ALT_PATROL;
        }
        else if (missionOdds < lowAltCapMissionOdds)
        {
            flightType = FlightTypes.LOW_ALT_CAP;
        }
        else
        {
            flightType = FlightTypes.PATROL;
        }

        return flightType;
    }

    private FlightTypes getAttackFlightType() throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.GROUND_ATTACK;

        return flightType;
    }

    private FlightTypes getBomberFlightType() 
                        throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.BOMB;
        int missionOdds = RandomNumberGenerator.getRandom(100);
        if (missionOdds < 15)
        {
            flightType = FlightTypes.LOW_ALT_BOMB;
        }

        return flightType;
    }
}

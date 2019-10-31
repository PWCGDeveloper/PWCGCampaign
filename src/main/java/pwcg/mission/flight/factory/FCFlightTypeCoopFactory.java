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

public class FCFlightTypeCoopFactory implements IFlightTypeFactory
{
    protected Campaign campaign;
    
    public FCFlightTypeCoopFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    @Override
    public FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        Role missionRole = squadron.getSquadronRoles().selectRoleForMission(campaign.getDate());

        if (missionRole == Role.ROLE_BOMB || missionRole == Role.ROLE_RECON || missionRole == Role.ROLE_ARTILLERY_SPOT)
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
        int patrolMissionOdds = 0;
        int lowAltPatrolMissionOdds = 0;
        int lowAltCapMissionOdds = 0;

        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            offensiveMissionOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedOffensiveMissionKey);
            interceptMissionOdds = offensiveMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedInterceptMissionKey);
            patrolMissionOdds = interceptMissionOdds
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
            patrolMissionOdds = interceptMissionOdds
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

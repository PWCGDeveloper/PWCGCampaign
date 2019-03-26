package pwcg.mission.flight.factory;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;


public class RoFFlightFactory extends FlightFactory
{
    public RoFFlightFactory (Campaign campaign) 
    {
        super(campaign);
    }


    @Override
    protected FlightTypes getActualFlightType(Squadron squadron, Date date, boolean isMyFlight) 
                    throws PWCGException
    {
        Role missionRole = squadron.getSquadronRoles().selectRoleForMission(date);

        if (missionRole == Role.ROLE_BOMB)
        {
            return getBomberFlightType();
        }
        else if (missionRole == Role.ROLE_RECON)
        {
            return getReconFlightType(isMyFlight);
        }
        else if (missionRole == Role.ROLE_ARTILLERY_SPOT)
        {
            return getArtillerySpotType(isMyFlight);
        }
        else if (missionRole == Role.ROLE_STRAT_BOMB)
        {
            return getStrategicBomberFlightType();
        }
        else if (missionRole == Role.ROLE_FIGHTER)
        {
            return getFighterFlightType(squadron, isMyFlight);
        }
        else if (missionRole == Role.ROLE_ATTACK)
        {
            return getAttackFlightType();
        }
        else if (Role.isRoleSeaPlane(missionRole))
        {
            if (squadron.determineSquadronPrimaryRole(campaign.getDate()) == Role.ROLE_SEA_PLANE_SMALL)
            {
                return getSmallSeaPlaneFlightType();
            }
            else
            {
                return getLargeSeaPlaneFlightType();
            }
        }
        else
        {
            throw new PWCGMissionGenerationException("No valid role for squadron: " + squadron.determineDisplayName(date));
        }
    }

    protected FlightTypes getFighterFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        FlightTypes flightType = FlightTypes.PATROL;

        // The odds of a particular type of mission
        int offensiveMissionOdds = 0;
        int interceptMissionOdds = 0;
        int balloonBustMissionOdds = 0;
        int balloonDefenseMissionOdds = 0;
        int escortMissionOdds = 0;
        int scrambleMissionOdds = 0;
        int patrolMissionOdds = 0;
        int lowAltPatrolMissionOdds = 0;
        int lowAltCapMissionOdds = 0;

        if (isPlayerFlight)
        {
            if (squadron.isHomeDefense(campaign.getDate()))
            {
                return FlightTypes.HOME_DEFENSE;
            }
        }

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
            scrambleMissionOdds = escortMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedScrambleMissionKey);
            patrolMissionOdds = scrambleMissionOdds
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
            scrambleMissionOdds = escortMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisScrambleMissionKey);
            patrolMissionOdds = scrambleMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisPatrolMissionKey);
            lowAltPatrolMissionOdds = patrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisLowAltPatrolMissionKey);
            lowAltCapMissionOdds = lowAltPatrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisLowAltPatrolMissionKey);
        }

        // Missions
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
            if (isPlayerFlight)
            {
                flightType = FlightTypes.BALLOON_DEFENSE;
            }
            else
            {
                flightType = FlightTypes.PATROL;
            }
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
        else if (missionOdds < scrambleMissionOdds)
        {
            if (isPlayerFlight)
            {
                flightType = FlightTypes.SCRAMBLE;
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

    protected FlightTypes getReconFlightType(boolean isPlayerFlight) throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.RECON;
        int spyMissionOdds = 5;
        int contactPatrolOdds = 20;

        int missionOddsRoll = RandomNumberGenerator.getRandom(100);
        if (missionOddsRoll < spyMissionOdds)
        {
            if (isPlayerFlight)
            {
                flightType = FlightTypes.SPY_EXTRACT;
            }
            else
            {
                flightType = FlightTypes.RECON;
            }
        }
        else if (missionOddsRoll < contactPatrolOdds)
        {
            flightType = FlightTypes.CONTACT_PATROL;
        }
        else
        {
            flightType = FlightTypes.RECON;
        }

        return flightType;
    }
    

    private FlightTypes getArtillerySpotType(boolean isMyFlight)
    {
        FlightTypes flightType = FlightTypes.ARTILLERY_SPOT;
        return flightType;
    }


    protected FlightTypes getAttackFlightType() 
    {
        FlightTypes flightType = FlightTypes.GROUND_ATTACK;
        return flightType;
    }

    protected FlightTypes getBomberFlightType() throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.BOMB;
        int missionOdds = RandomNumberGenerator.getRandom(100);
        if (missionOdds < 20)
        {
            flightType = FlightTypes.LOW_ALT_BOMB;
        }

        return flightType;
    }

    protected FlightTypes getStrategicBomberFlightType() 
    {
        FlightTypes flightType = FlightTypes.STRATEGIC_BOMB;

        FrontMapIdentifier mapId = PWCGContextManager.getInstance().getCurrentMap().getMapIdentifier();
        if (mapId == FrontMapIdentifier.CHANNEL_MAP)
        {
            int missionOddsRoll = RandomNumberGenerator.getRandom(100);
            int antiShippingMissionOdds = 20;
            if (missionOddsRoll < antiShippingMissionOdds)
            {
                flightType = FlightTypes.ANTI_SHIPPING;
            }
        }

        return flightType;
    }

    private FlightTypes getSmallSeaPlaneFlightType() throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.SEA_PATROL;
        int antiShippingOdds = 40;
        int missionOddsRoll = RandomNumberGenerator.getRandom(100);
        if (missionOddsRoll < antiShippingOdds)
        {
            flightType = FlightTypes.ANTI_SHIPPING;
        }
        else
        {
            flightType = FlightTypes.SEA_PATROL;
        }

        return flightType;
    }

    private FlightTypes getLargeSeaPlaneFlightType() 
    {
        return FlightTypes.ANTI_SHIPPING;
    }
}

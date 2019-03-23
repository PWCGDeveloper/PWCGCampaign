package pwcg.mission.flight.factory;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;

public class BoSFlightFactory extends FlightFactory
{
    public BoSFlightFactory (Campaign campaign) 
    {
        super(campaign);
    }

    @Override
    protected FlightTypes getActualFlightType(Squadron squadron, Date date, boolean isPlayerFlight) throws PWCGException
    {
        Role missionRole = squadron.getSquadronRoles().selectRoleForMission(date);

        if (missionRole == Role.ROLE_STRAT_BOMB)
        {
            return getStrategicBomberFlightType();
        }
        else if (missionRole == Role.ROLE_DIVE_BOMB)
        {
            return getDiveBomberFlightType();
        }
        else if (missionRole == Role.ROLE_BOMB)
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
        else if (missionRole == Role.ROLE_TRANSPORT)
        {
            return getTransportFlightType(isPlayerFlight);
        }
        else
        {
            throw new PWCGMissionGenerationException("No valid role for squadron: " + squadron.determineDisplayName(date));
        }
    }

    protected FlightTypes getFighterFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        FlightTypes flightType = FlightTypes.PATROL;

        int offensiveMissionOdds = 0;
        int interceptMissionOdds = 0;
        int escortMissionOdds = 0;
        int scrambleMissionOdds = 0;
        int patrolMissionOdds = 0;
        int lowAltPatrolMissionOdds = 0;
        int capMissionOdds = 0;

        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            offensiveMissionOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedOffensiveMissionKey);
            interceptMissionOdds = offensiveMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedInterceptMissionKey);
            escortMissionOdds = interceptMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedEscortMissionKey);
            scrambleMissionOdds = escortMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedScrambleMissionKey);
            patrolMissionOdds = scrambleMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedPatrolMissionKey);
            lowAltPatrolMissionOdds = patrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedLowAltPatrolMissionKey);
            capMissionOdds = lowAltPatrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedCAPMissionKey);
        }
        else
        {
            offensiveMissionOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisOffensiveMissionKey);
            interceptMissionOdds = offensiveMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisInterceptMissionKey);
            escortMissionOdds = interceptMissionOdds
                            + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisEscortMissionKey);
            scrambleMissionOdds = escortMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisScrambleMissionKey);
            patrolMissionOdds = scrambleMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisPatrolMissionKey);
            lowAltPatrolMissionOdds = patrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisLowAltPatrolMissionKey);
            capMissionOdds = lowAltPatrolMissionOdds
                    + campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisCAPMissionKey);
        }

        int missionOdds = RandomNumberGenerator.getRandom(capMissionOdds);

        if (missionOdds < offensiveMissionOdds)
        {
            flightType = FlightTypes.OFFENSIVE;
        }
        else if (missionOdds < interceptMissionOdds)
        {
            flightType = FlightTypes.INTERCEPT;
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
            if (isPlayerFlight && !campaign.getCampaignData().isCoop())
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
        else
        {
            flightType = FlightTypes.LOW_ALT_CAP;
        }

        return flightType;
    }
    

    private FlightTypes getTransportFlightType(boolean isPlayerFlight)
    {
        if (!isPlayerFlight)
        {
            return FlightTypes.TRANSPORT;
        }
        
        FlightTypes flightType = FlightTypes.TRANSPORT;
        int missionOdds = RandomNumberGenerator.getRandom(100);

        int spyMissionOdds = 5;
        int paratroopMissionOdds = spyMissionOdds + 15;
        int cargoDropMissionOdds = paratroopMissionOdds + 15;
        if (missionOdds < spyMissionOdds)
        {
            flightType = FlightTypes.SPY_EXTRACT;
        }
        else if (missionOdds < paratroopMissionOdds)
        {
            flightType = FlightTypes.PARATROOP_DROP;
        }
        else if (missionOdds < cargoDropMissionOdds)
        {
            flightType = FlightTypes.CARGO_DROP;
        }

        return flightType;
    }


    protected FlightTypes getAttackFlightType() throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.GROUND_ATTACK;

        return flightType;
    }

    protected FlightTypes getBomberFlightType() 
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

    protected FlightTypes getDiveBomberFlightType() 
                        throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.DIVE_BOMB;


        return flightType;
    }

    protected FlightTypes getStrategicBomberFlightType() 
    {
        FlightTypes flightType = FlightTypes.STRATEGIC_BOMB;

        return flightType;
    }

}

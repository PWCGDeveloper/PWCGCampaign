package pwcg.mission.flight.factory;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionAntiShippingSeaLaneFinder;
import pwcg.mission.SquadronRange;
import pwcg.mission.flight.FlightTypes;

public class BoSFlightTypeSpecialFactory implements IFlightTypeFactory
{
    private Campaign campaign;
    private ConfigManagerCampaign configManager;
    
    public BoSFlightTypeSpecialFactory (Campaign campaign) 
    {
        this.campaign = campaign;
        this.configManager = campaign.getCampaignConfigManager();
    }
    
    @Override
    public FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        Role missionRole = squadron.getSquadronRoles().selectRoleForMission(campaign.getDate());

        if (missionRole == Role.ROLE_BOMB || missionRole == Role.ROLE_ATTACK)
        {
            return getBomberFlightType(squadron);
        }
        else if (missionRole == Role.ROLE_FIGHTER)
        {
            return getFighterFlightType(squadron);
        }

        return FlightTypes.ANY;
    }

    private FlightTypes getBomberFlightType(Squadron squadron) throws PWCGException
    {
        if (canFlyAntiShipping(squadron))
        {
            int antiShippingOdds = getAntiShippingProbability(squadron);
            int antiShippingRoll = RandomNumberGenerator.getRandom(100);
            if (antiShippingRoll <= antiShippingOdds)
            {
                if (squadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_BOMB))
                {
                    return FlightTypes.ANTI_SHIPPING_BOMB;
                }
                else if (squadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_ATTACK))
                {
                    return FlightTypes.ANTI_SHIPPING_ATTACK;
                }
                else if (squadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_DIVE_BOMB))
                {
                    return FlightTypes.ANTI_SHIPPING_DIVE_BOMB;
                }
            }
        }
        return FlightTypes.ANY;
    }
    
    private boolean canFlyAntiShipping(Squadron squadron) throws PWCGException
    {
        List<FrontMapIdentifier> squadronMaps = AirfieldManager.getMapIdForAirfield(squadron.determineCurrentAirfieldName(campaign.getDate()));
        if (squadronMaps.contains(FrontMapIdentifier.KUBAN_MAP))
        {
            MissionAntiShippingSeaLaneFinder seaLaneFinder = new MissionAntiShippingSeaLaneFinder(campaign);
            ShippingLane shippingLane = seaLaneFinder.getShippingLaneForMission(squadron);
            
            Coordinate shippingLaneCenter = shippingLane.getShippingLaneBox().getCenter();
            return SquadronRange.positionIsInRange(campaign, squadron, shippingLaneCenter);
        }
        
        return false;
    }

    public FlightTypes getFighterFlightType(Squadron squadron) throws PWCGException
    {
        int acrambleOdds = getScrambleProbability(squadron);
        int scrambleRoll = RandomNumberGenerator.getRandom(100);
        if (scrambleRoll <= acrambleOdds)
        {
            return FlightTypes.SCRAMBLE;
        }
        return FlightTypes.ANY;
    }
    
    private int getAntiShippingProbability(Squadron squadron) throws PWCGException
    {
        int antiShippingOdds = configManager.getIntConfigParam(ConfigItemKeys.antiShipOddsAlliedKey);
        if (squadron.determineSide() == Side.AXIS)
        {
            antiShippingOdds = configManager.getIntConfigParam(ConfigItemKeys.antiShipOddsAxisKey);
        }
        return antiShippingOdds;
    }

    private int getScrambleProbability(Squadron squadron) throws PWCGException
    {
        int antiShippingOdds = configManager.getIntConfigParam(ConfigItemKeys.scrambleOddsAlliedKey);
        if (squadron.determineSide() == Side.AXIS)
        {
            antiShippingOdds = configManager.getIntConfigParam(ConfigItemKeys.scrambleOddsAxisKey);
        }
        return antiShippingOdds;
    }

}

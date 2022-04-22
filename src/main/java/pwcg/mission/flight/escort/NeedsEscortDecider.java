package pwcg.mission.flight.escort;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class NeedsEscortDecider
{
    public static boolean playerNeedsEscort(IFlight escortedFlight) throws PWCGException, PWCGException
    {
        if (!basicNeedsEscortTest(escortedFlight))
        {
            return false;
        }
        
        return true;
    }
    
    public static boolean aiNeedsEscort(IFlight escortedFlight) throws PWCGException, PWCGException
    {
        if (!escortedFlight.getFlightInformation().isVirtual())
        {
            return false;
        }
        
        if (!basicNeedsEscortTest(escortedFlight))
        {
            return false;
        }
        
        if (escortedFlight.getFlightInformation().getFlightType() == FlightTypes.STRATEGIC_BOMB)
        {
            return true;
        }

        int escortedOdds = getEsortOddsForFlightType(escortedFlight);
        int escortedDiceRoll = RandomNumberGenerator.getRandom(100);        
        if (escortedDiceRoll < escortedOdds)
        {
            return true;
        }
        
        return false;
    }
    
    private static int getEsortOddsForFlightType(IFlight escortedFlight) throws PWCGException
    {
        if (escortedFlight.getFlightType() == FlightTypes.RAID)
        {
            return 0;
        }
        else if (escortedFlight.getFlightType() == FlightTypes.ANTI_SHIPPING)
        {
            return 0;
        }
        else if (escortedFlight.getFlightType() == FlightTypes.STRATEGIC_BOMB)
        {
            return 80;
        }
        else if (escortedFlight.getFlightType() == FlightTypes.PARATROOP_DROP)
        {
            return 80;
        }
        else if (escortedFlight.getFlightType() == FlightTypes.CARGO_DROP)
        {
            return 80;
        }
        else if (escortedFlight.getFlightType() == FlightTypes.DIVE_BOMB)
        {
            return escortedFlight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.IsVirtualDiveBombEscortedOddsKey);
        }
        else if (escortedFlight.getFlightType() == FlightTypes.TRANSPORT)
        {
            return escortedFlight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.IsVirtualTransportEscortedOddsKey);
        }
        else if (FlightTypes.isTacticalLevelBombingFlight(escortedFlight.getFlightType()))
        {
            return escortedFlight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.IsVirtualBombingEscortedOddsKey);
        }
        else if (FlightTypes.isGroundAttackFlight(escortedFlight.getFlightType()))
        {
            return escortedFlight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.IsVirtualGroundAttackEscortedOddsKey);
        }
        
        return 0;
    }

    private static boolean basicNeedsEscortTest(IFlight escortedFlight)
    {
        if (escortedFlight.getFlightInformation().isFighterMission())
        {
            return false;
        }

        if (!FlightTypes.isFlightNeedsEscort(escortedFlight.getFlightType()))
        {
            return false;
        }

        if (escortedFlight.getMission().isNightMission())
        {
            return false;
        }

        return true;
    }
}

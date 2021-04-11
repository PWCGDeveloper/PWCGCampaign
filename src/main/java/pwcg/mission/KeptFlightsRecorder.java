package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class KeptFlightsRecorder
{
    public enum KeptFlightCountType
    {
        KEPT_FLIGHT_COUNT_FIGHTER,
        KEPT_FLIGHT_COUNT_BOMBER,
        KEPT_FLIGHT_COUNT_OTHER,
        KEPT_FLIGHT_COUNT_ALL
    }
    
    private Map<Side, KeptFlights> keptFlights = new HashMap<>();

    public KeptFlightsRecorder ()
    {
        KeptFlights alliedKeptFlights = new KeptFlights();
        KeptFlights axisKeptFlights = new KeptFlights();
        
        keptFlights.put(Side.ALLIED, alliedKeptFlights);
        keptFlights.put(Side.AXIS, axisKeptFlights);
    }
    
    public void keepFlight(IFlight keptFlight) throws PWCGException
    {
        KeptFlights keptFlightsForSide = getKeptFlightsForSide(keptFlight);
        keptFlightsForSide.keepFlight(keptFlight);
    }

    private KeptFlights getKeptFlightsForSide(IFlight keptFlight) throws PWCGException
    {
        if (keptFlight.getSquadron().determineSide() == Side.ALLIED)
        {
            return keptFlights.get(Side.ALLIED);
        }
        else
        {
            return keptFlights.get(Side.AXIS);
        }
    }

    public int getNumKeptFlightType(IFlight keptFlight) throws PWCGException
    {
        KeptFlights keptFlightsForSide = getKeptFlightsForSide(keptFlight);
        return keptFlightsForSide.getNumKeptFlightType(keptFlight.getFlightType());
    }

    public int getFlightKeptCount(KeptFlightCountType keptFlightCountType, IFlight keptFlight) throws PWCGException
    {
        KeptFlights keptFlightsForSide = getKeptFlightsForSide(keptFlight);
        return keptFlightsForSide.getFlightKeptCount(keptFlightCountType);
    }

    public List<IFlight> getKeptFlights()
    {
        List<IFlight> allKeptFlights = new ArrayList<>();
        
        KeptFlights alliedKeptFlights = keptFlights.get(Side.ALLIED);
        allKeptFlights.addAll(alliedKeptFlights.getKeptFlights());
        
        KeptFlights axisKeptFlights = keptFlights.get(Side.AXIS);
        allKeptFlights.addAll(axisKeptFlights.getKeptFlights());
        
        return allKeptFlights;
    }
    
    
    public boolean needsMoreFlights(IFlight flight) throws PWCGException
    {
        KeptFlights keptFlightsForSide = getKeptFlightsForSide(flight);
        int configuredNumFlights = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey);
        if (flight.getSquadron().determineSide() == Side.AXIS)
        {
            configuredNumFlights = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey);
        }
        
        if (keptFlightsForSide.getKeptFlights().size() < configuredNumFlights)
        {
            return true;
        }
        
        return false;
    }

    
    public boolean airfieldInUseForTakeoff(IFlight flight) throws PWCGException
    {
        KeptFlights keptFlightsForSide = getKeptFlightsForSide(flight);
        return keptFlightsForSide.airfieldInUseForTakeoff(flight);
    }

    public boolean isSquadronInKept(IFlight flight) throws PWCGException
    {
        KeptFlights keptFlightsForSide = getKeptFlightsForSide(flight);
        return keptFlightsForSide.isSquadronInKept(flight.getSquadron().getSquadronId());
    }
}

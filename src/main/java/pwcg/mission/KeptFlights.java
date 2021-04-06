package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.mission.KeptFlightsRecorder.KeptFlightCountType;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class KeptFlights
{
    private Map<Integer, IFlight> fighterFlightsKept = new HashMap<>();
    private Map<Integer, IFlight> bomberFlightsKept = new HashMap<>();
    private Map<Integer, IFlight> otherFlightsKept = new HashMap<>();
    
    private List<FlightTypes> keptFlightTypes = new ArrayList<>();

    public void keepFlight(IFlight keptFlight) throws PWCGException
    {
        if (keptFlight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
        {
            fighterFlightsKept.put(keptFlight.getSquadron().getSquadronId(), keptFlight);
        }
        else if (keptFlight.getFlightType().isCategory(FlightTypeCategory.BOMB))
        {
            bomberFlightsKept.put(keptFlight.getSquadron().getSquadronId(), keptFlight);
        }
        else
        {
            otherFlightsKept.put(keptFlight.getSquadron().getSquadronId(), keptFlight);
        }
        
        keptFlightTypes.add(keptFlight.getFlightType());
        
        keptFlight.getMission().getMissionSquadronRecorder().registerSquadronInUse(keptFlight.getSquadron());
    }
    
    public int getNumKeptFlightType(FlightTypes flightType)
    {
        int numKeptFlightsOfType = 0;
        for (FlightTypes keptFlightType : keptFlightTypes)
        {
            if (keptFlightType == flightType)
            {
                ++numKeptFlightsOfType;
            }
        }
        return numKeptFlightsOfType;
    }

    public int getFlightKeptCount(KeptFlightCountType keptFlightCountType) throws PWCGException
    {
        if (keptFlightCountType == KeptFlightCountType.KEPT_FLIGHT_COUNT_FIGHTER)
        {
            return fighterFlightsKept.size();
        }
        else if (keptFlightCountType == KeptFlightCountType.KEPT_FLIGHT_COUNT_BOMBER)
        {
            return bomberFlightsKept.size();
        }
        else if (keptFlightCountType == KeptFlightCountType.KEPT_FLIGHT_COUNT_OTHER)
        {
            return otherFlightsKept.size();
        }
        else if (keptFlightCountType == KeptFlightCountType.KEPT_FLIGHT_COUNT_ALL)
        {
            return (fighterFlightsKept.size() + bomberFlightsKept.size() + otherFlightsKept.size());
        }
        else
        {
            throw new PWCGException("Invalid kept flightcount request : " + keptFlightCountType);
        }
    }

    public List<IFlight> getKeptFlights()
    {
        List<IFlight> keptFlights = new ArrayList<>();
        
        keptFlights.addAll(fighterFlightsKept.values());
        keptFlights.addAll(bomberFlightsKept.values());
        keptFlights.addAll(otherFlightsKept.values());
        
        return keptFlights;
    }

}

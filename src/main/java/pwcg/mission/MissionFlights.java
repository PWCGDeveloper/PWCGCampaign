package pwcg.mission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class MissionFlights
{
    private Campaign campaign;
    private Mission mission;
    private List<IFlight> flights = new ArrayList<>();

    public MissionFlights(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }
    
    public void generateFlights() throws PWCGException
    {        
        AiFlightBuilder aiFlightBuilder = new AiFlightBuilder(campaign, mission);
        List<IFlight> aiFlights = aiFlightBuilder.createAiFlights(mission.getWeather());
        flights.addAll(aiFlights);
    }

    public List<IFlight> getAiFlightsForSide(Side side) throws PWCGException
    {
        List<IFlight> aiFlightsForSide = new ArrayList<IFlight>();
        for (IFlight flight : getAiFlights())
        {
            if (flight.getFlightInformation().getCountry().getSide() == side)
            {
                aiFlightsForSide.add(flight);
            }
        }
        return aiFlightsForSide;
    }

    public List<IFlight> getFlightsForSide(Side side)
    {
        List<IFlight> flightsForSide = new ArrayList<IFlight>();
        for (IFlight flight : getAllAerialFlights())
        {
            if (flight.getFlightInformation().getCountry().getSide() == side)
            {
                flightsForSide.add(flight);
            }
        }
        return flightsForSide;
    }

    public List<IFlight> getAllAerialFlights()
    {
        Set<Integer> flightIds = new HashSet<>();

        ArrayList<IFlight> allFlights = new ArrayList<IFlight>();
        for (IFlight flight : flights)
        {
            if (flight.getFlightPlanes().getPlanes().size() > 0)
            {
                if (!flightIds.contains(flight.getFlightId()))
                {
                    allFlights.add((IFlight) flight);
                    flightIds.add(flight.getFlightId());
                }
            }
        }
        
        return allFlights;
    }
    
    public List<IFlight> getAiFlights()
    {
        return flights;
    }
}

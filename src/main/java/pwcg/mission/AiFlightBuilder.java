package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.IFlight;
import pwcg.mission.options.MissionWeather;

public class AiFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private List<IFlight> missionFlights = new ArrayList<IFlight>();

    AiFlightBuilder (Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public List<IFlight> createAiFlights(MissionWeather missionWeather) throws PWCGException 
    {
        TestDriver testDriver = TestDriver.getInstance();
        if (testDriver.isCreatePlayerOnly())
        {
            return missionFlights;
        }
        
        // TODO TC change by squadron to a limited number of AI flights
        List<FlightBuildInformation> flightBuildInformationForMission = AIFlightPlanner.createFlightBuildInformationForMission(mission);

        for (FlightBuildInformation flightBuildInformation : flightBuildInformationForMission)
        {
            List<IFlight> flights = buildFlight(flightBuildInformation);
            for (IFlight flight : flights)
            {
                missionFlights.add(flight);
            }
        }
        return missionFlights;
    }

    private List<IFlight> buildFlight(FlightBuildInformation flightBuildInformation) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        List<IFlight> flights = flightFactory.buildFlight(flightBuildInformation);
        return flights;        
    }
}

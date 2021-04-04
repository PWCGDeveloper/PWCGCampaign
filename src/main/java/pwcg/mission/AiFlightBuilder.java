package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.factory.PWCGFlightTypeAbstractFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.factory.IFlightTypeFactory;
import pwcg.mission.flight.factory.NightFlightTypeConverter;
import pwcg.mission.flight.factory.WeatherFlightTypeConverter;
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
        
        AiSquadronIncluder aiSquadronIncluder = new AiSquadronIncluder(mission);
        List<Squadron> aiSquadronsForMission = aiSquadronIncluder.decideSquadronsForMission();
        
        for (Squadron squadron : aiSquadronsForMission)
        {
            FlightTypes flightType = determineFlightType(squadron);
            flightType = NightFlightTypeConverter.getFlightType(flightType, mission.isNightMission());
            flightType = WeatherFlightTypeConverter.getFlightType(flightType, missionWeather);

            IFlight flight = buildFlight(flightType, squadron);
            if (flight != null)
            {
                missionFlights.add(flight);
            }
            else
            {
                PWCGLogger.log(LogLevel.DEBUG, "Failed to generate a flight for : " + squadron.determineDisplayName(campaign.getDate()));
            }
        }
        return missionFlights;
    }

    private FlightTypes determineFlightType(Squadron squadron) throws PWCGException 
    {
        IFlightTypeFactory flightTypeFactory = makeFlightTypeFactory();
        boolean isPlayerFlight = false;
        FlightTypes flightType = flightTypeFactory.getFlightType(squadron, isPlayerFlight);
        return flightType;
    }

    private IFlight buildFlight(FlightTypes flightType, Squadron squadron) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        boolean isPlayerFlight = false;
        IFlight flight = flightFactory.buildFlight(mission, squadron, flightType, isPlayerFlight);
        return flight;        
    }

    
    private IFlightTypeFactory makeFlightTypeFactory() throws PWCGException
    {
        if (mission.getSkirmish() == null)
        {
            return PWCGFlightTypeAbstractFactory.createFlightTypeFactory(campaign);
        }
        else
        {
            return PWCGFlightTypeAbstractFactory.createSkirmishFlightTypeFactory(campaign, mission.getSkirmish());
        }
    }
}

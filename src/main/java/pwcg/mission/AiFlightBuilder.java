package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.factory.PWCGFlightTypeAbstractFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
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

    AiFlightBuilder (Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    private List<IFlight> missionFlights = new ArrayList<IFlight>();

    public List<IFlight> createAiFlights(MissionWeather missionWeather) throws PWCGException 
    {
        TestDriver testDriver = TestDriver.getInstance();
        if (testDriver.isCreatePlayerOnly())
        {
            return missionFlights;
        }
        
        List<Squadron> aiSquadronsForMission = mission.getMissionSquadronChooser().determineParticipatingSquadrons(mission);
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
        }
        return missionFlights;
    }

    private FlightTypes determineFlightType(Squadron squadron) throws PWCGException 
    {
        IFlightTypeFactory flightTypeFactory = PWCGFlightTypeAbstractFactory.createFlightFactory(campaign);
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

}

package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.factory.PWCGFlightTypeAbstractFactory;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
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

            List<IFlight> flights = buildFlight(flightType, squadron);
            for (IFlight flight : flights)
            {
                if (!cancelBecauseRare(flight))
                {
                    missionFlights.add(flight);
                }
            }
        }
        return missionFlights;
    }

    private boolean cancelBecauseRare(IFlight flight) throws PWCGException
    {
        if (flight.getSquadron().isRarePlane(campaign.getDate()))
        {
            int rarePlaneOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.OddsOfRareAircraftFlyingKey);
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < rarePlaneOdds)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    private FlightTypes determineFlightType(Squadron squadron) throws PWCGException 
    {
        IFlightTypeFactory flightTypeFactory = makeFlightTypeFactory();
        boolean isPlayerFlight = false;
        PwcgRole missionRole = MissionRoleGenerator.getMissionRole(campaign, squadron);
        FlightTypes flightType = flightTypeFactory.getFlightType(squadron, isPlayerFlight, missionRole);
        return flightType;
    }

    private List<IFlight> buildFlight(FlightTypes flightType, Squadron squadron)
    {
    	try
    	{
    		FlightFactory flightFactory = new FlightFactory(campaign);
    		List<IFlight> flights = flightFactory.buildFlight(mission, squadron, flightType, NecessaryFlightType.NONE);
    		return flights;        
    	}
    	catch(PWCGException e)
    	{
            PWCGLogger.log(LogLevel.ERROR, "Failed to create AI flight of type " + flightType + " for squadron " + squadron.getFileName());
            PWCGLogger.logException(e);
    		return new ArrayList<>();
    	}
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

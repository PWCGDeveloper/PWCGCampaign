package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.PWCGFlightTypeAbstractFactory;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.factory.IFlightTypeFactory;
import pwcg.mission.flight.factory.NightFlightTypeConverter;
import pwcg.mission.flight.factory.WeatherFlightTypeConverter;
import pwcg.mission.options.MissionWeather;

public class PlayerFlightTypeBuilder
{
    public static MissionSquadronFlightTypes buildPlayerFlightTypes(
            Campaign campaign, 
            MissionHumanParticipants participatingPlayers, 
            MissionProfile missionProfile, 
            MissionWeather weather, 
            Skirmish skirmish) throws PWCGException
    {
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        for (Integer squadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            IFlightTypeFactory flightTypeFactory = null;
            Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            if (skirmish == null)
            {
                flightTypeFactory = PWCGFlightTypeAbstractFactory.createFlightTypeFactory(campaign);
            }
            else
            {
                flightTypeFactory = PWCGFlightTypeAbstractFactory.createSkirmishFlightTypeFactory(campaign, skirmish);
            }

            FlightTypes requestedFlightType = addFlightType(missionProfile, weather, flightTypeFactory, playerSquadron);
            playerFlightTypes.add(playerSquadron, requestedFlightType);
        }
        return playerFlightTypes;
    }

    private static FlightTypes convertFlightTypeForEnvironmentalConditions(boolean isNightMission, MissionWeather weather, FlightTypes flightType)
            throws PWCGException
    {
        flightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        flightType = WeatherFlightTypeConverter.getFlightType(flightType, weather);
        return flightType;
    }

    private static FlightTypes addFlightType(MissionProfile missionProfile, MissionWeather weather, IFlightTypeFactory flightTypeFactory, Squadron playerSquadron) throws PWCGException
    {
        boolean isPlayerFlight = true;
        FlightTypes initialFlightType = flightTypeFactory.getFlightType(playerSquadron, isPlayerFlight);
        FlightTypes requestedFlightType = convertFlightTypeForEnvironmentalConditions(missionProfile.isNightMission(), weather, initialFlightType);
        return requestedFlightType;
    }
}

package pwcg.mission;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.PWCGFlightTypeAbstractFactory;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.factory.IFlightTypeFactory;
import pwcg.mission.flight.factory.WeatherFlightTypeConverter;
import pwcg.mission.options.MissionWeather;

public class PlayerFlightTypeBuilder
{
    public static MissionSquadronFlightTypes buildPlayerFlightTypes(
            Campaign campaign, 
            MissionHumanParticipants participatingPlayers, 
            MissionWeather weather, 
            Skirmish skirmish, 
            Map<Integer, PwcgRole> squadronRoleOverride) throws PWCGException
    {
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        for (Integer squadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            IFlightTypeFactory flightTypeFactory = null;
            Company playerSquadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);
            if (skirmish == null)
            {
                flightTypeFactory = PWCGFlightTypeAbstractFactory.createFlightTypeFactory(campaign);
            }
            else
            {
                flightTypeFactory = PWCGFlightTypeAbstractFactory.createSkirmishFlightTypeFactory(campaign, skirmish);
            }

            FlightTypes requestedFlightType = addFlightType(campaign, weather, flightTypeFactory, squadronRoleOverride, playerSquadron);
            playerFlightTypes.add(playerSquadron, requestedFlightType);
        }
        return playerFlightTypes;
    }

    private static FlightTypes addFlightType(
            Campaign campaign,
            MissionWeather weather, 
            IFlightTypeFactory flightTypeFactory, 
            Map<Integer, PwcgRole> squadronRoleOverride,
            Company playerSquadron) throws PWCGException
    {
        boolean isPlayerFlight = true;
        
        PwcgRole missionRole = MissionRoleGenerator.getMissionRole(campaign, squadronRoleOverride, playerSquadron);

        FlightTypes initialFlightType = flightTypeFactory.getFlightType(playerSquadron, isPlayerFlight, missionRole);
        FlightTypes requestedFlightType = convertFlightTypeForEnvironmentalConditions(weather, initialFlightType);
        return requestedFlightType;
    }

    private static FlightTypes convertFlightTypeForEnvironmentalConditions(MissionWeather weather, FlightTypes flightType)
            throws PWCGException
    {
        flightType = WeatherFlightTypeConverter.getFlightType(flightType, weather);
        return flightType;
    }
}

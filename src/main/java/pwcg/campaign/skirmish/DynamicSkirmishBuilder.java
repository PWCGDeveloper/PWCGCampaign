package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.shipping.CargoRoute;
import pwcg.campaign.shipping.CargoRouteManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionHumanParticipants;

public class DynamicSkirmishBuilder
{
    public static  List<Skirmish> getSkirmishesForDate(Campaign campaign, MissionHumanParticipants participatingPlayers) throws PWCGException 
    {     
        Skirmish cargoRouteSkirmish = buildSkirmishForCargoRoute(campaign, participatingPlayers);
        if (cargoRouteSkirmish != null)
        {
            return Arrays.asList(cargoRouteSkirmish);
        }
        else
        {
            return new ArrayList<>();
        }
    }

    private static Skirmish buildSkirmishForCargoRoute(Campaign campaign, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        Side playerSide = participatingPlayers.getMissionPlayerSides().get(0);
        Side shipSide = playerSide.getOppositeSide();
        CargoRoute cargoRoute = CargoRouteManager.getCargoRouteForSide(campaign, participatingPlayers, shipSide);

        if (cargoRoute != null)
        {
            return new Skirmish(
                    cargoRoute.getName(),
                    cargoRoute.getRouteStartPosition(), 
                    campaign.getDate(), 
                    playerSide,
                    SkirmishProfileType.SKIRMISH_PROFILE_ANTI_SHIPPING, 
                    new ArrayList<SkirmishIconicFlights>(),
                    new ArrayList<SkirmishForceRoleConversion>());
        }
        else
        {
            return null;
        }
    }
}

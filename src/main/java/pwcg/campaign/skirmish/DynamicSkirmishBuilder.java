package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.shipping.CargoRoute;
import pwcg.campaign.shipping.CargoRouteManager;
import pwcg.campaign.shipping.ShipEncounterZone;
import pwcg.campaign.shipping.ShipEncounterZoneManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionHumanParticipants;

public class DynamicSkirmishBuilder
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    
    public DynamicSkirmishBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }
    
    public  List<Skirmish> getSkirmishesForDate() throws PWCGException 
    {     
        Skirmish seaSkirmish = null;
        if (isCargo())
        {
            seaSkirmish = buildSkirmishForCargoRoute();
        }
        else
        {
            seaSkirmish = buildSkirmishForShippingEncounter();
        }

        if (seaSkirmish != null)
        {
            return Arrays.asList(seaSkirmish);
        }
        else
        {
            return new ArrayList<>();
        }
    }

    public boolean isCargo()
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 30)
        {
            return false; 
        }
        return true;
    }

    public Skirmish buildSkirmishForCargoRoute() throws PWCGException
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
    
    
    public Skirmish buildSkirmishForShippingEncounter() throws PWCGException
    {
        ShipEncounterZone shipEncounterZone = ShipEncounterZoneManager.getShipEncounterZone(campaign, participatingPlayers);
        Side playerSide = participatingPlayers.getMissionPlayerSides().get(0);

        if (shipEncounterZone != null)
        {
            return new Skirmish(
                    shipEncounterZone.getName(),
                    shipEncounterZone.getEncounterPoint(), 
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

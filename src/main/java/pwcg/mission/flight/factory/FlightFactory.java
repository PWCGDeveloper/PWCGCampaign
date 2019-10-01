package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.artySpot.ArtillerySpotPackage;
import pwcg.mission.flight.attack.GroundAttackPackage;
import pwcg.mission.flight.balloonBust.BalloonBustPackage;
import pwcg.mission.flight.balloondefense.BalloonDefensePackage;
import pwcg.mission.flight.bomb.BombingPackage;
import pwcg.mission.flight.bomb.LowAltBombingPackage;
import pwcg.mission.flight.bomb.StrategicBombingPackage;
import pwcg.mission.flight.contactpatrol.ContactPatrolPackage;
import pwcg.mission.flight.divebomb.DiveBombingPackage;
import pwcg.mission.flight.escort.PlayerEscortPackage;
import pwcg.mission.flight.ferry.FerryPackage;
import pwcg.mission.flight.intercept.HomeDefensePackage;
import pwcg.mission.flight.intercept.InterceptPackage;
import pwcg.mission.flight.lonewolf.LoneWolfPackage;
import pwcg.mission.flight.offensive.OffensivePackage;
import pwcg.mission.flight.paradrop.ParaDropPackage;
import pwcg.mission.flight.patrol.LowAltPatrolPackage;
import pwcg.mission.flight.patrol.PatrolPackage;
import pwcg.mission.flight.recon.ReconPackage;
import pwcg.mission.flight.scramble.ScramblePackage;
import pwcg.mission.flight.seapatrolantishipping.SeaAntiShippingPackage;
import pwcg.mission.flight.seapatrolscout.SeaPatrolPackage;
import pwcg.mission.flight.spy.SpyExtractPackage;
import pwcg.mission.flight.transport.TransportPackage;

public class FlightFactory
{
    protected Campaign campaign;
    
    public FlightFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }
    
    public Flight buildFlight(
    		Mission mission,
    		Squadron squadron,
    		FlightTypes flightType,
    		boolean isPlayerFlight) throws PWCGException 
    {
        Flight flight = null;
        
        FlightInformation flightInformation = createFlightInformation(mission, squadron, flightType, isPlayerFlight);

        // Flight generator
        IFlightPackage flightPackage = null;
        if (flightType == FlightTypes.ARTILLERY_SPOT)
        {
            flightPackage = new ArtillerySpotPackage(flightInformation);
        }
        else if (flightType == FlightTypes.BALLOON_BUST)
        {
            flightPackage = new BalloonBustPackage(flightInformation);
        }
        else if (flightType == FlightTypes.BALLOON_DEFENSE)
        {
            flightPackage = new BalloonDefensePackage(flightInformation);
        }
        else if (flightType == FlightTypes.BOMB)
        {
            flightPackage = new BombingPackage(flightInformation);
        }
        else if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            flightPackage = new LowAltBombingPackage(flightInformation);
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            flightPackage = new DiveBombingPackage(flightInformation);
        }
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            flightPackage = new StrategicBombingPackage(flightInformation);
        }
        else if (flightType == FlightTypes.ESCORT)
        {
            if (isPlayerFlight)
            {
                flightPackage = new PlayerEscortPackage(flightInformation);
            }
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            flightPackage = new GroundAttackPackage(flightInformation);
        }
        else if (flightType == FlightTypes.INTERCEPT)
        {
            flightPackage = new InterceptPackage(flightInformation);
        }
        else if (flightType == FlightTypes.LONE_WOLF)
        {
            flightPackage = new LoneWolfPackage(flightInformation);
        }
        else if (flightType == FlightTypes.OFFENSIVE)
        {
            flightPackage = new OffensivePackage(flightInformation);
        }
        else if (flightType == FlightTypes.PATROL)
        {
            flightPackage = new PatrolPackage(flightInformation);
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            flightPackage = new LowAltPatrolPackage(flightInformation);
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            flightPackage = new InterceptPackage(flightInformation);
        }
        else if (flightType == FlightTypes.RECON)
        {
            flightPackage = new ReconPackage(flightInformation);
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            flightPackage = new TransportPackage(flightInformation);
        }
        else if (flightType == FlightTypes.PARATROOP_DROP || flightType == FlightTypes.CARGO_DROP)
        {
            flightPackage = new ParaDropPackage(flightInformation);
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            flightPackage = new SpyExtractPackage(flightInformation);
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            flightPackage = new ContactPatrolPackage(flightInformation);
        }
        else if (flightType == FlightTypes.SCRAMBLE)
        {
            flightPackage = new ScramblePackage(flightInformation);
        }
        else if (flightType == FlightTypes.HOME_DEFENSE)
        {
            flightPackage = new HomeDefensePackage(flightInformation);
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING_BOMB || flightType == FlightTypes.ANTI_SHIPPING_ATTACK || flightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB)
        {
            flightPackage = new SeaAntiShippingPackage(flightInformation);
        }
        else if (flightType == FlightTypes.SEA_PATROL)
        {
            flightPackage = new SeaPatrolPackage(flightInformation);
        }
        else if (flightType == FlightTypes.FERRY)
        {
            flightPackage = new FerryPackage(flightInformation);
        }
        else
        {
            throw new PWCGMissionGenerationException("Invalid flight type: " + flightType);
        }

        if (flightPackage != null)
        {
            flight = flightPackage.createPackage();
        }
        
        return flight;
    }

    private FlightInformation createFlightInformation(
    		Mission mission,
    		Squadron squadron,
    		FlightTypes flightType,
    		boolean isPlayerFlight) throws PWCGException
    {
        FlightInformation flightInformation = null;
        if (isPlayerFlight)
        {
            flightInformation = FlightInformationFactory.buildPlayerFlightInformation(squadron, mission, flightType);
        }
        else
        {
            flightInformation = FlightInformationFactory.buildAiFlightInformation(squadron, mission, flightType);
        }
        return flightInformation;
    }
}

package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.Mission;
import pwcg.mission.flight.artySpot.ArtillerySpotPackage;
import pwcg.mission.flight.balloonBust.BalloonBustPackage;
import pwcg.mission.flight.balloondefense.BalloonDefensePackage;
import pwcg.mission.flight.bomb.BombingPackage;
import pwcg.mission.flight.bomb.StrategicBombingPackage;
import pwcg.mission.flight.cap.CAPPackage;
import pwcg.mission.flight.contactpatrol.ContactPatrolPackage;
import pwcg.mission.flight.divebomb.DiveBombingPackage;
import pwcg.mission.flight.escort.PlayerIsEscortPackage;
import pwcg.mission.flight.groundattack.GroundAttackPackage;
import pwcg.mission.flight.groundhunt.GroundFreeHuntPackage;
import pwcg.mission.flight.intercept.InterceptPackage;
import pwcg.mission.flight.lonewolf.LoneWolfPackage;
import pwcg.mission.flight.offensive.OffensivePackage;
import pwcg.mission.flight.paradrop.CargoDropPackage;
import pwcg.mission.flight.paradrop.ParaDropPackage;
import pwcg.mission.flight.patrol.LowAltPatrolPackage;
import pwcg.mission.flight.patrol.PatrolPackage;
import pwcg.mission.flight.recon.ReconPackage;
import pwcg.mission.flight.scramble.ScramblePackage;
import pwcg.mission.flight.spy.SpyExtractPackage;
import pwcg.mission.flight.strategicintercept.StrategicInterceptPackage;
import pwcg.mission.flight.transport.TransportPackage;

public class FlightFactory
{
    protected Campaign campaign;
    
    public FlightFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }
    
    public List<IFlight> buildFlight(
    		Mission mission,
    		Squadron squadron,
            FlightTypes flightType,
            NecessaryFlightType necessaryFlightType) throws PWCGException 
    {        
        if (flightType == FlightTypes.ANY)
        {
            throw new PWCGException("No flight type determined at build process");
        }
                
        IFlightPackage flightPackage = null;
        if (flightType == FlightTypes.ARTILLERY_SPOT)
        {
            flightPackage = new ArtillerySpotPackage();
        }
        else if (flightType == FlightTypes.BALLOON_BUST)
        {
            flightPackage = new BalloonBustPackage();
        }
        else if (flightType == FlightTypes.BALLOON_DEFENSE)
        {
            flightPackage = new BalloonDefensePackage();
        }
        else if (flightType == FlightTypes.BOMB || flightType == FlightTypes.LOW_ALT_BOMB)
        {
            flightPackage = new BombingPackage(flightType);
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            flightPackage = new DiveBombingPackage();
        }
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            flightPackage = new StrategicBombingPackage();
        }
        else if (flightType == FlightTypes.ESCORT)
        {
            flightPackage = new PlayerIsEscortPackage();
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            flightPackage = new GroundAttackPackage();
        }
        else if (flightType == FlightTypes.GROUND_HUNT)
        {
            flightPackage = new GroundFreeHuntPackage();
        }
        else if (flightType == FlightTypes.INTERCEPT)
        {
            flightPackage = new InterceptPackage(flightType);
        }
        else if (flightType == FlightTypes.LONE_WOLF)
        {
            flightPackage = new LoneWolfPackage();
        }
        else if (flightType == FlightTypes.OFFENSIVE)
        {
            flightPackage = new OffensivePackage();
        }
        else if (flightType == FlightTypes.PATROL)
        {
            flightPackage = new PatrolPackage();
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            flightPackage = new LowAltPatrolPackage();
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            flightPackage = new CAPPackage(flightType);
        }
        else if (flightType == FlightTypes.RECON)
        {
            flightPackage = new ReconPackage();
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            flightPackage = new TransportPackage();
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            flightPackage = new ParaDropPackage();
        }
        else if (flightType == FlightTypes.CARGO_DROP)
        {
            flightPackage = new CargoDropPackage();
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            flightPackage = new SpyExtractPackage();
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            flightPackage = new ContactPatrolPackage();
        }
        else if (flightType == FlightTypes.SCRAMBLE)
        {
            flightPackage = new ScramblePackage();
        }
        else if (flightType == FlightTypes.STRATEGIC_INTERCEPT)
        {
            flightPackage = new StrategicInterceptPackage();
        }
        else
        {
            throw new PWCGMissionGenerationException("Invalid flight type: " + flightType);
        }

        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, necessaryFlightType);
        List<IFlight> packageFlights = flightPackage.createPackage(flightBuildInformation);
        
        return packageFlights;
    }
}

package pwcg.mission.flight.factory;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.artySpot.ArtillerySpotPackage;
import pwcg.mission.flight.attack.GroundAttackPackage;
import pwcg.mission.flight.balloonBust.BalloonBustPackage;
import pwcg.mission.flight.balloondefense.BalloonDefensePackage;
import pwcg.mission.flight.bomb.BombingPackage;
import pwcg.mission.flight.bomb.LowAltBombingPackage;
import pwcg.mission.flight.bomb.StrategicBombingPackage;
import pwcg.mission.flight.contactpatrol.ContactPatrolPackage;
import pwcg.mission.flight.divebomb.DiveBombingPackage;
import pwcg.mission.flight.escort.EscortPackage;
import pwcg.mission.flight.ferry.FerryPackage;
import pwcg.mission.flight.intercept.HomeDefensePackage;
import pwcg.mission.flight.intercept.InterceptPackage;
import pwcg.mission.flight.intercept.LowAltInterceptPackage;
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

public abstract class FlightFactory
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
        FlightPackage flightPackage = null;

        // Flight generator
        if (flightType == FlightTypes.ARTILLERY_SPOT)
        {
            flightPackage = new ArtillerySpotPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.BALLOON_BUST)
        {
            flightPackage = new BalloonBustPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.BALLOON_DEFENSE)
        {
            flightPackage = new BalloonDefensePackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.BOMB)
        {
            flightPackage = new BombingPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            flightPackage = new LowAltBombingPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            flightPackage = new DiveBombingPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            flightPackage = new StrategicBombingPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.ESCORT)
        {
            if (isPlayerFlight)
            {
                flightPackage = new EscortPackage(mission, campaign, squadron, isPlayerFlight);
            }
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            flightPackage = new GroundAttackPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.INTERCEPT)
        {
            flightPackage = new InterceptPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.LONE_WOLF)
        {
            flightPackage = new LoneWolfPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.OFFENSIVE)
        {
            flightPackage = new OffensivePackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.PATROL)
        {
            flightPackage = new PatrolPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            flightPackage = new LowAltPatrolPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            flightPackage = new LowAltInterceptPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.RECON)
        {
            flightPackage = new ReconPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            flightPackage = new TransportPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.PARATROOP_DROP || flightType == FlightTypes.CARGO_DROP)
        {
            flightPackage = new ParaDropPackage(mission, campaign, squadron, flightType, isPlayerFlight);
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            flightPackage = new SpyExtractPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            flightPackage = new ContactPatrolPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.SCRAMBLE)
        {
            flightPackage = new ScramblePackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.HOME_DEFENSE)
        {
            flightPackage = new HomeDefensePackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING)
        {
            flightPackage = new SeaAntiShippingPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else if (flightType == FlightTypes.SEA_PATROL)
        {
            flightPackage = new SeaPatrolPackage(mission, campaign, squadron, isPlayerFlight);
        }
        else
        {
            throw new PWCGMissionGenerationException("Invalid flight type: " + flightType);
        }

        if (flightPackage != null)
        {
            flight = flightPackage.createPackage();
            flight.setCountry(squadron.determineSquadronCountry(campaign.getDate()));
        }
        
        return flight;
    }

    public FlightTypes buildFlight(Squadron squadron, boolean isMyFlight) 
                    throws PWCGException
    {
        TestFlightFactory testFlightFactory = new TestFlightFactory(campaign);
        FlightTypes testFlightType = testFlightFactory.getActualFlightType(squadron, campaign.getDate(), isMyFlight);
        if (testFlightType != FlightTypes.ANY)
        {
            return testFlightType;
        }
        
        return getActualFlightType(squadron, campaign.getDate(), isMyFlight);
    }
    

    public Flight buildFerryFlight(
                    Mission mission,
                    Squadron squad,
                    boolean isPlayerFlight) throws PWCGException 
    {
        Flight ferryFlight = null;

        FerryPackage ferryFlightPackage = new FerryPackage();
        ferryFlight = ferryFlightPackage.createPackage(mission, campaign, squad);

        return ferryFlight;
    }

    protected abstract FlightTypes getActualFlightType(Squadron squadron,  Date date, boolean isMyFlight) throws PWCGException;
}

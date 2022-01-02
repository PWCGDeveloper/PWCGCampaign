package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.Mission;
import pwcg.mission.flight.bomb.BombingPackage;
import pwcg.mission.flight.cap.CAPPackage;
import pwcg.mission.flight.divebomb.DiveBombingPackage;
import pwcg.mission.flight.groundattack.GroundAttackPackage;
import pwcg.mission.flight.groundhunt.GroundFreeHuntPackage;
import pwcg.mission.flight.intercept.InterceptPackage;
import pwcg.mission.flight.offensive.OffensivePackage;
import pwcg.mission.flight.paradrop.CargoDropPackage;
import pwcg.mission.flight.paradrop.ParaDropPackage;
import pwcg.mission.flight.patrol.LowAltPatrolPackage;
import pwcg.mission.flight.patrol.PatrolPackage;
import pwcg.mission.flight.raider.RaiderAttackPackage;
import pwcg.mission.target.TargetType;

public class FlightFactory
{
    protected Campaign campaign;
    
    public FlightFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }
    
    public List<IFlight> buildFlight(
    		Mission mission,
    		Company squadron,
            FlightTypes flightType,
            NecessaryFlightType necessaryFlightType) throws PWCGException 
    {        
        if (flightType == FlightTypes.ANY)
        {
            throw new PWCGException("No flight type determined at build process");
        }
                
        IFlightPackage flightPackage = null;
        if (flightType == FlightTypes.BOMB || flightType == FlightTypes.LOW_ALT_BOMB)
        {
            flightPackage = new BombingPackage(flightType);
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            flightPackage = new DiveBombingPackage();
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            flightPackage = new GroundAttackPackage(TargetType.TARGET_NONE);
        }
        else if (flightType == FlightTypes.TRAIN_BUST)
        {
            flightPackage = new GroundAttackPackage(TargetType.TARGET_TRAIN);
        }
        else if (flightType == FlightTypes.TANK_BUST)
        {
            flightPackage = new GroundAttackPackage(TargetType.TARGET_ARMOR);
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING)
        {
            flightPackage = new GroundAttackPackage(TargetType.TARGET_SHIPPING);
        }
        else if (flightType == FlightTypes.GROUND_HUNT)
        {
            flightPackage = new GroundFreeHuntPackage();
        }
        else if (flightType == FlightTypes.RAID)
        {
            flightPackage = new RaiderAttackPackage();
        }
        else if (flightType == FlightTypes.INTERCEPT)
        {
            flightPackage = new InterceptPackage(flightType);
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
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            flightPackage = new ParaDropPackage();
        }
        else if (flightType == FlightTypes.CARGO_DROP)
        {
            flightPackage = new CargoDropPackage();
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

package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.flight.bomb.BombingPackage;
import pwcg.mission.flight.cap.CAPPackage;
import pwcg.mission.flight.divebomb.DiveBombingPackage;
import pwcg.mission.flight.groundattack.GroundAttackPackage;
import pwcg.mission.flight.paradrop.CargoDropPackage;
import pwcg.mission.flight.paradrop.ParaDropPackage;
import pwcg.mission.flight.patrol.LowAltPatrolPackage;
import pwcg.mission.target.TargetType;

public class FlightFactory
{
    protected Campaign campaign;
    
    public FlightFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }
    
    public List<IFlight> buildFlight(FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        FlightTypes flightType = flightBuildInformation.getFlightType();
        if (flightType == FlightTypes.ANY)
        {
            throw new PWCGException("No flight type determined at build process");
        }
                
        IFlightPackage flightPackage = null;
        if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            flightPackage = new BombingPackage();
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            flightPackage = new DiveBombingPackage();
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            flightPackage = new GroundAttackPackage(TargetType.TARGET_NONE);
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            flightPackage = new LowAltPatrolPackage();
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            flightPackage = new CAPPackage();
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

        List<IFlight> packageFlights = flightPackage.createFlightPackage(flightBuildInformation);
        
        return packageFlights;
    }
}

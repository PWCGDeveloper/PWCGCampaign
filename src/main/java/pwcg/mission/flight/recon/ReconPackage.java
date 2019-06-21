package pwcg.mission.flight.recon;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.recon.ReconFlight.ReconFlightTypes;

public class ReconPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public ReconPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
    {
        // Initial target coordinates will be something near the target area
        ReconFlightTypes reconFlightType = getReconFlightType();

        // Now the actual mission
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());        
        ReconFlight recon = null;        
        if (flightInformation.isPlayerFlight())
        {
            PlayerReconFlight playerRecon = new PlayerReconFlight(flightInformation, missionBeginUnit);
            recon = playerRecon;
        }
        else
        {
            recon = new ReconFlight(flightInformation, missionBeginUnit);
        }

        recon.setReconFlightType(reconFlightType);
        recon.createUnitMission();
        
        return recon;
    }

    private ReconFlightTypes getReconFlightType()
    {
        ReconFlightTypes reconFlightType = ReconFlightTypes.RECON_FLIGHT_FRONT;

        int roll = RandomNumberGenerator.getRandom(100);

        if (roll < 50)
        {
            reconFlightType = ReconFlightTypes.RECON_FLIGHT_FRONT;
        }
        else if (roll < 85)
        {
            reconFlightType = ReconFlightTypes.RECON_FLIGHT_TRANSPORT;
        }
        else 
        {
            reconFlightType = ReconFlightTypes.RECON_FLIGHT_AIRFIELD;
        }

        return reconFlightType;
    }
}

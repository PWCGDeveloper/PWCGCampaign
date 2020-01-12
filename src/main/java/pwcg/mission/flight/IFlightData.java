package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.waypoint.IWaypointPackage;

public interface IFlightData
{

    void write(BufferedWriter writer) throws PWCGException;

    IFlightInformation getFlightInformation();

    IFlightPlanes getFlightPlanes();

    IWaypointPackage getWaypointPackage();

    ILinkedGroundUnits getLinkedGroundUnits();

    ILinkedFLights getLinkedFlights();

    Campaign getCampaign();

    Coordinate getFlightHomePosition() throws PWCGException;

    IFlightPlayerContact getFlightPlayerContact();
    
    void finalize() throws PWCGException;
    
    void initialize(IFlight flight) throws PWCGException;

}
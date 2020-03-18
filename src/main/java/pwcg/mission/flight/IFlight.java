package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.ground.org.IGroundUnitCollection;

public interface IFlight
{
    Mission getMission();

    Campaign getCampaign();

    void write(BufferedWriter writer) throws PWCGException;

    void finalizeFlight() throws PWCGException;

    void createFlight() throws PWCGException;
    
    IFlightInformation getFlightInformation();

    IFlightPlanes getFlightPlanes();

    IWaypointPackage getWaypointPackage();

    ILinkedGroundUnits getLinkedGroundUnits();

    void addLinkedGroundUnit(IGroundUnitCollection groundUnit);

    ILinkedFlights getLinkedFlights();

    Coordinate getFlightHomePosition() throws PWCGException;

    IFlightPlayerContact getFlightPlayerContact();
        
    void initialize(IFlight flight) throws PWCGException;

    IVirtualWaypointPackage getVirtualWaypointPackage();

    // From FlightInformation
    int getFlightId();
        
    Squadron getSquadron();
    
    FlightTypes getFlightType();

    boolean isPlayerFlight();

    // From FlightPlanes
    boolean isFlightHasFighterPlanes();

    // From FlightClosestContact
    double getClosestContactWithPlayerDistance();
}
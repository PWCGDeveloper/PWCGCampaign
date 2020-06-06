package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.target.TargetDefinition;

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

    ILinkedFlights getLinkedFlights();

    Coordinate getFlightHomePosition() throws PWCGException;

    IFlightPlayerContact getFlightPlayerContact();
        
    void initialize(IFlight flight) throws PWCGException;

    IVirtualWaypointPackage getVirtualWaypointPackage();

    int getFlightId();
        
    Squadron getSquadron();
    
    FlightTypes getFlightType();

    boolean isPlayerFlight();

    boolean isFlightHasFighterPlanes();

    double getClosestContactWithPlayerDistance();
    
    int getFlightCruisingSpeed();

    void overrideFlightCruisingSpeedForEscort(int cruisingSpeed);
    
    TargetDefinition getTargetDefinition();

}
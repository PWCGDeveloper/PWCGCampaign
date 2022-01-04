package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.virtual.IVirtualWaypointPackage;
import pwcg.mission.target.TargetDefinition;

public interface IFlight
{
    Mission getMission();

    Campaign getCampaign();

    void write(BufferedWriter writer) throws PWCGException;

    void createFlight() throws PWCGException;

    void finalizeFlight() throws PWCGException;
    
    FlightInformation getFlightInformation();

    IFlightPlanes getFlightPlanes();

    IWaypointPackage getWaypointPackage();

    Coordinate getFlightHomePosition() throws PWCGException;

    IFlightPlayerContact getFlightPlayerContact();
        
    void initialize(IFlight flight) throws PWCGException;

    IVirtualWaypointPackage getVirtualWaypointPackage();

    int getFlightId();
        
    Company getSquadron();
    
    FlightTypes getFlightType();

    boolean isPlayerFlight();

    double getClosestContactWithPlayerDistance();
    
    int getFlightCruisingSpeed();

    void overrideFlightCruisingSpeedForEscort(int cruisingSpeed);
    
    TargetDefinition getTargetDefinition();

    void setAssociatedFlight(IFlight associatedFlight);

    IFlight getAssociatedFlight();
}
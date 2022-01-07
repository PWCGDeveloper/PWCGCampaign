package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
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
        
    void initialize(IFlight flight) throws PWCGException;

    IVirtualWaypointPackage getVirtualWaypointPackage();

    int getFlightId();
            
    FlightTypes getFlightType();
    
    int getFlightCruisingSpeed();
    
    TargetDefinition getTargetDefinition();

    ICountry getCountry();

}
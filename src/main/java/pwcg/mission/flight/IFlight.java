package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.squadron.Squadron;
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
        
    public FrontMapIdentifier getCampaignMap() throws PWCGException;

    void write(BufferedWriter writer) throws PWCGException;

    void createFlight() throws PWCGException;

    void finalizeFlight() throws PWCGException;
    
    FlightInformation getFlightInformation();

    FlightPlanes getFlightPlanes();

    IWaypointPackage getWaypointPackage();

    Coordinate getFlightHomePosition() throws PWCGException;

    IFlightPlayerContact getFlightPlayerContact();
        
    void initialize(IFlight flight) throws PWCGException;

    IVirtualWaypointPackage getVirtualWaypointPackage();

    int getFlightId();
        
    Squadron getSquadron();
    
    FlightTypes getFlightType();

    boolean isPlayerFlight();

    double getClosestContactWithPlayerDistance();
    
    int getFlightCruisingSpeed();

    void overrideFlightCruisingSpeedForEscort(int cruisingSpeed);
    
    TargetDefinition getTargetDefinition();

    void addVirtualEscort() throws PWCGException;

    void setAssociatedFlight(IFlight associatedFlight);

    IFlight getAssociatedFlight();

    List<Integer> getAllPlanesIdsInFlight();

    void setEnemiesForFlight(List<Integer> enemyPlaneIds);
}
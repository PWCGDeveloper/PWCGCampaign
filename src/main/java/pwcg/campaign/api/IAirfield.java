package pwcg.campaign.api;

import java.io.BufferedWriter;
import java.util.Date;
import java.util.List;

import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.mission.Mission;

public interface IAirfield extends IFixedPosition
{

    IAirfield copy();

    void write(BufferedWriter writer) throws PWCGException;

    String toString();

    void initializeAirfieldFromLocation(PWCGLocation airfieldLocation);

    void addAirfieldObjects(Mission mission) throws PWCGException;

    public String getName();
    
    public Orientation getOrientation();

    public boolean isGroup();
    
    public String getModel();

    public String getScript();

    public Coordinate getPosition();

    public PWCGLocation getTakeoffLocation(Mission mission) throws PWCGException;

    public PWCGLocation getLandingLocation(Mission mission) throws PWCGException;

    public boolean isNearRunwayOrTaxiway(Mission mission, Coordinate pos) throws PWCGException;

    public Date getStartDate();

    public List<HotSpot> getNearbyHotSpots() throws PWCGException;

    public List<Coordinate> getBoundary() throws PWCGException;

    int getUnitCount();

    PWCGLocation getFakeAirfieldLocation(Mission mission) throws PWCGException;

    PWCGLocation getParkingLocation(Mission mission) throws PWCGException;
}

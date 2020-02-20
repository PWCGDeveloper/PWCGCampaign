package pwcg.campaign.api;

import java.io.BufferedWriter;
import java.util.Date;
import java.util.List;

import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.campaign.group.airfield.staticobject.AirfieldObjects;
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
    
    /**
     * Gets the location of the airfield as a whole, used for map coordinates,
     * distance checking etc. Does not relate to takeoff/landing locations.
     */
    public Coordinate getPosition();

    /**
     * Gets the position and direction for the start of the takeoff run from
     * this airfield.
     * @return Takeoff position and orientation
     * @throws PWCGException
     */
    public PWCGLocation getTakeoffLocation() throws PWCGException;

    /**
     * Gets the position and direction for the touchdown point when landing at
     * this airfield.
     * @return Landing position and orientation
     * @throws PWCGException
     */
    public PWCGLocation getLandingLocation() throws PWCGException;

    /**
     * Gets the position and direction of the lead plane when starting parked.
     * @return Parking position and orientation
     * @throws PWCGException
     */
    public PWCGLocation getParkingLocation() throws PWCGException;
    
    /**
     * Gets the position and direction that the fake airfield object should
     * be placed at.
     * @return Airfield position and orientation
     * @throws PWCGException
     */
    public PWCGLocation getFakeAirfieldLocation() throws PWCGException;

    public boolean isNearRunwayOrTaxiway(Coordinate pos) throws PWCGException;

    public Date getStartDate();

    public AirfieldObjects getAirfieldObjects();

    public List<HotSpot> getNearbyHotSpots() throws PWCGException;

    public List<Coordinate> getBoundary() throws PWCGException;
}

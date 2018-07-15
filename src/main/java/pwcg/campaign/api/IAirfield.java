package pwcg.campaign.api;

import java.io.BufferedWriter;
import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;

public interface IAirfield extends IFixedPosition
{

    IAirfield copy();

    void write(BufferedWriter writer) throws PWCGException;

    String toString();

    void initializeAirfieldFromLocation(PWCGLocation airfieldLocation);

    void addAirfieldObjects(Campaign campaign) throws PWCGException;

    public String getName();
    
    public Orientation getOrientation();

    public boolean isGroup();

    void setAAA(GroundUnitSpawning aaa);
    
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
    
    public Date getStartDate();
}
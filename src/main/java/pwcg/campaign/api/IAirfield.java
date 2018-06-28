package pwcg.campaign.api;

import java.io.BufferedWriter;
import java.util.Date;

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

    void addAirfieldObjects(Date date) throws PWCGException, PWCGException;

    public String getName();
    
    public Orientation getOrientation();

    public boolean isGroup();

    void setAAA(GroundUnitSpawning aaa);
    
    public String getModel();

    public String getScript();
    
    public double getPlaneOrientation();
    
    public Coordinate getPosition();

    public PWCGLocation getPlanePosition();
    
    public Date getStartDate();
}
package pwcg.campaign.ww1.airfield;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.airfield.AirfieldObjectPlacer;
import pwcg.campaign.group.airfield.AirfieldObjects;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuTREntity;

public class RoFAirfield extends FixedPosition implements IAirfield, Cloneable
{
	private boolean isGroup = false;
	private Date startDate = null;
	private GroundUnitSpawning aaa = null;
	private AirfieldObjects airfieldObjects = new AirfieldObjects();
	private FixedPosition airfieldFixedPosition = new FixedPosition();
	private McuTREntity entity = new McuTREntity();

	
	private RoFAirfield()
	{
		
	}
	
	public RoFAirfield (FixedPosition airfieldFixedPosition)
	{
		this.airfieldFixedPosition = airfieldFixedPosition;
	}

	public RoFAirfield copy()
	{
		RoFAirfield clone = new RoFAirfield();
		
		clone.airfieldFixedPosition = airfieldFixedPosition.clone(this.airfieldFixedPosition);
		clone.isGroup = this.isGroup;
		clone.name = this.name;
		clone.position = this.position;
		clone.orientation = this.orientation;

	    clone.airfieldObjects = new AirfieldObjects();
		
		if (startDate == null)
		{
			clone.startDate = null;
		}
		else
		{
			clone.startDate = (Date)this.startDate.clone();
		}
		
		return clone;
	}

	public void write(BufferedWriter writer) throws PWCGException
	{
        try
        {
    		writer.write("Airfield");
    		writer.newLine();
    		writer.write("{");
    		writer.newLine();
    		
    		airfieldFixedPosition.write(writer);
    
    		writer.write("}");
    		writer.newLine();
    		writer.newLine();
    		
            entity.write(writer);

    		if (aaa != null)
    		{
    			aaa.write(writer);
    		}
            
            // Write any vehicles on the airfield
            for (IVehicle airfieldObject : airfieldObjects.getAirfieldObjects())
            {
                airfieldObject.write(writer);
            }
            
            // Write airfield AAA
            for (GroundUnitSpawning airfieldAAA : airfieldObjects.getAaaForAirfield())
            {
                airfieldAAA.write(writer);
            }
            
            // Write any static planes
            for (IStaticPlane staticPlane : airfieldObjects.getStaticPlanes())
            {
                staticPlane.write(writer);
            }
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

	@Override
	public void addAirfieldObjects(Campaign campaign) throws PWCGException 
	{
	    if (!(createCountry(campaign.getDate()).isNeutral()))
	    {
	    	AirfieldObjectPlacer airfieldObjectPlacer = new AirfieldObjectPlacer(campaign, this);
	    	airfieldObjects = airfieldObjectPlacer.createAirfieldObjectsDefinedHotSpotsOnly();
	    }
	}

    private double getPlaneOrientation()
    {
        if (orientation != null)
        {
            return orientation.getyOri();
        }
        
        return airfieldFixedPosition.getOrientation().getyOri();
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public void setAAA(GroundUnitSpawning aaa) 
    {
        this.aaa = aaa;
    }

    @Override
    public void initializeAirfieldFromLocation(PWCGLocation airfieldLocation)
    {
        super.setFromLocation(airfieldLocation);
    }

	@Override
	public void initialize(Coordinate unitPosition, String name)
	{
		airfieldFixedPosition.initialize(unitPosition, name);
	}

	@Override
	public String getName()
	{
		return airfieldFixedPosition.getName();
	}

	@Override
	public void setName(String name)
	{
		airfieldFixedPosition.setName(name);
	}

	@Override
	public void setPosition(Coordinate unitPosition)
	{
		airfieldFixedPosition.setPosition(unitPosition);
	}

	@Override
	public Orientation getOrientation()
	{
		return airfieldFixedPosition.getOrientation();
	}

	@Override
	public String getModel()
	{
		return airfieldFixedPosition.getModel();
	}

	@Override
	public String getScript()
	{
		return airfieldFixedPosition.getScript();
	}

	@Override
	public Coordinate getPosition()
	{
		return airfieldFixedPosition.getPosition();
	}

	public ICountry createCountry(Date date) throws PWCGException
	{
		return airfieldFixedPosition.createCountry(date);
	}

	public ICountry getCountry(Date date) throws PWCGException
	{
		return airfieldFixedPosition.getCountry(date);
	}

	@Override
	public PWCGLocation getLandingLocation() throws PWCGException
	{
		PWCGLocation location = new PWCGLocation();
		location.getOrientation().setyOri(getPlaneOrientation() - 180);

		Campaign campaign = PWCGContextManager.getInstance().getCampaign();
		int landingDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.LandingDistanceKey);
		Coordinate landCoords = MathUtils.calcNextCoord(
				getPosition(),
				getPlaneOrientation(),
				landingDistance);
		landCoords.setYPos(0.0);
		location.setPosition(landCoords);

		return location;
	}

	@Override
	public PWCGLocation getTakeoffLocation() throws PWCGException {
		return this;
	}

	@Override
	public PWCGLocation getParkingLocation() throws PWCGException {
		throw new PWCGException("Parked starts not implemented for RoF");
	}

	@Override
	public PWCGLocation getFakeAirfieldLocation() throws PWCGException {
		throw new PWCGException("Fake airfields not used in RoF");
	}

	@Override
	public boolean isNearRunwayOrTaxiway(Coordinate pos) throws PWCGException {
		double runwayOrientation = getTakeoffLocation().getOrientation().getyOri();
		Coordinate startOfRunway = getTakeoffLocation().getPosition();
		Coordinate endOfRunway = MathUtils.calcNextCoord(getTakeoffLocation().getPosition(), runwayOrientation, 2000.0);

		CoordinateBox runwayCoordinateBox = CoordinateBox.coordinateBoxFromTwoCoordinates(startOfRunway, endOfRunway);
		runwayCoordinateBox.expandBox(200);

		return runwayCoordinateBox.isInBox(pos);
	}
}

package pwcg.campaign.ww1.airfield;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.airfield.AirfieldObjectPlacer;
import pwcg.campaign.group.airfield.AirfieldObjects;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.Logger;
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

    public double getPlaneOrientation() 
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
    public PWCGLocation getPlanePosition()
    {
        return this;
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
}

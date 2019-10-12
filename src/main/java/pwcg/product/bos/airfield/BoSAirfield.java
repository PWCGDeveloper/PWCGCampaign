package pwcg.product.bos.airfield;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.airfield.AirfieldObjectPlacer;
import pwcg.campaign.group.airfield.AirfieldObjects;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.vehicle.IVehicle;

public class BoSAirfield extends FixedPosition implements IAirfield, Cloneable
{
    private List<Runway> runways = new ArrayList<>();
	private AirfieldObjects airfieldObjects = new AirfieldObjects();

	public BoSAirfield ()
	{
		deleteAfterDeath = 0;
	}

	@Override
    public BoSAirfield copy()
	{
		BoSAirfield clone = new BoSAirfield();
		
	    clone.airfieldObjects = new AirfieldObjects();

        for (Runway r : runways)
            clone.runways.add(r.copy());

		super.clone(clone);
        
		return clone;
	}

	@Override
    public void write(BufferedWriter writer) throws PWCGException
	{
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

	@Override
    public String toString()
	{
		StringBuffer output = new StringBuffer("");
		output.append("Airfield\n");
		output.append("{\n");
		
		output.append("  Name = \"" + name + "\";");
		output.append("  Index = " + index + ";\n");
		output.append("  XPos = " + position.getXPos() + ";\n");
		output.append("  YPos = " + position.getYPos() + ";\n");
		output.append("  ZPos = " + position.getZPos() + ";\n");
		output.append("  YOri = " + orientation.getyOri() + ";\n");
		
		output.append("  Country = " + determineCountry().getCountryName() + ";\n");

		output.append("}\n");
		output.append("\n");
		output.append("\n");
		
		return output.toString();
	}

    @Override
    public void initializeAirfieldFromLocation(PWCGLocation planePosition)
    {
        this.position = planePosition.getPosition().copy();
        this.orientation = planePosition.getOrientation().copy();
        this.name = planePosition.getName();
    }

    public void initializeAirfieldFromDescriptor(AirfieldDescriptor desc)
    {
        this.position = desc.getPosition().copy();
        this.orientation = desc.getOrientation().copy();
        this.name = desc.getName();
        for (Runway r : desc.getRunways())
            this.runways.add(r.copy());
    }

    public double getPlaneOrientation() 
    {
        // BoS does not use airfield orientation
        return orientation.getyOri();
    }

    @Override
    public void addAirfieldObjects(Campaign campaign) throws PWCGException 
    {
        if (!(createCountry(campaign.getDate()).isNeutral()))
        {
	    	AirfieldObjectPlacer airfieldObjectPlacer = new AirfieldObjectPlacer(campaign, this);
	    	airfieldObjects = airfieldObjectPlacer.createAirfieldObjectsWithEmptySpace();
        }
    }

    @Override
    public boolean isGroup()
    {
        // BoS airfields are not real fields
        return false;
    }

    @Override
    public void setAAA(GroundUnitSpawning aaa)
    {
        // BoS airfields do not have AAA
    }

    @Override
    public Date getStartDate()
    {
        try
        {
            return DateUtils.getBeginningOfGame();
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Runway> getAllRunways()
    {
        return runways;
    }

    private PWCGLocation getRunwayStart() throws PWCGException
    {
		Runway runway = selectRunway();

		if (runway != null) {
			double runwayOrientation = MathUtils.calcAngle(runway.getStartPos(), runway.getEndPos());

			PWCGLocation loc = new PWCGLocation();
			loc.setPosition(runway.getStartPos());
			loc.setOrientation(new Orientation(runwayOrientation));
			return loc;
		} else {
			return this;
		}
    }
        
	@Override
	public PWCGLocation getTakeoffLocation() throws PWCGException {
		return getRunwayStart();
	}

	@Override
	public PWCGLocation getLandingLocation() throws PWCGException {
		return getRunwayStart();
	}

	@Override
	public PWCGLocation getParkingLocation() throws PWCGException {
		Runway runway = selectRunway();

		if (runway == null)
			return this;

		return runway.getParkingLocation();
	}

	@Override
	public PWCGLocation getFakeAirfieldLocation() throws PWCGException {
		Runway runway = selectRunway();

		PWCGLocation loc = new PWCGLocation();
		Coordinate pos = new Coordinate();
		pos.setXPos((runway.getStartPos().getXPos() + runway.getEndPos().getXPos()) / 2.0);
		pos.setYPos((runway.getStartPos().getYPos() + runway.getEndPos().getYPos()) / 2.0);
		pos.setZPos((runway.getStartPos().getZPos() + runway.getEndPos().getZPos()) / 2.0);
		loc.setPosition(pos);
        double runwayOrientation = MathUtils.calcAngle(runway.getStartPos(), runway.getEndPos());
        // BoX seems to like the runway orientation to be an odd integer
        runwayOrientation = Math.rint(runwayOrientation);
        if ((runwayOrientation % 2) == 0)
            runwayOrientation = MathUtils.adjustAngle(runwayOrientation, 1.0);
        loc.setOrientation(new Orientation(runwayOrientation));
		return loc;
	}

	private Runway selectRunway() throws PWCGException
	{
		// Note: wind direction is as specified in mission file, i.e. heading wind blows to
		// Adjust by 180 degrees to give standard direction
		double windDirection = MathUtils.adjustAngle(PWCGContext.getInstance().getCurrentMap().getMapWeather().getWindDirection(), 180);

		double bestOffset = 1000.0;
		Runway bestRunway = null;
		boolean invertRunway = false;

		for (Runway r : runways) {
			double offset = MathUtils.calcNumberOfDegrees(windDirection, r.getHeading());
			boolean invert = false;

			if (offset > 90.0) {
				offset = 180 - offset;
				invert = true;
			}

			if (offset < bestOffset) {
				bestOffset = offset;
				bestRunway = r;
				invertRunway = invert;
			}
		}

		if (invertRunway)
			return bestRunway.invert();
		else
			return bestRunway.copy();
	}

	private String getChartPoint(int ptype, Coordinate point) throws PWCGException
	{
		double xpos = point.getXPos() - getFakeAirfieldLocation().getPosition().getXPos();
		double ypos = point.getZPos() - getFakeAirfieldLocation().getPosition().getZPos();

		double angle = Math.toRadians(-getFakeAirfieldLocation().getOrientation().getyOri());

		double rxpos = Math.cos(angle) * xpos - Math.sin(angle) * ypos;
		double rypos = Math.cos(angle) * ypos + Math.sin(angle) * xpos;

		String pos;
		pos  = "      Point\n";
		pos += "      {\n";
		pos += "        Type = " + ptype + ";\n";
		pos += "        X = " + Coordinate.format(rxpos) + ";\n";
		pos += "        Y = " + Coordinate.format(rypos) + ";\n";
		pos += "      }\n";
		return pos;
	}

	public String getChart() throws PWCGException
	{
		Runway runway = selectRunway();

		if (runway == null)
			return "";
		
        if (runway.getParkingLocation() == null)
            return "";

		String chart;

		chart  = "    Chart\n";
		chart += "    {\n";
		chart += getChartPoint(0, runway.getParkingLocation().getPosition());
		for (Coordinate pos : runway.getTaxiToStart())
			chart += getChartPoint(1, pos);
		chart += getChartPoint(2, runway.getStartPos());
		chart += getChartPoint(2, runway.getEndPos());
		for (Coordinate pos : runway.getTaxiFromEnd())
			chart += getChartPoint(1, pos);
		chart += getChartPoint(0, runway.getParkingLocation().getPosition());
		chart += "    }\n";

		return chart;
	}

	@Override
	public boolean isNearRunwayOrTaxiway(Coordinate pos) throws PWCGException {
		if (runways.size() == 0)
		{
			double runwayOrientation = getTakeoffLocation().getOrientation().getyOri();
			Coordinate startOfRunway = getTakeoffLocation().getPosition();
			Coordinate endOfRunway = MathUtils.calcNextCoord(getTakeoffLocation().getPosition(), runwayOrientation, 2000.0);

			return MathUtils.distFromLine(startOfRunway, endOfRunway, pos) < 120;
		} else {
			for (Runway r : runways)
			{
				Coordinate extendedRunwayStart = MathUtils.calcNextCoord(r.getStartPos(), MathUtils.adjustAngle(r.getHeading(), 180), 300);
				Coordinate extendedRunwayEnd = MathUtils.calcNextCoord(r.getEndPos(), r.getHeading(), 300);
				if (MathUtils.distFromLine(extendedRunwayStart, extendedRunwayEnd, pos) < 120)
					return true;
				
				if (r.getParkingLocation() == null)
				{
				    return false;
				}

				Coordinate prevPoint = r.getParkingLocation().getPosition();
				for (Coordinate p : r.getTaxiToStart()) {
					if (MathUtils.distFromLine(prevPoint, p, pos) < 50)
						return true;
					prevPoint = p;
				}
				if (MathUtils.distFromLine(prevPoint, r.getStartPos(), pos) < 50)
					return true;

				prevPoint = r.getEndPos();
				for (Coordinate p : r.getTaxiFromEnd()) {
					if (MathUtils.distFromLine(prevPoint, p, pos) < 50)
						return true;
					prevPoint = p;
				}
				if (MathUtils.distFromLine(prevPoint, r.getParkingLocation().getPosition(), pos) < 50)
					return true;

				double parkingOrientation = MathUtils.adjustAngle(r.getParkingLocation().getOrientation().getyOri(), 90);
				Coordinate parkingEnd = MathUtils.calcNextCoord(r.getParkingLocation().getPosition(), parkingOrientation, 300);
				if (MathUtils.distFromLine(r.getParkingLocation().getPosition(), parkingEnd, pos) < 80)
					return true;
			}
		}

		return false;
	}
}

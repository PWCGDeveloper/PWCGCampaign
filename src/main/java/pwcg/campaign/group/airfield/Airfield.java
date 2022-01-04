package pwcg.campaign.group.airfield;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.group.airfield.staticobject.AirfieldObjectPlacer;
import pwcg.campaign.group.airfield.staticobject.AirfieldObjects;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.vehicle.IVehicle;

public class Airfield extends FixedPosition implements Cloneable
{
    private List<Runway> runways = new ArrayList<>();
    private AirfieldObjects airfieldObjects;

    public Airfield()
    {
        deleteAfterDeath = 0;
    }

    public Airfield copy()
    {
        Airfield clone = new Airfield();

        for (Runway r : runways)
            clone.runways.add(r.copy());

        super.clone(clone);

        return clone;
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        if (airfieldObjects != null)
        {
            for (IStaticPlane staticPlane : airfieldObjects.getStaticPlanes())
            {
                staticPlane.write(writer);
            }

            for (IVehicle airfieldObject : airfieldObjects.getStaticTrucks())
            {
                airfieldObject.write(writer);
            }
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

        try
        {
            output.append("  Country = " + determineCountry().getCountryName() + ";\n");
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }

        output.append("}\n");
        output.append("\n");
        output.append("\n");

        return output.toString();
    }

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
        {
            this.runways.add(r.copy());
        }
    }

    public void addAirfieldObjects(Mission mission, ICountry airfieldCountry) throws PWCGException
    {
        if (!(determineCountryOnDate(mission.getCampaign().getDate()).isNeutral()))
        {
            AirfieldObjectPlacer airfieldObjectPlacer = new AirfieldObjectPlacer(mission, this, airfieldCountry);
            airfieldObjects = airfieldObjectPlacer.createAirfieldObjects();
        }
    }

    public boolean isGroup()
    {
        return false;
    }

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

    private PWCGLocation getRunwayStart(Mission mission) throws PWCGException
    {
        Runway runway = selectRunway(mission);

        if (runway != null)
        {
            double runwayOrientation = MathUtils.calcAngle(runway.getStartPos(), runway.getEndPos());

            PWCGLocation runwayTakeoffLocation = new PWCGLocation();
            runwayTakeoffLocation.setPosition(runway.getStartPos());
            runwayTakeoffLocation.setOrientation(new Orientation(runwayOrientation));
            return runwayTakeoffLocation;
        }
        else
        {
            return this;
        }
    }

    private PWCGLocation getLandingStart(Mission mission) throws PWCGException
    {
        Runway runway = selectRunway(mission);

        if (runway != null)
        {
            PWCGLocation runwayLandingLocation = new PWCGLocation();
            runwayLandingLocation.setPosition(runway.getEndPos());
            
            double angleForLanding = MathUtils.calcAngle(runway.getEndPos(), runway.getStartPos());
            runwayLandingLocation.setOrientation(new Orientation(angleForLanding));

            return runwayLandingLocation;
        }
        else
        {
            return this;
        }
    }

    public PWCGLocation getTakeoffLocation(Mission mission) throws PWCGException
    {
        return getRunwayStart(mission);
    }

    public PWCGLocation getLandingLocation(Mission mission) throws PWCGException
    {
        return getLandingStart(mission);
    }

    public PWCGLocation getParkingLocation(Mission mission) throws PWCGException
    {
        Runway runway = selectRunway(mission);

        if (runway == null)
            return this;

        return runway.getParkingLocation();
    }

    public PWCGLocation getFakeAirfieldLocation(Mission mission) throws PWCGException
    {
        ConfigManagerCampaign configManager = PWCGContext.getInstance().getCampaign().getCampaignConfigManager();
        if (configManager.getIntConfigParam(ConfigItemKeys.AllowAirStartsKey) == 2)
        {
            // The game sometimes seems to go funny if the airfield location is on the runway when
            // doing cold starts, so move to the parking area instead
            return getParkingLocation(mission);
        }
        else
        {
            Runway runway = selectRunway(mission);

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
    }

    public String getChart(Mission mission) throws PWCGException
    {
        Runway runway = selectRunway(mission);

        if (runway == null)
            return "";

        if (runway.getParkingLocation() == null)
            return "";

        String chart;

        chart = "    Chart\n";
        chart += "    {\n";
        chart += getChartPoint(mission, 0, runway.getParkingLocation().getPosition());
        for (Coordinate pos : runway.getTaxiToStart())
            chart += getChartPoint(mission, 1, pos);
        chart += getChartPoint(mission, 2, runway.getStartPos());
        chart += getChartPoint(mission, 2, runway.getEndPos());
        for (Coordinate pos : runway.getTaxiFromEnd())
            chart += getChartPoint(mission, 1, pos);
        chart += getChartPoint(mission, 0, runway.getParkingLocation().getPosition());
        chart += "    }\n";

        return chart;
    }

    private String getChartPoint(Mission mission, int ptype, Coordinate point) throws PWCGException
    {
        double xpos = point.getXPos() - getFakeAirfieldLocation(mission).getPosition().getXPos();
        double ypos = point.getZPos() - getFakeAirfieldLocation(mission).getPosition().getZPos();

        double angle = Math.toRadians(-getFakeAirfieldLocation(mission).getOrientation().getyOri());

        double rxpos = Math.cos(angle) * xpos - Math.sin(angle) * ypos;
        double rypos = Math.cos(angle) * ypos + Math.sin(angle) * xpos;

        String pos;
        pos = "      Point\n";
        pos += "      {\n";
        pos += "        Type = " + ptype + ";\n";
        pos += "        X = " + Coordinate.format(rxpos) + ";\n";
        pos += "        Y = " + Coordinate.format(rypos) + ";\n";
        pos += "      }\n";
        return pos;
    }

    private Runway selectRunway(Mission mission) throws PWCGException
    {
        RunwayChooser runwayChooser = new RunwayChooser(mission);
        runwayChooser.selectRunway(runways);

        Runway bestRunway = runwayChooser.getBestRunway();
        return bestRunway.copy();
    }

    public boolean isNearRunwayOrTaxiway(Mission mission, Coordinate pos) throws PWCGException
    {
        if (runways.size() == 0)
        {
            double runwayOrientation = getTakeoffLocation(mission).getOrientation().getyOri();
            Coordinate startOfRunway = getTakeoffLocation(mission).getPosition();
            Coordinate endOfRunway = MathUtils.calcNextCoord(getTakeoffLocation(mission).getPosition(), runwayOrientation, 2000.0);

            return MathUtils.distFromLine(startOfRunway, endOfRunway, pos) < 120;
        }
        else
        {
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
                for (Coordinate p : r.getTaxiToStart())
                {
                    if (MathUtils.distFromLine(prevPoint, p, pos) < 50)
                        return true;
                    prevPoint = p;
                }
                if (MathUtils.distFromLine(prevPoint, r.getStartPos(), pos) < 50)
                    return true;

                prevPoint = r.getEndPos();
                for (Coordinate p : r.getTaxiFromEnd())
                {
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

    @Override
    public ICountry getCountry(Date date) throws PWCGException {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        Company squadronForField = squadronManager.getAnyActiveCompanyForAirfield(this, date);

        if (squadronForField != null)
        {
            return squadronForField.getCountry();
        }

        return super.getCountry(date);
    }

    public List<Coordinate> getBoundary() throws PWCGException
    {
        List<Coordinate> points = new ArrayList<>();

        CoordinateBox box = CoordinateBox.coordinateBoxFromCenter(getPosition(), 500);
        points.add(box.getNE());
        points.add(box.getNW());
        points.add(box.getSE());
        points.add(box.getSW());

        for (Runway runway : runways)
        {
            if (runway.getParkingLocation() != null)
            {
                points.add(runway.getParkingLocation().getPosition());
            }
            
            if (runway.getTaxiToStart() != null)
            {
                points.addAll(runway.getTaxiToStart());
            }
            
            if (runway.getTaxiFromEnd() != null)
            {
                points.addAll(runway.getTaxiFromEnd());
            }
            
            points.add(runway.getStartPos());
            points.add(runway.getEndPos());
        }

        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        List<Block>nearbyBlocks = groupManager.getBlockFinder().getBlocksWithinRadius(getPosition().copy(), 3000.0);
        for (Block block : nearbyBlocks)
        {
            if (block.getModel().contains("arf_") || block.getModel().contains("af_"))
            {
                points.add(block.getPosition());
            }
        }

        return MathUtils.convexHull(points);
    }
}

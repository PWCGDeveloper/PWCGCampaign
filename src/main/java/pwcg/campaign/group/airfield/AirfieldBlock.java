package pwcg.campaign.group.airfield;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuTREntity;

public class AirfieldBlock extends FixedPosition implements Cloneable
{
	private int id = 0;
	private boolean isGroup = false;
	private Date startDate = null;
	private GroundUnit aaa = null;
	private McuIcon airfieldOrientationIcon = null;
    private McuTREntity entity = new McuTREntity();

	public AirfieldBlock()
	{
		deleteAfterDeath = 0;
	}

	public AirfieldBlock copy()
	{
		AirfieldBlock clone = new AirfieldBlock();

		super.clone(clone);

		clone.id = this.id;
		clone.isGroup = this.isGroup;
		clone.airfieldOrientationIcon = airfieldOrientationIcon;

		if (startDate == null)
		{
			clone.startDate = null;
		}
		else
		{
			clone.startDate = (Date) this.startDate.clone();
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

			super.write(writer);

			writer.write("}");
			writer.newLine();
			writer.newLine();
            
            entity.write(writer);

			if (aaa != null)
			{
				aaa.write(writer);
			}
		}
		catch (IOException e)
		{
			PWCGLogger.logException(e);
			throw new PWCGIOException(e.getMessage());
		}
	}

	public void writePWCGFile(BufferedWriter writer) throws PWCGException
	{
		try
		{
			writer.write("Airfield");
			writer.newLine();
			writer.write("{");
			writer.newLine();

			super.write(writer);

			writer.write("}");
			writer.newLine();
			writer.newLine();

			// After we write the field, do the prientation
			if (airfieldOrientationIcon != null)
			{
				airfieldOrientationIcon.write(writer);
			}
		}
		catch (IOException e)
		{
			PWCGLogger.logException(e);
			throw new PWCGIOException(e.getMessage());
		}
	}

	public String toString()
	{
		StringBuffer output = new StringBuffer("");
		output.append("Airfield\n");
		output.append("{\n");

		output.append("  Name = \"" + name + "\";");
		output.append("  Index = " + index + ";\n");
		output.append("  LinkTrId = " + linkTrId + ";\n");
		output.append("  XPos = " + position.getXPos() + ";\n");
		output.append("  YPos = " + position.getYPos() + ";\n");
		output.append("  ZPos = " + position.getZPos() + ";\n");
		output.append("  XOri = " + orientation.getxOri() + ";\n");
		output.append("  YOri = " + orientation.getyOri() + ";\n");
		output.append("  ZOri = " + orientation.getzOri() + ";\n");
		output.append("  Script = \"" + script + "\";\n");
		output.append("  Model = \"" + model + "\";\n");
		output.append("  Desc = \"" + desc + "\";\n");
		output.append("  Durability = " + durability + ";\n");
		output.append("  DamageReport = " + damageReport + ";\n");
		output.append("  DamageThreshold = " + damageThreshold + ";\n");

		output.append("}\n");
		output.append("\n");
		output.append("\n");

		return output.toString();
	}

	public double getPlaneOrientation()
	{
		// Do we have specific instructions?
		if (airfieldOrientationIcon != null)
		{
			return airfieldOrientationIcon.getOrientation().getyOri();
		}

		// If not use the airfield orientation
		return orientation.getyOri();
	}

	public Coordinate getPlanePosition() throws PWCGException
	{
		Coordinate planeCoords = null;

		// Do we have specific instructions?
		if (airfieldOrientationIcon != null)
		{
			planeCoords = airfieldOrientationIcon.getPosition().copy();
		}
		else
		{
			planeCoords = getPlanePositionForGenericField();
		}

		return planeCoords;
	}

	private Coordinate getPlanePositionForGenericField() throws PWCGException
	{

		Double angleToHangars = getAngleToHangars();

		// Establish the initial coordinates
		double airfieldOffset = getOffsetForGenericField();
		Coordinate planeCoords = MathUtils.calcNextCoord(getPosition().copy(), angleToHangars, airfieldOffset);
		return planeCoords;
	}

	private double getOffsetForGenericField()
	{
		double offsetForGenericField = 0.0;

		if (this.getModel().contains("ger_med"))
		{
			offsetForGenericField = 140.0;
		}
		else if (this.getModel().contains("fr_med"))
		{
			offsetForGenericField = 0.0;
		}
		else if (this.getModel().contains("ger_lrg"))
		{
			offsetForGenericField = -100.0;
		}

		return offsetForGenericField;
	}

	private double getAngleToHangars()
	{
		// YOri is actually heading (0 = north, move clockwise)
		// and not orientation on a circle (0 = east, rotate counter-clockwise)
		// Convert from heading to orientation on a circle.
		double airfieldOrientation = getOrientation().getyOri();

		Double angleToHangars = MathUtils.adjustAngle(airfieldOrientation, 180);

		return angleToHangars;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public boolean isGroup()
	{
		return isGroup;
	}

	public void setGroup(boolean isGroup)
	{
		this.isGroup = isGroup;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public void setAAA(GroundUnit aaa)
	{
		this.aaa = aaa;
	}

	public void setAirfieldOrientation(McuIcon airfieldOrientation)
	{
		this.airfieldOrientationIcon = airfieldOrientation;
	}
}

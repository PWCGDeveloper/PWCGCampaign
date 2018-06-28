
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.CountryDesignator;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.plane.Balloon;
import pwcg.campaign.utils.LCIndexGenerator;
import pwcg.campaign.ww1.plane.RoFPlaneAttributeMapping;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.mission.MissionStringHandler;

public class McuIcon extends BaseFlightMcu
{

	private int enabled = 1;
	private int lCName = 0;
	private int lCDesc = 0;
	private int rColor = 255;
	private int gColor = 255;
	private int bColor = 255;
	private McuIconIdType iconId = McuIconIdType.ICON_ID_NORMAL;
    private McuIconLineType lineType = McuIconLineType.ICON_LINE_TYPE_NONE;
	private List<Coalition> coalitions = new ArrayList<Coalition>();

	public McuIcon (String iconName, String iconText)
	{
		super();

        name = iconName;
        desc = iconName;
        
        MissionStringHandler.getInstance().registerMissionText(lCName, iconText);
        
        coalitions.add(Coalition.getCoalitionBySide(Side.ALLIED));
        coalitions.add(Coalition.getCoalitionBySide(Side.AXIS));
    }

	public McuIcon (McuWaypoint waypoint)
	{
		super();
		position = waypoint.getPosition().copy();

		if (waypoint.getDesc().contains("Starting"))
		{
			iconId = McuIconIdType.ICON_ID_TAKEOFF;
			name = "Take Off";
			desc = "Take Off";
		}
		else if (waypoint.getDesc().contains("Landing"))
		{
			iconId = McuIconIdType.ICON_ID_LAND;
			name = "Land";
			desc = "Land";
		}
		else
		{
			iconId = McuIconIdType.ICON_ID_WAYPOINT;
			name = "Way Point";
			desc = "Way Point";
		}
		
		lCName = LCIndexGenerator.getInstance().getNextIndex();
		lCDesc = lCName;

		MissionStringHandler.getInstance().registerMissionText(lCName, name);
        
        coalitions.add(Coalition.getCoalitionBySide(Side.ALLIED));
        coalitions.add(Coalition.getCoalitionBySide(Side.AXIS));
	}

    public McuIcon (Balloon balloon)
    {
        super();
        
        this.iconId = McuIconIdType.ICON_ID_ENEMY_BALLOON;
        name = RoFPlaneAttributeMapping.BALLOON.getPlaneType();
        desc = RoFPlaneAttributeMapping.BALLOON.getPlaneType();
        position = balloon.getPosition().copy();
        
        lCName = LCIndexGenerator.getInstance().getNextIndex();
        lCDesc = lCName;

        MissionStringHandler.getInstance().registerMissionText(lCName, name);
        
        coalitions.add(Coalition.getCoalitionBySide(Side.ALLIED));
        coalitions.add(Coalition.getCoalitionBySide(Side.AXIS));
    }


    public McuIcon (IAirfield airfield)
    {
        super();
        
        this.iconId = McuIconIdType.ICON_ID_AIRFIELD;
        name = airfield.getName();
        desc = airfield.getName();
        position = airfield.getPosition().copy();
        
        lCName = LCIndexGenerator.getInstance().getNextIndex();
        lCDesc = lCName;

        MissionStringHandler.getInstance().registerMissionText(lCName, name);
        
        coalitions.add(Coalition.getCoalitionBySide(Side.ALLIED));
        coalitions.add(Coalition.getCoalitionBySide(Side.AXIS));
    }

    public McuIcon (FrontLinePoint frontLinePoint)
	{
        name = "";
        desc = "";
        
        lCName = LCIndexGenerator.getInstance().getNextIndex();
        lCDesc = lCName;
        MissionStringHandler.getInstance().registerMissionText(lCName, name);

        position = frontLinePoint.getPosition().copy();
        this.lineType = McuIconLineType.ICON_LINE_TYPE_POSITION;
        
        coalitions.add(Coalition.getCoalitionBySide(Side.ALLIED));
        coalitions.add(Coalition.getCoalitionBySide(Side.AXIS));
	}

	
    public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
        {
            writer.write("MCU_Icon");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("  Enabled = " + enabled + ";");
            writer.newLine();
            writer.write("  LCName = " + lCName + ";");
            writer.newLine();
            writer.write("  LCDesc = " + lCDesc + ";");
            writer.newLine();
             writer.write("  RColor = " + rColor + ";");
            writer.newLine();
            writer.write("  GColor = " + gColor + ";");
            writer.newLine();
            writer.write("  BColor = " + bColor + ";");
            writer.newLine();
            writer.write("  IconId = " + iconId.iconLineIdValue + ";");
            writer.newLine();
            writer.write("  LineType = " + lineType.iconLineTypeValue + ";");
            writer.newLine();

            CoalitionWriter.writeGenericCoalition(writer, coalitions);

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}	

    public ICountry getCountry(Date date) throws PWCGException
    {
        CountryDesignator countryDesignator = new CountryDesignator();
        return countryDesignator.determineCountry(this.position, date);
    }
    
    public void setColorBlue()
    {
    	rColor = 0;
    	gColor = 0;
    	bColor = 255;
    }

    public void setColorRed()
    {
    	rColor = 255;
    	gColor = 0;
    	bColor = 0;
    }

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

    public void setLineType(McuIconLineType lineType)
    {
        this.lineType = lineType;
    }

    public int getlCName()
    {
        return lCName;
    }

    public void setlCName(int lCName)
    {
        this.lCName = lCName;
    }

    public int getlCDesc()
    {
        return lCDesc;
    }

    public void setlCDesc(int lCDesc)
    {
        this.lCDesc = lCDesc;
    }

}

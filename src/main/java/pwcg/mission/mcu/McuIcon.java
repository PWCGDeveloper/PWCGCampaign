
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.plane.Balloon;
import pwcg.campaign.utils.LCIndexGenerator;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.mission.MissionStringHandler;
import pwcg.product.fc.plane.FCPlaneAttributeMapping;

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

    public McuIcon(String iconName, String iconText, Side side)
    {
        super();

        name = iconName;
        desc = iconName;

        MissionStringHandler.getInstance().registerMissionText(lCName, iconText);
        coalitions.add(Coalition.getCoalitionBySide(side));
    }

    public McuIcon(String iconName, String iconText)
    {
        super();

        name = iconName;
        desc = iconName;

        MissionStringHandler.getInstance().registerMissionText(lCName, iconText);
        coalitions.add(Coalition.getCoalitionBySide(Side.ALLIED));
        coalitions.add(Coalition.getCoalitionBySide(Side.AXIS));
    }

    public McuIcon(McuWaypoint waypoint, Side side)
    {
        super();
        position = waypoint.getPosition().copy();

        iconId = McuIconIdType.ICON_ID_WAYPOINT;

        name = waypoint.getName();

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (productSpecificConfiguration.usePosition1())
        {
            rColor = 0;
            gColor = 0;
            bColor = 0;

            lineType = McuIconLineType.ICON_LINE_TYPE_POSITION1;

            desc = waypoint.getName();
        }
        else
        {
            desc = waypoint.getName();
        }

        lCName = LCIndexGenerator.getInstance().getNextIndex();
        lCDesc = LCIndexGenerator.getInstance().getNextIndex();
        MissionStringHandler.getInstance().registerMissionText(lCName, name);
        MissionStringHandler.getInstance().registerMissionText(lCDesc, desc);
        coalitions.add(Coalition.getCoalitionBySide(side));
    }

    public McuIcon(McuTakeoff takeoff, Side side)
    {
        super();
        position = takeoff.getPosition().copy();
        iconId = McuIconIdType.ICON_ID_TAKEOFF;
        name = "Take Off";
        desc = "Take Off";

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (productSpecificConfiguration.usePosition1())
        {
            rColor = 0;
            gColor = 0;
            bColor = 0;

            lineType = McuIconLineType.ICON_LINE_TYPE_POSITION1;
        }

        lCName = LCIndexGenerator.getInstance().getNextIndex();
        lCDesc = lCName;

        MissionStringHandler.getInstance().registerMissionText(lCName, name);
        coalitions.add(Coalition.getCoalitionBySide(side));
    }

    public McuIcon(McuLanding landing, Side side)
    {
        super();
        position = landing.getPosition().copy();
        iconId = McuIconIdType.ICON_ID_LAND;
        name = "Land";
        desc = "Land";

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (productSpecificConfiguration.usePosition1())
        {
            rColor = 0;
            gColor = 0;
            bColor = 0;

            lineType = McuIconLineType.ICON_LINE_TYPE_POSITION1;
        }

        lCName = LCIndexGenerator.getInstance().getNextIndex();
        lCDesc = lCName;

        MissionStringHandler.getInstance().registerMissionText(lCName, name);
        coalitions.add(Coalition.getCoalitionBySide(side));
    }

    public McuIcon(Balloon balloon, Side side)
    {
        super();

        this.iconId = McuIconIdType.ICON_ID_ENEMY_BALLOON;
        name = FCPlaneAttributeMapping.BALLOON.getPlaneType();
        desc = FCPlaneAttributeMapping.BALLOON.getPlaneType();
        position = balloon.getPosition().copy();

        lCName = LCIndexGenerator.getInstance().getNextIndex();
        lCDesc = lCName;

        MissionStringHandler.getInstance().registerMissionText(lCName, name);

        coalitions.add(Coalition.getCoalitionBySide(Side.ALLIED));
        coalitions.add(Coalition.getCoalitionBySide(Side.AXIS));
    }

    public McuIcon(IAirfield airfield, Side side)
    {
        super();

        this.iconId = McuIconIdType.ICON_ID_AIRFIELD;
        name = airfield.getName();
        desc = airfield.getName();
        position = airfield.getPosition().copy();

        lCName = LCIndexGenerator.getInstance().getNextIndex();
        lCDesc = lCName;

        MissionStringHandler.getInstance().registerMissionText(lCName, name);
        coalitions.add(Coalition.getCoalitionBySide(side));
    }

    public McuIcon(FrontLinePoint frontLinePoint)
    {
        name = "";
        desc = "";

        lCName = LCIndexGenerator.getInstance().getNextIndex();
        lCDesc = lCName;
        MissionStringHandler.getInstance().registerMissionText(lCName, name);

        position = frontLinePoint.getPosition().copy();
        this.lineType = McuIconLineType.ICON_LINE_TYPE_POSITION0;

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

    public int getEnabled()
    {
        return enabled;
    }

    public void setEnabled(int enabled)
    {
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

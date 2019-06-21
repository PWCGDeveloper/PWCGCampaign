package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.mcu.McuIcon;

public class MissionFrontLineIconBuilder
{
    private Campaign campaign;
    private Mission mission;
    private List<McuIcon> alliedLineIcons = new ArrayList<>();
    private List<McuIcon> axisLineIcons = new ArrayList<>();
    
	public MissionFrontLineIconBuilder(Campaign campaign, Mission mission)
	{
		this.campaign = campaign;
		this.mission = mission;
	}

	public void buildFrontLineIcons() throws PWCGException
	{
		alliedLineIcons = createFrontPointIcons(Side.ALLIED);
		axisLineIcons = createFrontPointIcons(Side.AXIS);
	}

	public void write(BufferedWriter writer) throws PWCGIOException
	{
		for (McuIcon icon : alliedLineIcons)
		{
			icon.write(writer);
		}
		
		for (McuIcon icon : axisLineIcons)
		{
			icon.write(writer);
		}
	}

	private List<McuIcon> createFrontPointIcons(Side side) throws PWCGException
	{
        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
		List<FrontLinePoint> frontPointsForSide = frontLinesForMap.getFrontLines(side);

		List<McuIcon> iconsForFront = createFrontPoints(frontPointsForSide);
        setIconColors(side, iconsForFront);
        linkFrontLinePoints(iconsForFront);
        
        return iconsForFront;
	}

	private void setIconColors(Side side, List<McuIcon> iconsForFront)
	{
		for (McuIcon icon : iconsForFront)
        {
        	if (side == Side.AXIS)
        	{
        		icon.setColorBlue();
        	}
        	else
        	{
        		icon.setColorRed();
        	}
        }
	}

	private void linkFrontLinePoints(List<McuIcon> icons) throws PWCGException
	{
        for (int i = 1; i < icons.size(); ++i)
        {
        	McuIcon source = icons.get(i-1);
        	McuIcon target = icons.get(i);
        	
        	source.setTarget(target.getIndex());
        }
	}

	private List<McuIcon> createFrontPoints(List<FrontLinePoint> frontPointsForSide) throws PWCGException
	{
        CoordinateBox missionBorders = mission.getMissionBorders().expandBox(10000);
		List<McuIcon> iconsForFront = new ArrayList<>();
        for (int i = 0; i < frontPointsForSide.size(); ++i)
        {
            FrontLinePoint frontLinePoint = frontPointsForSide.get(i);
        	if (missionBorders.isInBox(frontLinePoint.getPosition()))
        	{
	        	if ((i % 2) == 0)
	        	{
	        		McuIcon icon = new McuIcon(frontLinePoint);
	        		iconsForFront.add(icon);
	        	}
        	}
        }
        
        return iconsForFront;
	}
}

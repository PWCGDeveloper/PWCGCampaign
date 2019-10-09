package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class PositionsManager 
{
	private ArrayList<Coordinate> alliedPositions = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> axisPositions = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> allAlliedPositions = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> allAxisPositions = new ArrayList<Coordinate>();

	public PositionsManager (Date date)
	{
		try
		{
			generateInfantryPositions(Side.AXIS, date, axisPositions, 30);
			generateInfantryPositions(Side.ALLIED, date, alliedPositions, 30);
			generateInfantryPositions(Side.AXIS, date, allAxisPositions, 100);
			generateInfantryPositions(Side.ALLIED, date, allAlliedPositions, 100);
		}
		catch (Exception e)
		{
            Logger.logException(e);
		}
	}
	

	public Coordinate getClosestDefinitePosition (Side side, Coordinate coord) 
		
	{
		ArrayList<Coordinate> positions = null;
		if (side == Side.ALLIED)
		{
			positions = allAlliedPositions;
		}
		else
		{
			positions = allAxisPositions;
		}

		return getClosestPosition(positions, coord);
	}

	public ArrayList<Coordinate> getPositions (ICountry country, CoordinateBox missionBorders) throws PWCGException 
	{
		ArrayList<Coordinate> positions = null;
		if (country.getSide() == Side.ALLIED)
		{
			positions = allAlliedPositions;
		}
		else
		{
			positions = allAxisPositions;
		}

		ArrayList<Coordinate>selectedPositions = new ArrayList<Coordinate>();
		for (int i = 1; i < positions.size(); ++i)
		{
			Coordinate position = positions.get(i).copy();
			if (missionBorders.isInBox(position))
			{
				selectedPositions.add(position);
			}
		}

		return selectedPositions;
	}	

	private void generateInfantryPositions (Side side, 
											Date date,
										    ArrayList<Coordinate> positions,
										    int odds) throws PWCGException 
	{
		
		FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
		List<FrontLinePoint>frontLines = frontLineMarker.getFrontLines(side);

		for (int i = 1; i < frontLines.size(); ++i)
		{
			Coordinate prevCoord = frontLines.get(i-1).getPosition().copy();
			Coordinate currentCoord = frontLines.get(i).getPosition().copy();
			
			int roll = RandomNumberGenerator.getRandom(100);
			if (roll < odds)
			{
				boolean addPosition = false;
				if(positions.size() == 0)
				{
					addPosition = true;
				}
				else
				{
					Coordinate lastPosition = positions.get(positions.size() - 1);
					
					double distance = MathUtils.calcDist(lastPosition, currentCoord);
					if (distance > 7000.0)
					{
						addPosition = true;
					}
				}
				if (addPosition)
				{
					if (side == Side.ALLIED)
					{
						Coordinate coord = createAllied(prevCoord, currentCoord);
						positions.add(coord);
					}
					else
					{
						Coordinate coord = createGerman(prevCoord, currentCoord);
						positions.add(coord);
					}
				}
			}
		}
	}

	private Coordinate createGerman(Coordinate p1, Coordinate p2) throws PWCGException 
	{
		double angle = MathUtils.calcAngle(p2, p1) ;
		angle = MathUtils.adjustAngle (angle, 90);
		Coordinate position = MathUtils.calcNextCoord(p1, angle, 7000.0);
		
		return position;
	}

	private Coordinate createAllied(Coordinate p1, Coordinate p2) throws PWCGException 
	{
		double angle = MathUtils.calcAngle(p2, p1);
		angle = MathUtils.adjustAngle (angle, -90);
		Coordinate position = MathUtils.calcNextCoord(p1, angle, 7000.0);
		
		return position;
	}

	private Coordinate getClosestPosition (ArrayList<Coordinate> positions, Coordinate coord) 
	{
		Coordinate selectedPosition = null;

		double closestDistance = 10000000.0;
		for (int i = 1; i < positions.size(); ++i)
		{
			Coordinate position = positions.get(i).copy();
			double thisDistance = MathUtils.calcDist(coord, position);
			if(thisDistance < closestDistance)
			{
				closestDistance = thisDistance;
				selectedPosition = position;
			}
		}

		return selectedPosition;
	}
}

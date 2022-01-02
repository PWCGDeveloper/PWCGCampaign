package pwcg.campaign.context;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;

public class BehindEnemyLines 
{    
	private Date date;
	private String reason = "";
	private String reasonCode = "";
	
    public BehindEnemyLines (Date date)
    {
    	this.date = date;
    }

    public boolean isBehindEnemyLinesForCapture(FrontMapIdentifier mapId, Coordinate landingCoordinates, Side friendlySide) throws PWCGException 
    {
    	FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMapByMapId(mapId).getFrontLinesForMap(date);
        
        Side enemySide = friendlySide.getOppositeSide();

        boolean behindEnemyLines = true;
        
        Coordinate friendlyCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(landingCoordinates, friendlySide);
        Coordinate enemyCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(friendlyCoordinates, enemySide);

        boolean inNoMansLand = inNoMansLand(landingCoordinates, friendlyCoordinates, enemyCoordinates);
        if (inNoMansLand)
        {
        	reasonCode = "NML";
            return false;
        }

        behindEnemyLines = behindLinesByFrontLineDistance(landingCoordinates, friendlyCoordinates, enemyCoordinates);
        if (!behindEnemyLines)
        {
        	reasonCode = "Friendly Territory";
            return false;
        }
        
        behindEnemyLines = notBehindLinesByAirfield(landingCoordinates, friendlySide, behindEnemyLines);
        if (!behindEnemyLines)
        {
        	reasonCode = "Friendly Airfield";
            return false;
        }

        behindEnemyLines = behindLinesByGroup(landingCoordinates, friendlySide, behindEnemyLines);
        if (!behindEnemyLines)
        {
        	reasonCode = "Friendly Group";
            return false;
        }
        
        return true;
    }

    public boolean inReportingRange(FrontMapIdentifier mapId, Coordinate landingCoordinates, Side friendlySide) throws PWCGException 
    {
    	FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMapByMapId(mapId).getFrontLinesForMap(date);

        Side enemySide = friendlySide.getOppositeSide();

        boolean behindEnemyLines = false;
        
        Coordinate friendlyCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(landingCoordinates, friendlySide);
        Coordinate enemyCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(landingCoordinates, enemySide);

       boolean inNoMansLand = inNoMansLand(landingCoordinates, friendlyCoordinates, enemyCoordinates);
        if (!inNoMansLand)
        {
            return true;
        }

        behindEnemyLines = behindLinesByFrontLineDistance(landingCoordinates, friendlyCoordinates, enemyCoordinates);
        if (behindEnemyLines)
        {
            return true;
        }

        behindEnemyLines = behindLinesByGroup(landingCoordinates, friendlySide, behindEnemyLines);
        if (behindEnemyLines)
        {
            return true;
        }
        
        behindEnemyLines = notBehindLinesByAirfield(landingCoordinates, friendlySide, behindEnemyLines);
        if (behindEnemyLines)
        {
            return true;
        }
        
        return false;
    }

    public double getDistanceBehindLines(FrontMapIdentifier mapId, Coordinate landingCoordinates, Side friendlySide) throws PWCGException 
    {
        Side enemySide = friendlySide.getOppositeSide();
       
    	FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMapByMapId(mapId).getFrontLinesForMap(date);
        Coordinate enemyCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(landingCoordinates, enemySide);

        double distanceToEnemy = MathUtils.calcDist(landingCoordinates, enemyCoordinates);
        
        return distanceToEnemy;
    }

    private boolean notBehindLinesByAirfield(Coordinate landingCoordinates, Side crewMemberSide, boolean behindEnemyLines)
                    throws PWCGException
    {
        Airfield closestAirfield =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().findClosestAirfield(landingCoordinates);
        if (closestAirfield != null)
        {
            if (closestAirfield.getCountry(date).getCountry() == Country.NEUTRAL)
            {
            	reason = "You found friendly positions after coming down near the contested airfield of " + closestAirfield.getName();
                behindEnemyLines = false;
            }
            
            if (crewMemberSide == closestAirfield.determineCountryOnDate(date).getSide())
            {
            	reason = "You found friendly positions after coming down near the friendly airfield of " + closestAirfield.getName();
                behindEnemyLines = false;
            }
        }
        else
        {
            behindEnemyLines = false;
        }
        return behindEnemyLines;
    }

    private boolean behindLinesByGroup(Coordinate landingCoordinates, Side crewMemberSide, boolean behindEnemyLines)
                    throws PWCGException
    {
        PWCGLocation closestTown =  PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(landingCoordinates);
        if (closestTown != null)
        {
            if (closestTown.getCountry(date).getSide() == Side.NEUTRAL)
            {
            	reason = "You found friendly positions after coming down near the contested town of " + closestTown.getName();
                behindEnemyLines = false;
            }
            
            if (crewMemberSide == closestTown.getCountry(date).getSide())
            {
            	reason = "You found friendly positions after coming down near the friendly town of " + closestTown.getName();
                behindEnemyLines = false;
            }
        }
        else
        {
        	reason = "You escaped capture when you were fortuante enough to land in an unoccupied area";
            behindEnemyLines = false;
        }
        return behindEnemyLines;
    }

    private boolean behindLinesByFrontLineDistance(Coordinate landingCoordinates, Coordinate friendlyCoordinates, Coordinate enemyCoordinates)
    {
        double distanceToEnemy = MathUtils.calcDist(landingCoordinates, enemyCoordinates);
        double distanceToFriendly = MathUtils.calcDist(landingCoordinates, friendlyCoordinates);

        boolean behindEnemyLines = true;
        
        if (distanceToFriendly < distanceToEnemy)
        {
        	reason = "You escaped capture because you were fortunate enough to avoid enemy positions";
            behindEnemyLines = false;
        }

        if (distanceToFriendly < 3000.0)
        {
        	reason = "You escaped capture landing close to one of our positions.";
            behindEnemyLines = false;
        }
        
        return behindEnemyLines;
    }

    private boolean inNoMansLand(Coordinate landingCoordinates, Coordinate friendlyCoordinates,
                    Coordinate enemyCoordinates) throws PWCGException
    {
        boolean inNoMansLand = false;

        double angleToEnemy = MathUtils.calcAngle(landingCoordinates, enemyCoordinates);
        double angleToFriendly = MathUtils.calcAngle(landingCoordinates, friendlyCoordinates);

        double differenceInAngle = angleToEnemy - angleToFriendly;
        if (Math.abs(differenceInAngle) > 100)
        {
        	reason = "You escaped capture after landing in no mans land";
            inNoMansLand = true;
        }
        
        return inNoMansLand;
    }

	public String getReason()
	{
		return reason;
	}

	public String getReasonCode()
	{
		return reasonCode;
	}
    
    
}

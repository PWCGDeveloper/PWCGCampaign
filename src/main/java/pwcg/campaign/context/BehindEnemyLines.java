package pwcg.campaign.context;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;

public class BehindEnemyLines
{
    private Campaign campaign;
    private String reason = "";
    private String reasonCode = "";

    public BehindEnemyLines(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public boolean isBehindEnemyLinesForCapture(Coordinate landingCoordinates, Side friendlySide) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMapByMapId(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());

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

    public boolean inReportingRange(Coordinate landingCoordinates, Side friendlySide) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMapByMapId(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());

        Side enemySide = friendlySide.getOppositeSide();

        boolean behindEnemyLines = false;

        Coordinate friendlyCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(landingCoordinates, friendlySide);
        Coordinate enemyCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(landingCoordinates, enemySide);

        boolean inNoMansLand = inNoMansLand(landingCoordinates, friendlyCoordinates, enemyCoordinates);
        if (!inNoMansLand)
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

        behindEnemyLines = behindLinesByFrontLineDistance(landingCoordinates, friendlyCoordinates, enemyCoordinates);
        if (behindEnemyLines)
        {
            return true;
        }

        return false;
    }

    public double getDistanceBehindLines(Coordinate landingCoordinates, Side friendlySide) throws PWCGException
    {
        Side enemySide = friendlySide.getOppositeSide();

        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMapByMapId(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());
        Coordinate enemyCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(landingCoordinates, enemySide);

        double distanceToEnemy = MathUtils.calcDist(landingCoordinates, enemyCoordinates);

        return distanceToEnemy;
    }

    private boolean notBehindLinesByAirfield(Coordinate landingCoordinates, Side pilotSide, boolean behindEnemyLines) throws PWCGException
    {
        Airfield closestAirfield = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getAirfieldManager().getAirfieldFinder()
                .findClosestAirfield(landingCoordinates);
        if (closestAirfield != null)
        {
            if (closestAirfield.getCountry(campaign.getCampaignMap(), campaign.getDate()).getCountry() == Country.NEUTRAL)
            {
                reason = "You found friendly positions after coming down near the contested airfield of " + closestAirfield.getName();
                behindEnemyLines = false;
            }

            if (pilotSide == closestAirfield.determineCountryOnDate(campaign.getCampaignMap(), campaign.getDate()).getSide())
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

    private boolean behindLinesByGroup(Coordinate landingCoordinates, Side pilotSide, boolean behindEnemyLines) throws PWCGException
    {
        PWCGLocation closestTown = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager().getTownFinder()
                .findClosestTown(landingCoordinates);
        if (closestTown != null)
        {
            if (closestTown.getCountry(campaign.getCampaignMap(), campaign.getDate()).getSide() == Side.NEUTRAL)
            {
                reason = "You found friendly positions after coming down near the contested town of " + closestTown.getName();
                behindEnemyLines = false;
            }

            if (pilotSide == closestTown.getCountry(campaign.getCampaignMap(), campaign.getDate()).getSide())
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

    private boolean inNoMansLand(Coordinate landingCoordinates, Coordinate friendlyCoordinates, Coordinate enemyCoordinates) throws PWCGException
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

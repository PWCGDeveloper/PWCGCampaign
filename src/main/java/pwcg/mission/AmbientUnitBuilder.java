package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public abstract class AmbientUnitBuilder
{
    protected ICountry attackingCountry = null;
    protected ICountry targetCountry = null;
    
    protected Mission mission = null;
    protected Campaign campaign = null;
    protected ICountry country = null;

    public AmbientUnitBuilder (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }
    
    protected void chooseSides() throws PWCGException
    {
        ICountry alliedCountry = CountryFactory.makeMapReferenceCountry(Side.ALLIED);
        ICountry axisCountry = CountryFactory.makeMapReferenceCountry(Side.AXIS);

        if (campaign.determineCountry().getSide() == alliedCountry.getSide())
        {
            country = axisCountry;
        }
        else
        {
            country = alliedCountry;
        }
    }

    protected boolean isBehindFrontLines(Coordinate position, Side targetSide) throws PWCGException
    {
        Coordinate closestFrontCoordinate = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate()).findClosestFrontCoordinateForSide(position, targetSide);            
        if (MathUtils.calcDist(closestFrontCoordinate, position) > 5000.0)
        {
            return true;
        }
        
        return false;
    }

}

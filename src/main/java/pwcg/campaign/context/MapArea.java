package pwcg.campaign.context;

import pwcg.core.location.Coordinate;

public abstract class MapArea
{    
    protected double xMin = 0.0;
    protected double xMax = 0.0;
    protected double zMin = 0.0;
    protected double zMax = 0.0;
    
    public double getxMin()
    {
        return this.xMin;
    }
    
    public void setxMin(double xMin)
    {
        this.xMin = xMin;
    }
    
    public double getxMax()
    {
        return this.xMax;
    }
    
    public void setxMax(double xMax)
    {
        this.xMax = xMax;
    }
    
    public double getzMin()
    {
        return this.zMin;
    }
    
    public void setzMin(double zMin)
    {
        this.zMin = zMin;
    }
    
    public double getzMax()
    {
        return this.zMax;
    }
    
    public void setzMax(double zMax)
    {
        this.zMax = zMax;
    }
    
    public Coordinate getCenter()
    {
        double xCenter = xMin + xMax / 2;
        double zCenter = zMin + zMax / 2;
        return new Coordinate(xCenter, 0.0, zCenter);
    }
    
    public boolean isInMapArea(Coordinate position)
    {
        if (position.getXPos() < xMin)
        {
            return false;
        }
        if (position.getXPos() > xMax)
        {
            return false;
        }
        if (position.getZPos() < zMin)
        {
            return false;
        }
        if (position.getZPos() > zMax)
        {
            return false;
        }
        
        return true;
    }
}

package pwcg.campaign.context;

import pwcg.core.location.Coordinate;

public abstract class FrontParameters
{    
    static public double MIN_DISTANCE_FROM_BORDER = 5000.0;
    

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
}

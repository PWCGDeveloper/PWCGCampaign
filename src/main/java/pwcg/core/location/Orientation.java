package pwcg.core.location;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;

public class Orientation implements Cloneable
{
	private double xOri = 0.0; // always 0
	private double yOri = 0.0;
	private double zOri = 0.0; // always 0

	public Orientation ()
	{
	}
	
	public Orientation(double angle)
    {
        this.yOri = angle;
    }

    public Orientation copy()
	{
		Orientation ori = new Orientation();
		ori.yOri = this.yOri;
		
		return ori;
	}

	public double getyOri() {
		return yOri;
	}

	public double getxOri() {
		return xOri;
	}

	public double getzOri() {
		return zOri;
	}

	public void setyOri(double yOri) throws PWCGException
	{
		yOri = MathUtils.adjustAngle (yOri, 0);		
		if (Double.isNaN(yOri))
		{
			throw new PWCGException ("Invalid orientation:" + yOri);
		}
		
		this.yOri = yOri;
	}
	
	
	public void write (BufferedWriter writer) throws PWCGIOException
	{
	    try
	    {
    		writer.write("  XOri = " + Coordinate.format(xOri) + ";");
    		writer.newLine();
    		writer.write("  YOri = " + Coordinate.format(yOri) + ";");
    		writer.newLine();
    		writer.write("  ZOri = " + Coordinate.format(zOri) + ";");
    		writer.newLine();
	    }
	    catch (IOException e)
	    {
	        Logger.logException(e);
	        throw new PWCGIOException(e.getMessage());
	    }
	}

}

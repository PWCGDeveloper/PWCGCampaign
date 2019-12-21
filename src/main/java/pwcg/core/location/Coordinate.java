package pwcg.core.location;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Formatter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

public class Coordinate implements Cloneable
{
	private double xPos;
	private double yPos;
	private double zPos;
	
    public Coordinate ()
    {
    }
    
    public Coordinate (double xPos, double yPos, double zPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }
    
	public Coordinate copy()
	{
		Coordinate coord = new Coordinate();
		coord.xPos = this.getXPos();
		coord.yPos = this.getYPos();
		coord.zPos = this.getZPos();
				
		return coord;
	}

	public void setXPos(double xPos) throws PWCGException
	{
		if (Double.isNaN(xPos))
		{
			throw new PWCGException ("Invalid X Position calculation");
		}

		this.xPos = xPos;
	}

	public void setYPos(double yPos)
	{
		this.yPos = yPos;
	}

	public void setZPos(double zPos)  throws PWCGException
	{
		if (Double.isNaN(zPos))
		{
			throw new PWCGException ("Invalid Z Position calculation");
		}

		this.zPos = zPos;
	}

	public double getXPos() 
	{
		return xPos;
	}

	public double getYPos()
	{
		return yPos;
	}

	public double getZPos() 
	{
		return zPos;
	}
	
	public String toString()
	{
		Formatter formatter = new Formatter();
	    String out = formatter.format("XPOS: %6.0f     ZPOS: %6.0f     YPOS: %6.0f", xPos / 1000, zPos / 1000, yPos / 1000).toString();
	    formatter.close();
	    
		return out;
	}
	
	/**
	 * @param writer
	 * @throws PWCGIOException
	 */
	public void write(BufferedWriter writer) throws PWCGIOException
	{
        try
        {
    		writer.write("  XPos = " + Coordinate.format(xPos) + ";");
    		writer.newLine();
    		writer.write("  YPos = " + Coordinate.format(yPos) + ";");
    		writer.newLine();
    		writer.write("  ZPos = " + Coordinate.format(zPos) + ";");
    		writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String format(double value)
	{
		int placesPlusDecimal = 3;
		String doubleString = Double.valueOf(value).toString();
		int decimalIndex = doubleString.indexOf('.');
		// No decimal point fond - pad by three
		if (decimalIndex == -1)
		{
			doubleString += ".00";
		}
		// Too long - shorten to three decimal places
		if (doubleString.length() > (decimalIndex + placesPlusDecimal))
		{
			doubleString = doubleString.substring(0, decimalIndex + placesPlusDecimal);
		}
		// Too short - pad to three places
		else if (doubleString.length() < (decimalIndex + placesPlusDecimal))
		{
			for (int i = doubleString.length(); i < (decimalIndex + placesPlusDecimal); ++i)
			{
				doubleString += '0';
			}
		}

		return doubleString;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other)
	{
		boolean isEqual = false;
		
		if (other != null)
		{
			if (other instanceof Coordinate)
			{
				Coordinate otherCoord = (Coordinate)other;
				if (this.xPos == otherCoord.xPos && 
					this.yPos == otherCoord.yPos && 
					this.zPos == otherCoord.zPos)
				{
					isEqual = true;
				}
			}
		}
		
		return isEqual;
	}
}

package pwcg.core.logfiles.event;

import java.io.BufferedWriter;
import java.util.HashMap;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

public abstract class ATypeBase 
{	
    private static int sequenceNumCounter = 0;
    private static HashMap<String,Integer> generations = new HashMap<>();
	private int sequenceNum = 0;
	private AType atype;
	
	public static void reset()
	{
	    generations = new HashMap<>();
	}

	public ATypeBase (AType atype)
	{
	    this.atype = atype;
	    this.sequenceNum = sequenceNumCounter;
	    ++sequenceNumCounter;
	}

	protected Coordinate findCoordinate(String line, String startTag) throws PWCGException 
	{
		try
        {
		    int startPosition = line.indexOf(startTag);
		    int endPosition = line.substring(startPosition).indexOf(")");
		    String positionString = line.substring(startPosition, startPosition+endPosition+1);

            // Get the location
            int startLocXPos = positionString.indexOf(startTag) + startTag.length();
            String endLocXTag = ",";
            int endLocXPos = positionString.indexOf(endLocXTag);
            String locXStr = positionString.substring(startLocXPos, endLocXPos);
            
            int startLocYPos = endLocXPos + 1;
            int endLocYPos = positionString.indexOf(",", startLocYPos);
            String locYStr = positionString.substring(startLocYPos, endLocYPos);
            
            int startLocZPos = endLocYPos + 1;
            int endLocZPos = positionString.indexOf(")", startLocZPos);
            String locZStr = positionString.substring(startLocZPos, endLocZPos);
            
            Coordinate coords = new Coordinate();
            coords.setXPos(Double.valueOf(locXStr));
            coords.setYPos(Double.valueOf(locYStr));
            coords.setZPos(Double.valueOf(locZStr));
            
            return coords;
        }
        catch (NumberFormatException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }

	}

	protected String getString(String line, String startTag, String endTag) 
	{
	    String lineLower = line.toLowerCase();
        startTag = startTag.toLowerCase();
		int startPos = lineLower.indexOf(startTag) + startTag.length();
		String value = "";
		if(endTag != null)
		{
	        endTag = endTag.toLowerCase();
			int endPos = lineLower.indexOf(endTag);
			value = line.substring(startPos, endPos);
		}
		else
		{
			value = line.substring(startPos);
		}
		return value;
	}

	protected int getInteger(String line, String startTag, String endTag) 
	{
		int startPos = line.indexOf(startTag) + startTag.length();
		int endPos = line.indexOf(endTag);
		int value = Integer.valueOf(line.substring(startPos, endPos));
		
		return value;
	}

	protected double getDouble(String line, String startTag, String endTag) 
	{
		int startDamagePos = line.indexOf(startTag) + startTag.length();
		int endDamagePos = line.indexOf(endTag);
		double value = Double.valueOf(line.substring(startDamagePos, endDamagePos));
		
		return value;
	}

	protected String getNewId(String line, String startTag, String endTag)
	{
	    String raw_id = getString(line, startTag, endTag);
	    int generation = generations.getOrDefault(raw_id, 0);
	    generation++;
	    generations.put(raw_id, generation);
	    return raw_id + "@" + generation;
	}

	protected String getId(String line, String startTag, String endTag)
	{
        String raw_id = getString(line, startTag, endTag);
	    if (raw_id.equals("-1"))
	        return raw_id;
        // Eject events can be emitted before their spawn event, so assume first generation
        return raw_id + "@" + generations.getOrDefault(raw_id, 1);
	}

	public int getSequenceNum() 
	{
		return sequenceNum;
	}
	
    public AType getAtype()
    {
        return atype;
    }

    public abstract void write(BufferedWriter writer) throws PWCGException;
}

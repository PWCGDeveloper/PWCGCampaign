package pwcg.core.logfiles.event;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

public class GreatBattlesFileParseUtils
{

    public static Coordinate findCoordinate(String line, String startTag) throws PWCGException 
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

    public static String getString(String line, String startTag, String endTag) 
    {
        String lineLower = line.toLowerCase();
        startTag = startTag.toLowerCase();
        int startPos = lineLower.indexOf(startTag) + startTag.length();
        String value = "";
        if(endTag != null && !endTag.isEmpty())
        {
            endTag = endTag.toLowerCase();
            String fromStartPos = line.substring(startPos);
            int endPos = startPos + fromStartPos.indexOf(endTag);
            value = line.substring(startPos, endPos);
        }
        else
        {
            value = line.substring(startPos);
        }
        return value;
    }

    public static int getInteger(String line, String startTag, String endTag) 
    {
        int startPos = line.indexOf(startTag) + startTag.length();
        int endPos = line.indexOf(endTag);
        int value = Integer.valueOf(line.substring(startPos, endPos));
        
        return value;
    }

    public static double getDouble(String line, String startTag, String endTag) 
    {
        int startDamagePos = line.indexOf(startTag) + startTag.length();
        int endDamagePos = line.indexOf(endTag);
        double value = Double.valueOf(line.substring(startDamagePos, endDamagePos));
        
        return value;
    }
}
package pwcg.core.logfiles.event;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

// T:138300 AType:6 PID:302079 POS(64488.945, 174.336, 41086.227)
public class AType6 extends ATypeBase implements IAType6
{
	private String pid = "";
	protected Coordinate location;

    public AType6(String line) throws PWCGException
    {    
        super(AType.ATYPE6);
        parse(line);
    }
    
    private void parse (String line) throws PWCGException 
    {
		pid = getId(line, "PID:", " POS(");
		location = findCoordinate(line, "POS(");
	}
	
    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            String format = "T:14605 AType:6 PID:%s POS(%.1f,%.1f,%.1f)";
            
            String atype = String.format(format, pid.split("@")[0], location.getXPos(),location.getYPos(), location.getZPos());
            writer.write(atype);
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }   
	
	public String getPid()
    {
        return pid;
    }

    public Coordinate getLocation() {
		return location;
	}
}

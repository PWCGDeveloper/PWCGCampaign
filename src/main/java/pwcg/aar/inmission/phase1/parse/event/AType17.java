package pwcg.aar.inmission.phase1.parse.event;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;


// T:14605 AType:17 ID:129023 POS(97132.141,1781.138,133919.984)
public class AType17 extends ATypeBase implements IAType17
{
	private String id;
	private Coordinate location;

    public AType17(String line) throws PWCGException  
    {
        super(AType.ATYPE17);
        parse(line);
    }

    public AType17(IAType10 atype10)  
    {
        super(AType.ATYPE17);
        id = atype10.getId();
        location = atype10.getLocation().copy();
    }

    private void parse (String line) throws PWCGException 
	{
		id = getId(line, "AType:17 ID:", " POS(");
		location = findCoordinate(line, "POS(");		
	}
	
    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            String format = "T:14605 AType:17 ID:%s POS(%.1f,%.1f,%.1f)";
            
            String atype = String.format(format, id.split("@")[0], location.getXPos(),location.getYPos(), location.getZPos());
            writer.write(atype);
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }   

	@Override
    public String getId() 
	{
		return id;
	}

	@Override
    public Coordinate getLocation() 
	{
		return location;
	}
}

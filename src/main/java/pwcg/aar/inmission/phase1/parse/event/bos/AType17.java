package pwcg.aar.inmission.phase1.parse.event.bos;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.aar.inmission.phase1.parse.event.ATypeBase;
import pwcg.aar.inmission.phase1.parse.event.IAType10;
import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;


// T:14605 AType:17 ID:129023 POS(97132.141,1781.138,133919.984)
public class AType17 extends ATypeBase implements IAType17
{
	private String id;
	private Coordinate location;

    public AType17(String line) throws PWCGException  
    {
        super();
        parse(line);
    }

    public AType17(IAType10 atype10)  
    {
        super();
        id = atype10.getId();
        location = atype10.getLocation().copy();
    }

    private void parse (String line) throws PWCGException 
	{
		id = getString(line, "AType:17 ID:", " POS(");
		location = findCoordinate(line, "POS(");		
	}
	
    @Override
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        try
        {
            String format = "T:14605 AType:17 ID:%s POS(%.1f,%.1f,%.1f)";
            
            String atype = String.format(format, id, location.getXPos(),location.getYPos(), location.getZPos());
            writer.write(atype);
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
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

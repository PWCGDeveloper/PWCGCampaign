package pwcg.aar.inmission.phase1.parse.event.rof;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.aar.inmission.phase1.parse.event.ATypeBase;
import pwcg.aar.inmission.phase1.parse.event.IAType3;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;

// T:54877 AType:3 AID:-1 TID:35839 POS(112150.266,93.277,111696.758)
public class AType3 extends ATypeBase implements IAType3
{
    private String victor = "";
    private String victim = "";
    protected Coordinate location;

    public AType3(String line) throws PWCGException
    {    
        super();
        parse(line);
    }
    
    private void parse (String line) throws PWCGException 
    {
        victor = getString(line, "AID:", " TID:"); // Victor
        victim = getString(line, "TID:", " POS("); // Victim
        location = findCoordinate(line, "POS(");
    }

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            String format = "T:14605 AType:3 AID:%s TID:%s POS(%.1f,%.1f,%.1f)";

            String atype = String.format(format, victor, victim, location.getXPos(), location.getYPos(), location.getZPos());
            writer.write(atype);
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public String getVictor()
    {
        return victor;
    }

    public String getVictim()
    {
        return victim;
    }

    public Coordinate getLocation()
    {
        return location;
    }
}

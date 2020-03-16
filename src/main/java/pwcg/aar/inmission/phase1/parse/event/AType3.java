package pwcg.aar.inmission.phase1.parse.event;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

//T:105391 AType:3 AID:1680383 TID:2256895 POS(155892.000,171.120,22055.320)
public class AType3 extends ATypeBase implements IAType3
{
    private String victor = "";
    private String victim = "";
    protected Coordinate location;

    public AType3(String line) throws PWCGException
    {    
        super(AType.ATYPE3);
        parse(line);
    }

    public AType3(String victor, String victim, Coordinate location) throws PWCGException
    {    
        super(AType.ATYPE3);
        this.victor = victor;
        this.victim = victim;
        this.location = location;
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
            PWCGLogger.logException(e);
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

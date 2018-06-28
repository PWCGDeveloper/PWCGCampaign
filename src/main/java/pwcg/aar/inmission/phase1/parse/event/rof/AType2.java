package pwcg.aar.inmission.phase1.parse.event.rof;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.aar.inmission.phase1.parse.event.ATypeBase;
import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;

//T:54991 AType:2 DMG:0.220 AID:-1 TID:36863 POS(112185.984,73.297,111706.273)        
public class AType2  extends ATypeBase implements IAType2
{
	private String aid = "";
	private String tid = "";
	private double damageLevel = 0.0;
	protected Coordinate location;

    public AType2(String line) throws PWCGException
    {    
        super();
        parse(line);
    }
    
    private void parse (String line) throws PWCGException 
    {
		damageLevel = getDouble(line, "DMG:", " AID:");
		aid = getString(line, "AID:", " TID:");
		tid = getString(line, "TID:", " POS(");
		location = findCoordinate(line, "POS(");
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        try
        {
            String format = "T:14605 AType:2 DMG:%.3f AID:%s TID:%s POS(%.1f,%.1f,%.1f)";
            
            String atype = String.format(format, damageLevel, aid, tid, location.getXPos(),location.getYPos(), location.getZPos());
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
    public String getVictor() {
		return aid;
	}

	@Override
    public String getVictim() {
		return tid;
	}

	@Override
    public double getDamageLevel() {
		return damageLevel;
	}

	@Override
    public Coordinate getLocation() {
		return location;
	}
}

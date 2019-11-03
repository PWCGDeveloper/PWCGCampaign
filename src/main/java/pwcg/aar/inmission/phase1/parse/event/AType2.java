package pwcg.aar.inmission.phase1.parse.event;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;

// T:80867 AType:2 DMG:0.082 AID:302079 TID:1351679 POS(155995.594,172.822,21908.119)
public class AType2  extends ATypeBase implements IAType2
{
	private String aid = AARLogParser.UNKNOWN_MISSION_LOG_ENTITY;
	private String tid = AARLogParser.UNKNOWN_MISSION_LOG_ENTITY;
	private double damageLevel = 0.0;
	protected Coordinate location;

    public AType2(String line) throws PWCGException
    {    
        super(AType.ATYPE2);
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

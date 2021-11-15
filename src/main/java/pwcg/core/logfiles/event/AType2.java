package pwcg.core.logfiles.event;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

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

    public AType2(String aid, String tid, double damageLevel, Coordinate location) throws PWCGException
    {    
        super(AType.ATYPE2);
        this.aid = aid;
        this.tid = tid;
        this.damageLevel = damageLevel;
        this.location = location;
    }
    
    private void parse (String line) throws PWCGException 
    {
		damageLevel = getDouble(line, "DMG:", " AID:");
		aid = getId(line, "AID:", " TID:");
		tid = getId(line, "TID:", " POS(");
		location = findCoordinate(line, "POS(");
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
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
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
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

package pwcg.aar.inmission.phase1.parse.event;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

// missionReport(2018-03-19_08-04-13)[0].txt:T:15 AType:10 PLID:302079 PID:303103 BUL:3056 SH:0 BOMB:17 RCT:0 (64429.594,174.325,41093.000) IDS:beadb889-54ff-4b2c-ba6c-185bc706600b LOGIN:6cfc9723-e261-44cc-b59c-1a1abc71edb5 NAME:PatrickAWlson TYPE:He 111 H-6 COUNTRY:201 FORM:1 FIELD:236543 INAIR:1 PARENT:-1 ISPL:1 ISTSTART:1
public class AType10 extends ATypeBase implements IAType10
{
	private String id;
	private String type;
	private ICountry country;
	private Coordinate location;


    public AType10(String line) throws PWCGException 
    {
        super(AType.ATYPE10);
        parse(line);
    }
    
    private void parse (String line) throws PWCGException 
	{
		id = getId(line, "AType:10 PLID:", " TYPE:");
		type = getString(line, " TYPE:", " COUNTRY:");
		location = findCoordinate(line, "RCT:");

		int countryCode = getInteger(line, "COUNTRY:", " FORM:") ;
        country = CountryFactory.makeCountryByCode(countryCode);
	}

    @Override
	protected Coordinate findCoordinate(String line, String startTag) throws PWCGException 
	{
		try
        {
            int startLocXPos = line.indexOf(startTag) + startTag.length();
            String posSubString = line.substring(startLocXPos);
            startLocXPos += posSubString.indexOf('(') + 1;
            
            String endLocXTag = ",";
            int endLocXPos = line.indexOf(endLocXTag);
            String locXStr = line.substring(startLocXPos, endLocXPos);
            
            int startLocYPos = endLocXPos + 1;
            int endLocYPos = line.indexOf(",", startLocYPos);
            String locYStr = line.substring(startLocYPos, endLocYPos);
            
            int startLocZPos = endLocYPos + 1;
            int endLocZPos = line.indexOf(")", startLocZPos);
            String locZStr = line.substring(startLocZPos, endLocZPos);
            
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

	@Override
    public String getId() {
		return id;
	}

	@Override
    public String getType() {
		return type;
	}

	@Override
    public ICountry getCountry() {
		return country;
	}

	@Override
    public Coordinate getLocation() 
	{
		return location;
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            String format = "AType:10 PLID:130047 PID:131071 BUL:1850 SH:0 BOMB:0 RCT:0 (%.1f,%.1f,%.1f) IDS:a1e32593-94c5-4e1a-a4ac-a583aaee570e LOGIN:580c2eb7-fc7f-4d4e-b25f-098946515ce1 NAME:PatrickAWilson TYPE:%s COUNTRY:%d FORM:1 FIELD:0 INAIR:0 PARENT:-1 PAYLOAD:5 FUEL:1.000 SKIN: WM:1";
            
            String atype = String.format(format, location.getXPos(),location.getYPos(), location.getZPos(), type, country.getCountryCode());
            writer.write(atype);
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
}

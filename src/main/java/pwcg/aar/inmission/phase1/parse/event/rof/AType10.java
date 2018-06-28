package pwcg.aar.inmission.phase1.parse.event.rof;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.aar.inmission.phase1.parse.event.ATypeBase;
import pwcg.aar.inmission.phase1.parse.event.IAType10;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;

// AType:10 PLID:130047 PID:131071 BUL:1850 SH:0 BOMB:0 RCT:0 (280249.000,4.706,29009.000) IDS:a1e32593-94c5-4e1a-a4ac-a583aaee570e LOGIN:580c2eb7-fc7f-4d4e-b25f-098946515ce1 NAME:Patrik Schorner TYPE:R.E.8 COUNTRY:102 FORM:1 FIELD:0 INAIR:0 PARENT:-1 PAYLOAD:5 FUEL:1.000 SKIN: WM:1                 
public class AType10 extends ATypeBase implements IAType10
{
	private String id;
	private String type;
	private ICountry country;
	private Coordinate location;


    public AType10(String line) throws PWCGException 
    {
        super();
        parse(line);
    }
    
    private void parse (String line) throws PWCGException 
	{
		id = getString(line, "AType:10 ID:", " TYPE:");
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
            coords.setXPos(new Double(locXStr));
            coords.setYPos(new Double(locYStr));
            coords.setZPos(new Double(locZStr));
            
            return coords;
        }
        catch (NumberFormatException e)
        {
            Logger.logException(e);
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
    public void write(BufferedWriter writer) throws PWCGIOException 
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
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
}

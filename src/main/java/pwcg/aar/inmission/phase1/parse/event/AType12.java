package pwcg.aar.inmission.phase1.parse.event;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

// T:15 AType:12 ID:302079 TYPE:He 111 H-6 COUNTRY:201 NAME:Obltn Heinkel Mann PID:-1 POS(64429.594,174.325,41093.000)
public class AType12 extends ATypeBase implements IAType12
{
    private String id = "";
    private String type = "";
    private String name;
    private ICountry country = CountryFactory.makeNeutralCountry();
    private String pid = "";

    public AType12(String line) throws PWCGException
    {    
        super(AType.ATYPE12);
        parse(line);
    }

    public AType12(String line, String id) throws PWCGException
    {    
        super(AType.ATYPE12);
        parse(line);
        this.id = id;
    }

    public AType12(
            String id, 
            String type, 
            String name, 
            ICountry country,
            String pid) throws PWCGException
    {    
        super(AType.ATYPE12);
        this.id = id;
        this.type = type;
        this.name = name;
        this.country = country;
        this.pid = pid;
    }

    private void parse (String line) throws PWCGException 
    {
        id = getString(line, "ATYPE:12 ID:", " TYPE:");
        type = getString(line, " TYPE:", " COUNTRY:");

        int countryCode = getInteger(line, "COUNTRY:", " NAME:");
        country = CountryFactory.makeCountryByCode(countryCode);

        name = getString(line, "NAME:", " PID:");
        if (name.startsWith("\u0001"))
            name = name.substring(1);
        
        if (line.contains(" POS("))
            pid = getString(line, "PID:", " POS(");
        else
            pid = getString(line, "PID:", null);
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getType()
    {
        return type;
    }

    @Override
    public ICountry getCountry()
    {
        return country;
    }

    @Override
    public String getPid()
    {
        return pid;
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            String format1 = "AType:12 ID:%d TYPE:%s COUNTRY:%d NAME:%s PID:-1";
            String format2 = "AType:12 ID:%d TYPE:Common Bot COUNTRY:%d NAME: PID:%d";

            int index = IndexGenerator.getInstance().getNextIndex();
            String atype1 = String.format(format1, index, type, country.getCountryCode(), name);
            writer.write(atype1);
            writer.newLine();

            // Associate a bot with the plane
            int index2 = IndexGenerator.getInstance().getNextIndex();
            String atype2 = String.format(format2, index2, country.getCountryCode(), index);
            writer.write(atype2);
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
}

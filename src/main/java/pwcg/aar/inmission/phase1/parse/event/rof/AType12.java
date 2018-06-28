package pwcg.aar.inmission.phase1.parse.event.rof;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.aar.inmission.phase1.parse.event.ATypeBase;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

// AType:12 ID:1488895 TYPE:MiG-3 ser.24 COUNTRY:101 NAME:Lt Anatoly Gorbunov PID:-1 POS(119648.406,152.195,44274.488)
public class AType12 extends ATypeBase implements IAType12
{
    private String id = "";
    private String type = "";
    private String name;
    private ICountry country = CountryFactory.makeNeutralCountry();
    private String pid = "";

    public AType12(String line) throws PWCGException
    {    
        super();
        parse(line);
    }
    
    public AType12()
    {
        super();
    }

    private void parse (String line) throws PWCGException 
    {
        id = getString(line, "ATYPE:12 ID:", " TYPE:");
        type = getString(line, " TYPE:", " COUNTRY:");

        int countryCode = getInteger(line, "COUNTRY:", " NAME:");
        country = CountryFactory.makeCountryByCode(countryCode);

        name = getString(line, "NAME:", " PID:");
        pid = getString(line, "PID:", null);
    }

    @Override
    public String getId()
    {
        return id;
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

    public String getName()
    {
        return name;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
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

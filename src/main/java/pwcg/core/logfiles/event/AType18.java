package pwcg.core.logfiles.event;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;


// T:66199 AType:18 BOTID:51199 PARENTID:50175 POS(75142.617,169.053,188263.078)
public class AType18 extends ATypeBase implements IAType18
{
    private String botId;
    private String vehicleId;
    private Coordinate location;

    public AType18(String line) throws PWCGException
    {
        super(AType.ATYPE18);
        parse(line);
    }

    private void parse (String line) throws PWCGException
    {
        botId = getId(line, "AType:18 BOTID:", " PARENTID:");
        vehicleId = getId(line, "PARENTID:", " POS(");
        location = findCoordinate(line, "POS(");
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            String format = "T:14605 AType:18 BOTID:%s PARENTID:%s POS(%.1f,%.1f,%.1f)";

            String atype = String.format(format, botId.split("@")[0], vehicleId.split("@")[0], location.getXPos(),location.getYPos(), location.getZPos());
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
    public String getBotId()
    {
        return botId;
    }

    @Override
    public String getVehicleId()
    {
        return vehicleId;
    }

    @Override
    public Coordinate getLocation()
    {
        return location;
    }
}

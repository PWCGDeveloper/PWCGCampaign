package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class McuSpawn extends BaseFlightMcu
{
    private int spawnAtMe = 1;

    public McuSpawn()
    {
        super();
    }

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            writer.write("MCU_Spawner");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            super.write(writer);

            writer.write("  SpawnAtMe  = " + spawnAtMe + ";");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public int getSpawnAtMe()
    {
        return spawnAtMe;
    }

    public void setSpawnAtMe(int spawnAtMe)
    {
        this.spawnAtMe = spawnAtMe;
    }
}

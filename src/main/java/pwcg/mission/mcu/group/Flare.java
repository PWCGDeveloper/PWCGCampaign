package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.mcu.McuFlare;
import pwcg.mission.mcu.McuTimer;

public class Flare
{
    private static final int FLARES_INTERVAL_SECONDS = 5;
    private static final int FLARE_ALTITUDE = 1000;

    private McuTimer flareTimer = new McuTimer();
    private McuFlare flare = new McuFlare();

    public Flare()
    {
    }

    public void createFlare(Coordinate position, int color, int flareVehicleId) throws PWCGException 
    {        
        createFlareTimer(position);
        createFlareMcu(position, color);
        createTargetAssociations();
        createObjectAssociations(flareVehicleId);
    }

    public void createFlareTimer(Coordinate position) throws PWCGException 
    {        
        flareTimer.setName("FlareTimer");
        flareTimer.setTime(FLARES_INTERVAL_SECONDS);
        flareTimer.setPosition(position.copy());
    }

    public void createFlareMcu(Coordinate position, int color) throws PWCGException 
    {        
        flare.setName("Flare");
        flare.setColor(color);
        Coordinate flarePosition = position.copy();
        flarePosition.setYPos(FLARE_ALTITUDE);
        flare.setPosition(flarePosition);
    }
    
    private void createTargetAssociations()
    {
        flareTimer.setTimerTarget(flare.getIndex());        
    }
    
    private void createObjectAssociations(int flareVehicleId)
    {
        flare.setObject(flareVehicleId);
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        flareTimer.write(writer);
        flare.write(writer);
    }
    
    public int getEntryPoint()
    {
        return flareTimer.getIndex();        
    }
    
    public void setNextTarget(int nextTarget)
    {
        flareTimer.setTimerTarget(nextTarget);        
    }
}

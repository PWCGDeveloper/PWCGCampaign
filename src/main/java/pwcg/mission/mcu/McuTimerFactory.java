package pwcg.mission.mcu;

import pwcg.core.location.Coordinate;

public class McuTimerFactory
{
    public static McuTimer buildStandardMcuTimer(String name, Coordinate position) 
    {
        return buildMcuTimer(name, position, 0.2);
    }
    
    public static McuTimer buildMcuTimer(String name, Coordinate position, double timeInSeconds) 
    {
        McuTimer timer = new McuTimer();
        timer.setPosition(position);
        timer.setName(name);
        timer.setDesc(name);
        timer.setTime(timeInSeconds);
        return timer;
    }
    
    public static McuCheckZone buildMcuCheckZone(String name, Coordinate position, int checkZoneTriggerDistanceMeters) 
    {
        McuCheckZone checkZone = new McuCheckZone(name);
        checkZone.setPosition(position);
        checkZone.setZone(checkZoneTriggerDistanceMeters);
        return checkZone;
    }
    
    public static McuDeactivate buildMcuDeactivate(String name, Coordinate position) 
    {
        McuDeactivate deactivate = new McuDeactivate();
        deactivate.setName(name);
        deactivate.setDesc(name);
        deactivate.setPosition(position);
        return deactivate;
    }
    
    
}

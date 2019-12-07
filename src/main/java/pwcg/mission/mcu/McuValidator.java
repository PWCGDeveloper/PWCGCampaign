package pwcg.mission.mcu;

public class McuValidator
{
    public static boolean hasTarget(BaseFlightMcu mcu, int expectedTarget)
    {
        for (String target : mcu.getTargets())
        {
            int actualTarget = new Integer(target);
            if (actualTarget == expectedTarget)
            {
                return true;
            }
        }
        return false;
    }
    
    public static int getNumTargets(BaseFlightMcu mcu)
    {
        return mcu.getTargets().size();
    }
    
    public static boolean hasObject(BaseFlightMcu mcu, int expectedObject)
    {
        for (String object : mcu.getObjects())
        {
            int actualObject = new Integer(object);
            if (actualObject == expectedObject)
            {
                return true;
            }
        }
        return false;
    }
    
    public static int getNumObjects(BaseFlightMcu mcu)
    {
        return mcu.getObjects().size();
    }
}

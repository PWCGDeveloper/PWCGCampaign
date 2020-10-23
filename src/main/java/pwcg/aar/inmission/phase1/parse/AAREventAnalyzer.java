package pwcg.aar.inmission.phase1.parse;

import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.inmission.phase1.parse.event.IAType3;

public class AAREventAnalyzer
{
    private AARLogEventData logEventData = new AARLogEventData();

    public AAREventAnalyzer(AARLogEventData logEventData)
    {
        this.logEventData = logEventData;
    }
    
    public void analyze()
    {
        for (IAType3 destroyed : logEventData.getDestroyedEvents())
        {
            String victim = getVehicleName(destroyed.getVictim());
            String victor = getVehicleName(destroyed.getVictor());
            System.out.println(victim + " killed by " + victor);
        }
    }
    
    private String getVehicleName(String id)
    {
        IAType12 vehicle = logEventData.getVehicle(id);
        if (vehicle != null)
        {
            return vehicle.getName() + " (" +  vehicle.getId() + ")";
        }
        else
        {
            return "Unknown";
        }
    }
}

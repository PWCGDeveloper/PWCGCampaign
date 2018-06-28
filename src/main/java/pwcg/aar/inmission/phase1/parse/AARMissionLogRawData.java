package pwcg.aar.inmission.phase1.parse;

public class AARMissionLogRawData
{
    private AARLogEventData logEventData = new AARLogEventData();
    

    public AARLogEventData getLogEventData()
    {
        return logEventData;
    }

    public void setLogEventData(AARLogEventData logEventData)
    {
        this.logEventData = logEventData;
    } 
}

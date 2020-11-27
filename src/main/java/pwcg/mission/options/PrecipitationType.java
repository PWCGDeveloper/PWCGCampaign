package pwcg.mission.options;

public enum PrecipitationType
{
    CLEAR(0),
    RAIN(1),
    SNOW(2);
    
    private int value = 0;
    
    private PrecipitationType (int value)
    {
        this.value = value;
    }
    
    public int getPrecipitationValue()
    {
        return value;
    }
}

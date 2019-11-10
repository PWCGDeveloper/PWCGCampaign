package pwcg.mission.mcu;

public enum Coalition
{
    COALITION_ALLIED(1),
    COALITION_AXIS(2),
    COALITION_ENTENTE(3),
    COALITION_CENTRAL(4);

    private int coalitionValue;

    private Coalition(int coalitionValue)
    {
        this.coalitionValue = coalitionValue;
    }

    public int getCoalitionValue() 
    {
        return coalitionValue;
    }
}

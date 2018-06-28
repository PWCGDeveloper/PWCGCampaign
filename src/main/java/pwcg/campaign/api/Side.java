package pwcg.campaign.api;

public enum Side
{
    ALLIED,
    AXIS,
    NEUTRAL;
    
    /**
     * @param side
     * @return
     */
    public Side getOppositeSide()
    {
        if (this == ALLIED)
        {
            return AXIS;
        }
        else if (this == AXIS)
        {
            return ALLIED;
        }
        else
        {
            return NEUTRAL;
        }
    }
}

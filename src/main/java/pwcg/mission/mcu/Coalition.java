package pwcg.mission.mcu;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;

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
    
    public static List<Coalition> getCoalitions()
    {
        List<Coalition> coalitions = new ArrayList<>();
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            coalitions.add(COALITION_ALLIED);
            coalitions.add(COALITION_AXIS);
        }
        else
        {
            coalitions.add(COALITION_ENTENTE);
            coalitions.add(COALITION_CENTRAL);
        }
        return coalitions;
    }

    public static List<Coalition> getCoalitionsForSide(Side side)
    {
        List<Coalition> coalitions = new ArrayList<>();
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            if (side == Side.ALLIED)
            {
                coalitions.add(COALITION_ALLIED);
            }
            else
            {
                coalitions.add(COALITION_AXIS);
            }
        }
        else
        {
            if (side == Side.ALLIED)
            {
                coalitions.add(COALITION_ENTENTE);
            }
            else
            {
                coalitions.add(COALITION_CENTRAL);
            }
        }
        return coalitions;
    }
}

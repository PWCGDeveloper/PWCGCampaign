package pwcg.campaign.tank.payload;

import java.util.ArrayList;
import java.util.List;

public class TankPayloadDesignation
{
    private int payloadId = 0;
    private String modMask = "1";
    private List<TankPayloadElement> payloadElements = new ArrayList<>();

    public int getPayloadId()
    {
        return payloadId;
    }

    public void setPayloadId(int payloadId)
    {
        this.payloadId = payloadId;
    }

    public String getModMask()
    {
        return modMask;
    }

    public void setModMask(String modMask)
    {
        this.modMask = modMask;
    }

    public List<TankPayloadElement> getPayloadElements()
    {
        return payloadElements;
    }

    public void setPayloadElements(List<TankPayloadElement> payloadElements)
    {
        this.payloadElements = payloadElements;
    }

    public String getPayloadDescription()
    {
        String payloadDescription = "";
        for (TankPayloadElement payloadElement : payloadElements)
        {
            if (!payloadDescription.isEmpty())
            {
                payloadDescription += ", ";
            }
            payloadDescription += payloadElement.getDescription();
        }
        return payloadDescription;
    }

    public boolean containsElement(TankPayloadElement searchPayloadElement)
    {
        for (TankPayloadElement payloadElement : payloadElements)
        {
            if (payloadElement == searchPayloadElement)
            {
                return true;
            }
        }
        return false;
    }
}

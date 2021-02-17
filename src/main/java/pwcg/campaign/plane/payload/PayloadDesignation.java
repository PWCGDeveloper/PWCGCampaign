package pwcg.campaign.plane.payload;

import java.util.ArrayList;
import java.util.List;

public class PayloadDesignation
{
    private int payloadId = 0;
    private String modMask = "1";
    private List<PayloadElement> payloadElements = new ArrayList<>();

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

    public List<PayloadElement> getPayloadElements()
    {
        return payloadElements;
    }

    public void setPayloadElements(List<PayloadElement> payloadElements)
    {
        this.payloadElements = payloadElements;
    }

    public String getPayloadDescription()
    {
        String payloadDescription = "";
        for (PayloadElement payloadElement : payloadElements)
        {
            if (!payloadDescription.isEmpty())
            {
                payloadDescription += ", ";
            }
            payloadDescription += payloadElement.getDescription();
        }
        return payloadDescription;
    }

    public boolean containsElement(PayloadElement searchPayloadElement)
    {
        for (PayloadElement payloadElement : payloadElements)
        {
            if (payloadElement == searchPayloadElement)
            {
                return true;
            }
        }
        return false;
    }
}

package pwcg.campaign.plane.payload;

import java.util.ArrayList;
import java.util.List;

public class PlanePayloadDesignation
{
    private int payloadId = 0;
    private String modMask = "1";
    private List<PlanePayloadElement> payloadElements = new ArrayList<>();

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

    public List<PlanePayloadElement> getPayloadElements()
    {
        return payloadElements;
    }

    public void setPayloadElements(List<PlanePayloadElement> payloadElements)
    {
        this.payloadElements = payloadElements;
    }

    public String getPayloadDescription()
    {
        String payloadDescription = "";
        for (PlanePayloadElement payloadElement : payloadElements)
        {
            if (!payloadDescription.isEmpty())
            {
                payloadDescription += ", ";
            }
            payloadDescription += payloadElement.getDescription();
        }
        return payloadDescription;
    }

    public boolean containsElement(PlanePayloadElement searchPayloadElement)
    {
        for (PlanePayloadElement payloadElement : payloadElements)
        {
            if (payloadElement == searchPayloadElement)
            {
                return true;
            }
        }
        return false;
    }
}

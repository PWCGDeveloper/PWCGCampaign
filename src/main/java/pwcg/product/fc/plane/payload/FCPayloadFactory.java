package pwcg.product.fc.plane.payload;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.plane.FCPlaneAttributeFactory;
import pwcg.product.fc.plane.FCPlaneAttributeMapping;

public class FCPayloadFactory implements IPayloadFactory
{
	public IPlanePayload createPlanePayload(String planeTypeName) throws PWCGException 
	{
		PlaneType planeType = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeName);
		FCPlaneAttributeMapping attributeMapping = FCPlaneAttributeFactory.createPlaneAttributeMap(planeTypeName);
	    
        if (attributeMapping == FCPlaneAttributeMapping.ALBATROSD5)
        {
            return new AlbatrosD5Payload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.PFALZD3A)
        {
            return new PfalzD3Payload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.FOKKERDR1)
        {
            return new FokkerDRIPayload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.FOKKERD7)
        {
            return new FokkerD7Payload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.FOKKERD7F)
        {
            return new FokkerD7FPayload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.HALBERSTADTCLII)
        {
            return new Halberstadtcl2Payload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.HALBERSTADTCLIIAU)
        {
            return new Halberstadtcl2auPayload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.SE5A)
        {
            return new Se5aPayload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.CAMEL)
        {
            return new CamelPayload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.DOLPHIN)
        {
            return new DolphinPayload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.SPAD13)
        {
            return new Spad13Payload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.BRISTOLF2BF2)
        {
            return new BristolF2B2Payload(planeType);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.BRISTOLF2BF3)
        {
            return new BristolF2B3Payload(planeType);
        }

        throw new PWCGException ("No payload for plane " + planeTypeName);
	}

    @Override
    public PayloadDesignation getPlanePayloadDesignation(String planeTypeName, int selectedPrimaryPayloadId) throws PWCGException
    {
        IPlanePayload planePayload = createPlanePayload(planeTypeName);
        planePayload.setSelectedPayloadId(selectedPrimaryPayloadId);
        return planePayload.getSelectedPayloadDesignation();
    }
}

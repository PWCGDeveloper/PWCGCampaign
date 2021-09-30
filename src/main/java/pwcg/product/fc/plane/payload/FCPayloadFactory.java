package pwcg.product.fc.plane.payload;

import java.util.Date;

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
	public IPlanePayload createPlanePayload(String planeTypeName, Date date) throws PWCGException 
	{
		PlaneType planeType = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeName);
		FCPlaneAttributeMapping attributeMapping = FCPlaneAttributeFactory.createPlaneAttributeMap(planeTypeName);
	    
        if (attributeMapping == FCPlaneAttributeMapping.ALBATROSD5)
        {
            return new AlbatrosD5Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.PFALZD3A)
        {
            return new PfalzD3Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.PFALZD12)
        {
            return new PfalzD12Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.FOKKERDR1)
        {
            return new FokkerDRIPayload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.FOKKERD7)
        {
            return new FokkerD7Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.FOKKERD7F)
        {
            return new FokkerD7FPayload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.FOKKERD8)
        {
            return new FokkerD8Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.HALBERSTADTCLII)
        {
            return new Halberstadtcl2Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.HALBERSTADTCLIIAU)
        {
            return new Halberstadtcl2auPayload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.SE5A)
        {
            return new Se5aPayload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.CAMEL)
        {
            return new CamelPayload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.DOLPHIN)
        {
            return new DolphinPayload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.NIEUPORT28)
        {
            return new Nieuport28Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.SPAD7EARLY)
        {
            return new Spad7EarlyPayload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.SPAD7LATE)
        {
            return new Spad7LatePayload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.SPAD13)
        {
            return new Spad13Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.DH4)
        {
            return new AircoDH4Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.BRISTOLF2BF2)
        {
            return new BristolF2B2Payload(planeType, date);
        }
        else if (attributeMapping == FCPlaneAttributeMapping.BRISTOLF2BF3)
        {
            return new BristolF2B3Payload(planeType, date);
        }

        throw new PWCGException ("No payload for plane " + planeTypeName);
	}

    @Override
    public PayloadDesignation getPlanePayloadDesignation(String planeTypeName, int selectedPrimaryPayloadId, Date date) throws PWCGException
    {
        IPlanePayload planePayload = createPlanePayload(planeTypeName, date);
        planePayload.setSelectedPayloadId(selectedPrimaryPayloadId);
        return planePayload.getSelectedPayloadDesignation();
    }
}

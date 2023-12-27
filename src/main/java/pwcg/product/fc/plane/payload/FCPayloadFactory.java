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
		if (!planeType.getSubstituteType().isEmpty())
		{
		    planeTypeName = planeType.getSubstituteType();
		}

		
		FCPlaneAttributeMapping attributeMapping = FCPlaneAttributeFactory.createPlaneAttributeMap(planeTypeName);
		
		switch (attributeMapping) 
		{
        case ALBATROSD2:
            return new AlbatrosD2Payload(planeType, date);
        case ALBATROSD5:
            return new AlbatrosD5Payload(planeType, date);
        case PFALZD3A:
            return new PfalzD3Payload(planeType, date);
        case PFALZD12:
            return new PfalzD12Payload(planeType, date);
        case FOKKERDR1:
            return new FokkerDRIPayload(planeType, date);
        case FOKKERD7:
            return new FokkerD7Payload(planeType, date);
        case FOKKERD7F:
            return new FokkerD7FPayload(planeType, date);
        case FOKKERD8:
            return new FokkerD8Payload(planeType, date);
        case SSWD4:
            return new SSWD4Payload(planeType, date);
        case DFWCV:
            return new DFWCVPayload(planeType, date);
        case GOTHAGV:
            return new GothaGVPayload(planeType, date);
        case HALBERSTADTD2:
            return new HalberstadtD2Payload(planeType, date);
        case HALBERSTADTCLII:
            return new Halberstadtcl2Payload(planeType, date);
        case HALBERSTADTCLIIAU:
            return new Halberstadtcl2auPayload(planeType, date);
 
        case SE5A:
            return new Se5aPayload(planeType, date);
        case CAMEL:
            return new SopwithCamelPayload(planeType, date);
        case SNIPE:
            return new SopwithSnipePayload(planeType, date);
        case TRIPE:
            return new SopwithTriplanePayload(planeType, date);
        case DOLPHIN:
            return new DolphinPayload(planeType, date);
        case FE2B:
            return new FE2BPayload(planeType, date);
        case RE8:
            return new RE8Payload(planeType, date);
        case DH4:
            return new AircoDH4Payload(planeType, date);
        case BRISTOLF2BF2:
            return new BristolF2B2Payload(planeType, date);
        case BRISTOLF2BF3:
            return new BristolF2B3Payload(planeType, date);
        case HANDLEYPAGE400:
            return new HandleyPage400Payload(planeType, date);

        case NIEUPORT11:
            return new Nieuport11Payload(planeType, date);
        case NIEUPORT17:
            return new Nieuport17Payload(planeType, date);
        case NIEUPORT17GBR:
            return new Nieuport17GBRPayload(planeType, date);
        case NIEUPORT28:
            return new Nieuport28Payload(planeType, date);
        case HANRIOTHD1:
            return new HanriotHD1Payload(planeType, date);
        case SPAD7EARLY:
            return new Spad7EarlyPayload(planeType, date);
        case SPAD7LATE:
            return new Spad7LatePayload(planeType, date);
        case SPAD13:
            return new Spad13Payload(planeType, date);
        case BREGUET14:
            return new Breguet14Payload(planeType, date);
        default:
            throw new PWCGException ("No payload for plane " + planeTypeName);
        }

	}

    @Override
    public PayloadDesignation getPlanePayloadDesignation(String planeTypeName, int selectedPayloadId, Date date) throws PWCGException
    {
        IPlanePayload planePayload = createPlanePayload(planeTypeName, date);
        planePayload.setSelectedPayloadId(selectedPayloadId);
        return planePayload.getSelectedPayloadDesignation();
    }
}

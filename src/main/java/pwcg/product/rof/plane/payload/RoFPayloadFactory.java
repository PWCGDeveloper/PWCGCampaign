package pwcg.product.rof.plane.payload;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.product.rof.plane.RoFPlaneAttributeFactory;
import pwcg.product.rof.plane.RoFPlaneAttributeMapping;
import pwcg.product.rof.plane.payload.aircraft.Breguet14Payload;
import pwcg.product.rof.plane.payload.aircraft.BristolF2BPayload;
import pwcg.product.rof.plane.payload.aircraft.DFWCVPayload;
import pwcg.product.rof.plane.payload.aircraft.DH4Payload;
import pwcg.product.rof.plane.payload.aircraft.Fe2bPayload;
import pwcg.product.rof.plane.payload.aircraft.FelixstowePayload;
import pwcg.product.rof.plane.payload.aircraft.GothaGVPayload;
import pwcg.product.rof.plane.payload.aircraft.HBW12Payload;
import pwcg.product.rof.plane.payload.aircraft.HP400Payload;
import pwcg.product.rof.plane.payload.aircraft.HalberstadtCLIIPayload;
import pwcg.product.rof.plane.payload.aircraft.Nieuport11Payload;
import pwcg.product.rof.plane.payload.aircraft.Nieuport17BritPayload;
import pwcg.product.rof.plane.payload.aircraft.Nieuport17Payload;
import pwcg.product.rof.plane.payload.aircraft.RE8Payload;
import pwcg.product.rof.plane.payload.aircraft.RolandCIIPayload;
import pwcg.product.rof.plane.payload.aircraft.SPADXIIIPayload;
import pwcg.product.rof.plane.payload.aircraft.Sikorsky22Payload;
import pwcg.product.rof.plane.payload.aircraft.SopStrutterBPayload;
import pwcg.product.rof.plane.payload.aircraft.SopStrutterPayload;
import pwcg.product.rof.plane.payload.aircraft.Spad7Payload;
import pwcg.product.rof.plane.payload.aircraft.StandardBritishScoutPayload;
import pwcg.product.rof.plane.payload.aircraft.StandardScoutPayload;
import pwcg.core.exception.PWCGException;

public class RoFPayloadFactory implements IPayloadFactory
{
	public IPlanePayload createPlanePayload(String planeTypeName) throws PWCGException 
	{
		PlaneType planeType = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeName);
		RoFPlaneAttributeMapping attributeMapping = RoFPlaneAttributeFactory.createPlaneAttributeMap(planeTypeName);
	    
        // German scout
        if (attributeMapping == RoFPlaneAttributeMapping.FOKKERE3)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.ALBATROSD2)
        {
            return new StandardScoutPayload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.ALBATROSD2LATE)
        {
            return new StandardScoutPayload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.ALBATROSD3)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.ALBATROSD5)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.PFALZD3A)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.PFALZD12)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.FOKKERDR1)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.FOKKERD7)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.FOKKERD7F)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.FOKKERD8)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.HALBERSTADTDII)
        {
            return new StandardScoutPayload(planeType);
        }
        
        // German multi place
        if (attributeMapping == RoFPlaneAttributeMapping.DFWC5)
        {
            return new DFWCVPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.HALBERSTADTCLII)
        {
            return new HalberstadtCLIIPayload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.HALBERSTADTCLIIAU)
        {
            return new HalberstadtCLIIPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.ROLANDCII)
        {
            return new RolandCIIPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.GOTHAGV)
        {
            return new GothaGVPayload(planeType);
        }
        
        // German sea planes
        if (attributeMapping == RoFPlaneAttributeMapping.HBW12)
        {
            return new HBW12Payload(planeType);
        }
        
        // French scouts
        if (attributeMapping == RoFPlaneAttributeMapping.NIEUPORT11)
        {
            return new Nieuport11Payload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.NIEUPORT17)
        {
            return new Nieuport17Payload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.NIEUPORT17LEWIS)
        {
            return new Nieuport17BritPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.NIEUPORT17RUSSIAN)
        {
            return new Nieuport17Payload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.NIEUPORT28)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.HANRIOTHD1)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.HANRIOTHD2)
        {
            return new StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.SPAD7EARLY)
        {
            return new Spad7Payload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.SPAD7)
        {
            return new Spad7Payload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.SPAD13)
        {
            return new SPADXIIIPayload(planeType);
        }

        // French multi place
        if (attributeMapping == RoFPlaneAttributeMapping.BREGUET14)
        {
            return new Breguet14Payload(planeType);
        }
        
        // British scouts
        if (attributeMapping == RoFPlaneAttributeMapping.DH2)
        {
            return new  StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.PUP)
        {
            return new  StandardScoutPayload(planeType);
        }
        if (attributeMapping == RoFPlaneAttributeMapping.TRIPE)
        {
            return new  StandardScoutPayload(planeType);
        }
        
        // Later British planes carried bombs
        if (attributeMapping == RoFPlaneAttributeMapping.SE5A)
        {
            return new  StandardBritishScoutPayload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.DOLPHIN)
        {
            return new  StandardBritishScoutPayload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.CAMEL)
        {
            return new  StandardBritishScoutPayload(planeType);            
        }
        
        // British multi place
        if (attributeMapping == RoFPlaneAttributeMapping.BRISTOLF2BF2)
        {
            return new  BristolF2BPayload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.BRISTOLF2BF3)
        {
            return new  BristolF2BPayload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.HP400)
        {
            return new HP400Payload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.FE2B)
        {
            return new Fe2bPayload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.RE8)
        {
            return new RE8Payload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.DH4)
        {
            return new DH4Payload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.SOPSTR)
        {
            return new SopStrutterPayload(planeType);            
        }
        if (attributeMapping == RoFPlaneAttributeMapping.SOPSTRB)
        {
            return new SopStrutterBPayload(planeType);            
        }
        
        // British sea planes
        if (attributeMapping == RoFPlaneAttributeMapping.FELIXSTOWEF2A)
        {
            return new FelixstowePayload(planeType);            
        }
        
        // Russian scout
        if (attributeMapping == RoFPlaneAttributeMapping.S16)
        {
            return new  StandardScoutPayload(planeType);
        }
        
        // Russian multi place
        if (attributeMapping == RoFPlaneAttributeMapping.S22)
        {
            return new Sikorsky22Payload(planeType);            
        }
        
        throw new PWCGException ("No payload for type " + planeTypeName);
	}


    @Override
    public PayloadDesignation getPlanePayloadDesignation(String planeTypeName, int selectedPrimaryPayloadId) throws PWCGException
    {
        IPlanePayload planePayload = createPlanePayload(planeTypeName);
        planePayload.setSelectedPayloadId(selectedPrimaryPayloadId);
        return planePayload.getSelectedPayloadDesignation();
    }
}

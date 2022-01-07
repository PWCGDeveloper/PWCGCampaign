package pwcg.mission.flight.plane;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneAttributeFactory;
import pwcg.campaign.plane.PlaneAttributeMapping;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadDesignation;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.payload.aircraft.A20BPayload;
import pwcg.mission.flight.plane.payload.aircraft.B25DRAFPayload;
import pwcg.mission.flight.plane.payload.aircraft.Bf109E7Payload;
import pwcg.mission.flight.plane.payload.aircraft.Bf109F2Payload;
import pwcg.mission.flight.plane.payload.aircraft.Bf109F4Payload;
import pwcg.mission.flight.plane.payload.aircraft.Bf109G14Payload;
import pwcg.mission.flight.plane.payload.aircraft.Bf109G2Payload;
import pwcg.mission.flight.plane.payload.aircraft.Bf109G4Payload;
import pwcg.mission.flight.plane.payload.aircraft.Bf109G6LatePayload;
import pwcg.mission.flight.plane.payload.aircraft.Bf109G6Payload;
import pwcg.mission.flight.plane.payload.aircraft.Bf109K4Payload;
import pwcg.mission.flight.plane.payload.aircraft.Bf110E2Payload;
import pwcg.mission.flight.plane.payload.aircraft.Bf110G2Payload;
import pwcg.mission.flight.plane.payload.aircraft.C47Payload;
import pwcg.mission.flight.plane.payload.aircraft.Fw190A3Payload;
import pwcg.mission.flight.plane.payload.aircraft.Fw190A5Payload;
import pwcg.mission.flight.plane.payload.aircraft.Fw190A6Payload;
import pwcg.mission.flight.plane.payload.aircraft.Fw190A8Payload;
import pwcg.mission.flight.plane.payload.aircraft.Fw190D9Payload;
import pwcg.mission.flight.plane.payload.aircraft.He111H16Payload;
import pwcg.mission.flight.plane.payload.aircraft.He111H6Payload;
import pwcg.mission.flight.plane.payload.aircraft.Hs129B2Payload;
import pwcg.mission.flight.plane.payload.aircraft.HurricaneMkIIPayload;
import pwcg.mission.flight.plane.payload.aircraft.I16Type24Payload;
import pwcg.mission.flight.plane.payload.aircraft.IL2M41Payload;
import pwcg.mission.flight.plane.payload.aircraft.IL2M42Payload;
import pwcg.mission.flight.plane.payload.aircraft.IL2M43Payload;
import pwcg.mission.flight.plane.payload.aircraft.Ju52Payload;
import pwcg.mission.flight.plane.payload.aircraft.Ju87D3Payload;
import pwcg.mission.flight.plane.payload.aircraft.Ju88A4Payload;
import pwcg.mission.flight.plane.payload.aircraft.La5FNS2Payload;
import pwcg.mission.flight.plane.payload.aircraft.La5S8Payload;
import pwcg.mission.flight.plane.payload.aircraft.Lagg3S29Payload;
import pwcg.mission.flight.plane.payload.aircraft.Ma202Ser8Payload;
import pwcg.mission.flight.plane.payload.aircraft.Me262APayload;
import pwcg.mission.flight.plane.payload.aircraft.MiG3Ser24Payload;
import pwcg.mission.flight.plane.payload.aircraft.P38J25Payload;
import pwcg.mission.flight.plane.payload.aircraft.P39L1Payload;
import pwcg.mission.flight.plane.payload.aircraft.P40E1Payload;
import pwcg.mission.flight.plane.payload.aircraft.P47D22Payload;
import pwcg.mission.flight.plane.payload.aircraft.P47D28Payload;
import pwcg.mission.flight.plane.payload.aircraft.P51B5Payload;
import pwcg.mission.flight.plane.payload.aircraft.P51D15Payload;
import pwcg.mission.flight.plane.payload.aircraft.Pe2S35Payload;
import pwcg.mission.flight.plane.payload.aircraft.Pe2S87Payload;
import pwcg.mission.flight.plane.payload.aircraft.SpitfireMkIXePayload;
import pwcg.mission.flight.plane.payload.aircraft.SpitfireMkVbPayload;
import pwcg.mission.flight.plane.payload.aircraft.SpitfireMkXIVPayload;
import pwcg.mission.flight.plane.payload.aircraft.TempestMKVS2Payload;
import pwcg.mission.flight.plane.payload.aircraft.TyphoonMkIbPayload;
import pwcg.mission.flight.plane.payload.aircraft.U2VSPayload;
import pwcg.mission.flight.plane.payload.aircraft.Yak1S127Payload;
import pwcg.mission.flight.plane.payload.aircraft.Yak1S69Payload;
import pwcg.mission.flight.plane.payload.aircraft.Yak7BS36Payload;
import pwcg.mission.flight.plane.payload.aircraft.Yak9S1Payload;
import pwcg.mission.flight.plane.payload.aircraft.Yak9TS1Payload;

public class PlanePayloadFactory
{
	public IPlanePayload createPayload(String planeTypeName, Date date) throws PWCGException 
	{
		PlaneType planeType = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeName);
		PlaneAttributeMapping attributeMapping = PlaneAttributeFactory.createPlaneAttributeMap(planeTypeName);
	    
        if (attributeMapping == PlaneAttributeMapping.BF109_E7)
        {
            return new Bf109E7Payload(planeType, date);
        }
        if (attributeMapping == PlaneAttributeMapping.BF109_F2)
        {
            return new Bf109F2Payload(planeType, date);
        }
        if (attributeMapping == PlaneAttributeMapping.BF109_F4)
        {
            return new Bf109F4Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.BF109_G2)
        {
            return new Bf109G2Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.BF109_G4)
        {
            return new Bf109G4Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.BF109_G6)
        {
            return new Bf109G6Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.BF109_G6_LATE)
        {
            return new Bf109G6LatePayload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.BF109_G14)
        {
            return new Bf109G14Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.BF109_K4)
        {
            return new Bf109K4Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.BF110_E2)
        {
            return new Bf110E2Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.BF110_G2)
        {
            return new Bf110G2Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.FW190_A3)
        {
            return new Fw190A3Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.FW190_A5)
        {
            return new Fw190A5Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.FW190_A6)
        {
            return new Fw190A6Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.FW190_A8)
        {
            return new Fw190A8Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.FW190_D9)
        {
            return new Fw190D9Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.HE111_H6)
        {
            return new He111H6Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.HE111_H16)
        {
            return new He111H16Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.HS129_B2S)
        {
            return new Hs129B2Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.JU52)
        {
            return new Ju52Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.JU87_D3)
        {
            return new Ju87D3Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.JU88_A4)
        {
            return new Ju88A4Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.Ma202_SER8)
        {
            return new Ma202Ser8Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.ME262_A)
        {
            return new Me262APayload(planeType, date);
        }

        // Russian
        else if (attributeMapping == PlaneAttributeMapping.U2_VS)
        {
            return new U2VSPayload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.I16_T24)
        {
            return new I16Type24Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.IL2_M41)
        {
            return new IL2M41Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.IL2_M42)
        {
            return new IL2M42Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.IL2_M43)
        {
            return new IL2M43Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.LAGG3_S29)
        {
            return new Lagg3S29Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.LA5_S8)
        {
            return new La5S8Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.LA5N_S2)
        {
            return new La5FNS2Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.MIG3_S24)
        {
            return new MiG3Ser24Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.P38_J25)
        {
            return new P38J25Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.P39_L1)
        {
            return new P39L1Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.P40_E1)
        {
            return new P40E1Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.P47_D22)
        {
            return new P47D22Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.P47_D28)
        {
            return new P47D28Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.P51_B5)
        {
            return new P51B5Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.P51_D15)
        {
            return new P51D15Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.PE2_S35)
        {
            return new Pe2S35Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.PE2_S87)
        {
            return new Pe2S87Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.A20B)
        {
            return new A20BPayload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.B25)
        {
            return new B25DRAFPayload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.C47)
        {
            return new C47Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.HURRICANE_MKII)
        {
            return new HurricaneMkIIPayload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.SPITFIRE_MKVB)
        {
            return new SpitfireMkVbPayload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.SPITFIRE_MKIXE)
        {
            return new SpitfireMkIXePayload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.SPITFIRE_MKXIV)
        {
            return new SpitfireMkXIVPayload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.TEMPEST_MKVS2)
        {
            return new TempestMKVS2Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.TYPHOON_MKIB)
        {
            return new TyphoonMkIbPayload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.YAK1_S69)
        {
            return new Yak1S69Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.YAK1_S127)
        {
            return new Yak1S127Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.YAK7B_S36)
        {
            return new Yak7BS36Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.YAK9_S1)
        {
            return new Yak9S1Payload(planeType, date);
        }
        else if (attributeMapping == PlaneAttributeMapping.YAK9T_S1)
        {
            return new Yak9TS1Payload(planeType, date);
        }

        throw new PWCGException ("No payload for plane " + planeTypeName);
	}

    public PlanePayloadDesignation getPlanePayloadDesignation(String planeTypeName, int selectedPrimaryPayloadId, Date date) throws PWCGException
    {
        IPlanePayload planePayload = createPayload(planeTypeName, date);
        planePayload.setSelectedPayloadId(selectedPrimaryPayloadId);
        return planePayload.getSelectedPayloadDesignation();
    }
}

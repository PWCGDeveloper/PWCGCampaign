package pwcg.campaign.tank.payload;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankAttributeFactory;
import pwcg.campaign.tank.TankAttributeMapping;
import pwcg.campaign.tank.TankType;
import pwcg.campaign.tank.TankTypeFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.payload.tank.GAZAAA;
import pwcg.mission.playerunit.payload.tank.KV1S;
import pwcg.mission.playerunit.payload.tank.M4A2;
import pwcg.mission.playerunit.payload.tank.PzkwIIIL;
import pwcg.mission.playerunit.payload.tank.PzkwIIIM;
import pwcg.mission.playerunit.payload.tank.PzkwIVG;
import pwcg.mission.playerunit.payload.tank.PzkwVD;
import pwcg.mission.playerunit.payload.tank.PzkwVIH;
import pwcg.mission.playerunit.payload.tank.Sdkfz10;
import pwcg.mission.playerunit.payload.tank.Sdkfz184;
import pwcg.mission.playerunit.payload.tank.Su122;
import pwcg.mission.playerunit.payload.tank.Su152;
import pwcg.mission.playerunit.payload.tank.T34Early;
import pwcg.mission.playerunit.payload.tank.T34Late;

public class TankPayloadFactory
{
	public ITankPayload createPayload(String tankeTypeName, Date date) throws PWCGException 
	{
	    TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
		TankType tankType = tankTypeFactory.createTankTypeByAnyName(tankeTypeName);
		TankAttributeMapping attributeMapping = TankAttributeFactory.createTankAttributeMap(tankeTypeName);
	    
        if (attributeMapping == TankAttributeMapping.PZKW_III_L)
        {
            return new PzkwIIIL(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.PZKW_III_M)
        {
            return new PzkwIIIM(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.PZKW_III_M)
        {
            return new PzkwIVG(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.PANTHER_D)
        {
            return new PzkwVD(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.TIGER_I)
        {
            return new PzkwVIH(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.SDKFZ_10_AAA)
        {
            return new Sdkfz10(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.ELEFANT)
        {
            return new Sdkfz184(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.T34_EARLY)
        {
            return new T34Early(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.T34_LATE)
        {
            return new T34Late(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.KV1_S)
        {
            return new KV1S(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.SU122)
        {
            return new Su122(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.SU152)
        {
            return new Su152(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.GAZ_AAA)
        {
            return new GAZAAA(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.SHERMAN_M4A2)
        {
            return new M4A2(tankType, date);
        }
 
        throw new PWCGException ("No payload for tank " + tankeTypeName);
	}

    public TankPayloadDesignation getTankPayloadDesignation(String tankTypeName, int selectedPrimaryPayloadId, Date date) throws PWCGException
    {
        ITankPayload payload = createPayload(tankTypeName, date);
        payload.setSelectedPayloadId(selectedPrimaryPayloadId);
        return payload.getSelectedPayloadDesignation();
    }
}

package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayloadDesignation;
import pwcg.campaign.tank.payload.TankPayloadFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.crew.CrewTankPayloadPairing;

public class BriefingTankModificationsFilter
{
    private CrewTankPayloadPairing crewPlane;
    
    public BriefingTankModificationsFilter(CrewTankPayloadPairing crewPlane)
    {
        this.crewPlane = crewPlane;
    }


    public List<String> selectModificationsForPlane(Date date) throws PWCGException 
    {
        List<String> tankModifications = new ArrayList<>();

        TankPayloadFactory payloadfactory = new TankPayloadFactory();
        ITankPayload payload = payloadfactory.createPayload(crewPlane.getTank().getType(), date);
        
        for (TankPayloadDesignation payloadDesignation : payload.getOptionalPayloadModifications())
        {
            tankModifications.add(payloadDesignation.getPayloadDescription());
        }
        return tankModifications;
    }
}

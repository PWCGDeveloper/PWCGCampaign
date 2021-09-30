package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingPlaneModificationsFilter
{
    private CrewPlanePayloadPairing crewPlane;
    
    public BriefingPlaneModificationsFilter(CrewPlanePayloadPairing crewPlane)
    {
        this.crewPlane = crewPlane;
    }


    public List<String> selectModificationsForPlane(Date date) throws PWCGException 
    {
        List<String> planeModifications = new ArrayList<>();

        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(crewPlane.getPlane().getType(), date);
        
        for (PayloadDesignation payloadDesignation : payload.getOptionalPayloadModifications())
        {
            planeModifications.add(payloadDesignation.getPayloadDescription());
        }
        return planeModifications;
    }
}

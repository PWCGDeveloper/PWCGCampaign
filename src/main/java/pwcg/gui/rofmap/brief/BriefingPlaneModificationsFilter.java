package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.plane.payload.PayloadElementCategory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingPlaneModificationsFilter
{
    private CrewPlanePayloadPairing crewPlane;
    
    public BriefingPlaneModificationsFilter(CrewPlanePayloadPairing crewPlane)
    {
        this.crewPlane = crewPlane;
    }


    public List<String> selectModificationsForPlane() throws PWCGException 
    {
        List<String> planeModifications = new ArrayList<>();

        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(crewPlane.getPlane().getType());
        
        for (PayloadDesignation payloadDesignation : payload.getPayloadDesignations())
        {
            if (payloadDesignation.getPayloadElements().get(0).getCategory() == PayloadElementCategory.MODIFICATION)
            {
                planeModifications.add(payloadDesignation.getPayloadDescription());
            }
        }
        return planeModifications;
    }
}

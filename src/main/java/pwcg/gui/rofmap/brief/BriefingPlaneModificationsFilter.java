package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPlanePayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadDesignation;
import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.crew.CrewVehiclePayloadPairing;

public class BriefingPlaneModificationsFilter
{
    private CrewVehiclePayloadPairing crewPlane;
    
    public BriefingPlaneModificationsFilter(CrewVehiclePayloadPairing crewPlane)
    {
        this.crewPlane = crewPlane;
    }


    public List<String> selectModificationsForPlane(Date date) throws PWCGException 
    {
        List<String> planeModifications = new ArrayList<>();

        IPlanePayloadFactory payloadfactory = PWCGContext.getInstance().getPlanePayloadFactory();
        IPlanePayload payload = payloadfactory.createPayload(crewPlane.getPlane().getType(), date);
        
        for (PlanePayloadDesignation payloadDesignation : payload.getOptionalPayloadModifications())
        {
            planeModifications.add(payloadDesignation.getPayloadDescription());
        }
        return planeModifications;
    }
}

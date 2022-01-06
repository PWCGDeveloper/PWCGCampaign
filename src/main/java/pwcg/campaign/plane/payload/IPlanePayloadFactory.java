package pwcg.campaign.plane.payload;

import java.util.Date;

import pwcg.core.exception.PWCGException;

public interface IPlanePayloadFactory 
{
    public IPlanePayload createPayload(String planeTypeName, Date date) throws PWCGException;
    PlanePayloadDesignation getPlanePayloadDesignation(String planeTypeName, int selectedPrimaryPayloadId, Date date) throws PWCGException;
}

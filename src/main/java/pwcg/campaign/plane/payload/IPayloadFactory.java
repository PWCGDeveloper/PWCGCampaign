package pwcg.campaign.plane.payload;

import pwcg.core.exception.PWCGException;

public interface IPayloadFactory 
{
    public IPlanePayload createPlanePayload(String planeTypeName) throws PWCGException;
    public PayloadDesignation getPlanePayloadDesignation(String planeTypeName, int payloadId) throws PWCGException;
}

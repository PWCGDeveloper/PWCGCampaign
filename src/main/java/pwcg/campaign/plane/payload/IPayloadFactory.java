package pwcg.campaign.plane.payload;

import java.util.Date;

import pwcg.core.exception.PWCGException;

public interface IPayloadFactory 
{
    public IPlanePayload createPlanePayload(String planeTypeName, Date date) throws PWCGException;
    PayloadDesignation getPlanePayloadDesignation(String planeTypeName, int selectedPrimaryPayloadId, Date date) throws PWCGException;
}

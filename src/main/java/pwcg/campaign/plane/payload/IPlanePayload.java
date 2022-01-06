package pwcg.campaign.plane.payload;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public interface IPlanePayload
{
    IPlanePayload copy();
    PlanePayloads getPayloads();
    PlaneModifications getModifications();

    PlanePayloadDesignation getSelectedPayloadDesignation() throws PWCGException;
    List<PlanePayloadDesignation> getAvailablePayloadDesignations(IFlight iFlight) throws PWCGException;
    int getPayloadIdByDescription(String payloadDescription);
    String getPayloadMaskByDescription(String payloadDescription);
    int createWeaponsPayload(IFlight flight) throws PWCGException;
    void createStandardWeaponsPayload();
    int getSelectedPayloadId();
    void setSelectedPayloadId(int selectedPrimaryPayloadId);
    void selectNoOrdnancePayload();
    boolean isOrdnance();

    void selectModification(PlanePayloadElement payloadElement);
    void clearModifications();
    List<PlanePayloadElement> getSelectedModifications() throws PWCGException;
    String generateFullModificationMask() throws PWCGException;
    List<PlanePayloadDesignation> getOptionalPayloadModifications();
}

package pwcg.campaign.plane.payload;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public interface IPlanePayload
{
    IPlanePayload copy();

    PayloadDesignation getSelectedPayloadDesignation() throws PWCGException;
    List<PayloadDesignation> getAvailablePayloadDesignations(IFlight iFlight) throws PWCGException;
    int getPayloadIdByDescription(String payloadDescription);
    String getPayloadMaskByDescription(String payloadDescription);
    int createWeaponsPayload(IFlight flight) throws PWCGException;
    void createStandardWeaponsPayload();
    int getSelectedPayloadId();
    void setSelectedPayloadId(int selectedPrimaryPayloadId);
    void selectNoOrdnancePayload();
    boolean isOrdnance();

    void selectModification(PayloadElement payloadElement);
    void clearModifications();
    List<PayloadElement> getSelectedModifications() throws PWCGException;
    String generateFullModificationMask() throws PWCGException;
    List<PayloadDesignation> getOptionalPayloadModifications();
}

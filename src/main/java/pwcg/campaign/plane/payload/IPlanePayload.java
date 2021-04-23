package pwcg.campaign.plane.payload;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public interface IPlanePayload
{
    public List<PayloadDesignation> getPayloadDesignations() throws PWCGException;
    public PayloadDesignation getSelectedPayloadDesignation() throws PWCGException;
    public int getPayloadIdByDescription(String payloadDescription);
    public String getPayloadMaskByDescription(String payloadDescription);
    public int createWeaponsPayload(IFlight flight) throws PWCGException;
    public int createStandardWeaponsPayload();
    public int getSelectedPayloadId();
    public void setSelectedPayloadId(int selectedPrimaryPayloadId);
    public void addModification(PayloadElement payloadElement);
    public void clearModifications();
    public List<PayloadElement> getModifications();
    public String generateFullModificationMask();
    public IPlanePayload copy();
    public void noOrdnance();
    List<PayloadElement> getStockModifications();
}

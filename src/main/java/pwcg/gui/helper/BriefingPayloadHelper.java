package pwcg.gui.helper;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.PlaneMcu;

public class BriefingPayloadHelper
{
    private Mission mission;
    private BriefingAssignmentData briefingAssignmentData = new BriefingAssignmentData();
    
	public BriefingPayloadHelper(Mission mission, BriefingAssignmentData briefingAssignmentData)
	{
        this.mission = mission;
        this.briefingAssignmentData = briefingAssignmentData;
	}

    public void initializePayloadsFromMission() throws PWCGException
    {
        assignPayloadsToCrewPlanes();
        assignModificationsToCrewPlanes();
    }

    public void modifyPayload(Integer pilotSerialNumber, int payloadId) 
    {
        try
        {
            CrewPlanePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByPilot(pilotSerialNumber);
            if (crewPlane != null)
            {
                crewPlane.setPayloadId(payloadId);
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    public void setPayloadForAddedPlane(Integer pilotSerialNumber) throws PWCGException
    {
        CrewPlanePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByPilot(pilotSerialNumber);
        executePayloadAssignmentSequence(crewPlane);
    }

    public void setPayloadForChangedPlane(Integer pilotSerialNumber) throws PWCGException
    {
        CrewPlanePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByPilot(pilotSerialNumber);
        executePayloadAssignmentSequence(crewPlane);
    }

    private void executePayloadAssignmentSequence(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        if (setPayloadToSimilarPlane(crewPlane))
        {
            return;
        }
        else
        {
            setPayloadFromPayloadFactory(crewPlane);
        }
    }

    private void assignPayloadsToCrewPlanes() throws PWCGException
    {
        IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(briefingAssignmentData.getSquadron().getSquadronId());
        for (PlaneMcu plane : playerFlight.getFlightPlanes().getPlanes())
        {
            CrewPlanePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByPlane(plane.getSerialNumber());
            if (crewPlane != null)
            {
                crewPlane.setPayloadId(plane.getPlanePayload().getSelectedPayloadDesignation().getPayloadId());
            }
        }
    }

    private void assignModificationsToCrewPlanes() throws PWCGException
    {
        IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(briefingAssignmentData.getSquadron().getSquadronId());
        for (PlaneMcu plane : playerFlight.getFlightPlanes().getPlanes())
        {
            CrewPlanePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByPlane(plane.getSerialNumber());
            if (crewPlane != null)
            {
            	for (PayloadElement modification : plane.getPlanePayload().getModifications())
            	{
            		crewPlane.addModification(modification.getDescription());
            	}
            }
        }
    }

    private boolean setPayloadToSimilarPlane(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        boolean setToSimilarPlane = false;
        for (CrewPlanePayloadPairing sourceCrewPlane : briefingAssignmentData.getCrews())
        {
            if (sourceCrewPlane.getPlane().getType().equals(crewPlane.getPlane().getType()))
            {
                if (sourceCrewPlane.getPilot().getSerialNumber() != crewPlane.getPilot().getSerialNumber())
                {
                    mapPayloadFromPlane(crewPlane, sourceCrewPlane);
                    setToSimilarPlane = true;
                    break;
                }
            }
        }
        return setToSimilarPlane;
    }

    private void mapPayloadFromPlane(CrewPlanePayloadPairing targetPlane, CrewPlanePayloadPairing sourcePlane)
    {
        targetPlane.setPayloadId(sourcePlane.getPayloadId());
        for (String modification : sourcePlane.getModifications())
        {
            targetPlane.addModification(modification);
        }
    }
    
    private void setPayloadFromPayloadFactory(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(briefingAssignmentData.getSquadron().getSquadronId());
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadFactory.createPlanePayload(crewPlane.getPlane().getType());
        payload.createWeaponsPayload(playerFlight);
        crewPlane.setPayloadId(payload.getSelectedPayloadId());
    }
}

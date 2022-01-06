package pwcg.gui.rofmap.brief;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPlanePayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.rofmap.brief.model.BriefingCrewMemberAssignmentData;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.playerunit.crew.CrewVehiclePayloadPairing;

public class BriefingPayloadHelper
{
    private Mission mission;
    private BriefingCrewMemberAssignmentData briefingAssignmentData = new BriefingCrewMemberAssignmentData();
    
	public BriefingPayloadHelper(Mission mission, BriefingCrewMemberAssignmentData briefingAssignmentData)
	{
        this.mission = mission;
        this.briefingAssignmentData = briefingAssignmentData;
	}

    public void initializePayloadsFromMission() throws PWCGException
    {
        assignPayloadsToCrewPlanes();
        assignModificationsToCrewPlanes();
    }

    public void modifyPayload(Integer crewMemberSerialNumber, int payloadId) 
    {
        try
        {
            CrewVehiclePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
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

    public void setPayloadForAddedPlane(Integer crewMemberSerialNumber) throws PWCGException
    {
        CrewVehiclePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
        executePayloadAssignmentSequence(crewPlane);
    }

    public void setPayloadForChangedPlane(Integer crewMemberSerialNumber) throws PWCGException
    {
        CrewVehiclePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
        executePayloadAssignmentSequence(crewPlane);
    }

    private void executePayloadAssignmentSequence(CrewVehiclePayloadPairing crewPlane) throws PWCGException
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
        IFlight playerFlight = mission.getFlights().getPlayerFlightForSquadron(briefingAssignmentData.getSquadron().getCompanyId());
        for (PlaneMcu plane : playerFlight.getFlightPlanes().getPlanes())
        {
            CrewVehiclePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByPlane(plane.getSerialNumber());
            if (crewPlane != null)
            {
                crewPlane.setPayloadId(plane.getPlanePayload().getSelectedPayloadDesignation().getPayloadId());
            }
        }
    }

    private void assignModificationsToCrewPlanes() throws PWCGException
    {
        IFlight playerFlight = mission.getFlights().getPlayerFlightForSquadron(briefingAssignmentData.getSquadron().getCompanyId());
        for (PlaneMcu plane : playerFlight.getFlightPlanes().getPlanes())
        {
            CrewVehiclePayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByPlane(plane.getSerialNumber());
            if (crewPlane != null)
            {
            	for (PlanePayloadElement modification : plane.getPlanePayload().getSelectedModifications())
            	{
            		crewPlane.addModification(modification.getDescription());
            	}
            }
        }
    }

    private boolean setPayloadToSimilarPlane(CrewVehiclePayloadPairing crewPlane) throws PWCGException
    {
        boolean setToSimilarPlane = false;
        for (CrewVehiclePayloadPairing sourceCrewPlane : briefingAssignmentData.getCrews())
        {
            if (sourceCrewPlane.getPlane().getType().equals(crewPlane.getPlane().getType()))
            {
                if (sourceCrewPlane.getCrewMember().getSerialNumber() != crewPlane.getCrewMember().getSerialNumber())
                {
                    mapPayloadFromPlane(crewPlane, sourceCrewPlane);
                    setToSimilarPlane = true;
                    break;
                }
            }
        }
        return setToSimilarPlane;
    }

    private void mapPayloadFromPlane(CrewVehiclePayloadPairing targetPlane, CrewVehiclePayloadPairing sourcePlane)
    {
        targetPlane.setPayloadId(sourcePlane.getPayloadId());
        for (String modification : sourcePlane.getModifications())
        {
            targetPlane.addModification(modification);
        }
    }
    
    private void setPayloadFromPayloadFactory(CrewVehiclePayloadPairing crewPlane) throws PWCGException
    {
        IFlight playerFlight = mission.getFlights().getPlayerFlightForSquadron(briefingAssignmentData.getSquadron().getCompanyId());
        IPlanePayloadFactory payloadFactory = PWCGContext.getInstance().getPlanePayloadFactory();
        IPlanePayload payload = payloadFactory.createPayload(crewPlane.getPlane().getType(), mission.getCampaign().getDate());
        payload.createWeaponsPayload(playerFlight);
        crewPlane.setPayloadId(payload.getSelectedPayloadId());
    }
}

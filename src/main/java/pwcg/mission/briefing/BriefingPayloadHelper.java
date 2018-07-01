package pwcg.mission.briefing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.rofmap.brief.BriefingCrewPlanePayloadSorter;
import pwcg.mission.Mission;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.PlaneMCU;

public class BriefingPayloadHelper
{
    private Mission mission;
    private Map <Integer, CrewPlanePayloadPairing> assignedCrewMap = new HashMap <>();
    
	public BriefingPayloadHelper(Mission mission, Map <Integer, CrewPlanePayloadPairing> assignedCrewMap)
	{
        this.mission = mission;
        this.assignedCrewMap = assignedCrewMap;
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
            CrewPlanePayloadPairing crewPlane = assignedCrewMap.get(pilotSerialNumber);
            if (crewPlane != null)
            {
                crewPlane.setPayloadId(payloadId);
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }
    }

    public void setPayloadForAddedPlane(Integer pilotSerialNumber) throws PWCGException
    {
        CrewPlanePayloadPairing crewPlane = assignedCrewMap.get(pilotSerialNumber);
        executePayloadAssignmentSequence(crewPlane);
    }

    public void setPayloadForChangedPlane(Integer pilotSerialNumber) throws PWCGException
    {
        CrewPlanePayloadPairing crewPlane = assignedCrewMap.get(pilotSerialNumber);
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
        for (PlaneMCU plane : mission.getMissionFlightBuilder().getPlayerFlight().getPlanes())
        {
            SquadronMember pilotOfPlane = plane.getPilot();
            CrewPlanePayloadPairing crewPlane = assignedCrewMap.get(pilotOfPlane.getSerialNumber());
            if (crewPlane != null)
            {
                crewPlane.setPayloadId(plane.getPlanePayload().getSelectedPayloadDesignation().getPayloadId());
            }
        }
    }

    private void assignModificationsToCrewPlanes() throws PWCGException
    {
        for (PlaneMCU plane : mission.getMissionFlightBuilder().getPlayerFlight().getPlanes())
        {
            SquadronMember pilotOfPlane = plane.getPilot();
            CrewPlanePayloadPairing crewPlane = assignedCrewMap.get(pilotOfPlane.getSerialNumber());
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
        for (CrewPlanePayloadPairing sourceCrewPlane : getCrewsSorted())
        {
            if (sourceCrewPlane.getPlaneType().equals(crewPlane.getPlaneType()))
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
        IPayloadFactory payloadFactory = PWCGContextManager.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadFactory.createPlanePayload(crewPlane.getPlaneType());
        payload.createWeaponsPayload(mission.getMissionFlightBuilder().getPlayerFlight());
        crewPlane.setPayloadId(payload.getSelectedPayloadId());
    }


    private List<CrewPlanePayloadPairing> getCrewsSorted() throws PWCGException
    {
        BriefingCrewPlanePayloadSorter crewSorter = new BriefingCrewPlanePayloadSorter(mission, assignedCrewMap);
        return crewSorter.getAssignedCrewsSorted();
    }
}

package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PayloadElementManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.flight.plane.PlaneMCUFactory;
import pwcg.mission.flight.plane.PlaneMcu;

public class PlayerFlightEditor
{
    private Campaign campaign;
    private IFlight playerFlight;
    private List<PlaneMcu> updatedPlaneSet = new ArrayList<PlaneMcu>();
    
    public PlayerFlightEditor(Campaign campaign, IFlight playerFlight)
    {
        this.campaign = campaign;
        this.playerFlight = playerFlight;
    }

    public void updatePlayerPlanes(List<CrewPlanePayloadPairing> crewPlanes) throws PWCGException
    {
        updatePlanesFromBriefing(crewPlanes);
        replacePlanesInPlayerFlight();
        resetPlayerFlightInitialPosition();
    }

    private void updatePlanesFromBriefing(List<CrewPlanePayloadPairing> crewPlanes) throws PWCGException
    {
        int numInFormation = 1;
        for (CrewPlanePayloadPairing crewPlane : crewPlanes)
        {
            createPlaneBasedOnBriefingSelections(numInFormation, crewPlane);
            ++numInFormation;
        }
    }

    private void replacePlanesInPlayerFlight() throws PWCGException
    {
        playerFlight.getFlightPlanes().setPlanes(updatedPlaneSet);
    }

    private void resetPlayerFlightInitialPosition() throws PWCGException
    {
        FlightPositionSetter.setFlightInitialPosition(playerFlight);
    }

    private void createPlaneBasedOnBriefingSelections(int numInFormation, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        PlaneMcu plane = null;
        if (numInFormation == 1)
        {
            plane = updateLeader(crewPlane);
        }
        else
        {
            plane = updateFlightMember(crewPlane);
        }

        plane.setNumberInFormation(numInFormation);
        plane.setCallsign(playerFlight.getSquadron().determineCurrentCallsign(campaign.getDate()));
        plane.setCallnum(numInFormation);
        setPayloadFromBriefing(plane, crewPlane);
        setModificationsFromBriefing(plane, crewPlane);
        configurePlaneForCrew(plane, crewPlane);

        updatedPlaneSet.add(plane);
    }

    private void setPayloadFromBriefing(PlaneMcu plane, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(plane.getType());
        payload.setSelectedPayloadId(crewPlane.getPayloadId());
        plane.setPlanePayload(payload);
    }

    private void setModificationsFromBriefing(PlaneMcu plane, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        IPlanePayload payload = plane.getPlanePayload();
        payload.clearModifications();

        PayloadElementManager payloadElementManager = new PayloadElementManager();
        for (String modificationDescription : crewPlane.getModifications())
        {
        	PayloadElement modification = payloadElementManager.getPayloadElementByDescription(modificationDescription);
        	payload.addModification(modification);
        }        
        plane.setPlanePayload(payload);
    }

    private void configurePlaneForCrew(PlaneMcu plane, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        AiSkillLevel aiLevel = crewPlane.getPilot().getAiSkillLevel();
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(playerFlight.getSquadron().getSquadronId());
        SquadronMember squadronMember = squadronPersonnel.getSquadronMember(crewPlane.getPilot().getSerialNumber());
        if (squadronMember == null)
        {
            squadronMember = campaign.getPersonnelManager().getCampaignAce(crewPlane.getPilot().getSerialNumber());
        }
        
        if (squadronMember.isPlayer())
        {
            aiLevel = AiSkillLevel.PLAYER;
        }

        plane.setName(crewPlane.getPilot().getNameAndRank());
        plane.setDesc(crewPlane.getPilot().getNameAndRank());
        plane.setAiLevel(aiLevel);
    }

    private PlaneMcu updateFlightMember(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        PlaneMcu flightmember = playerFlight.getFlightPlanes().getFlightLeader();
        PlaneMcu updatedPlaneMcu = PlaneMCUFactory.createPlaneMcuByPlaneType(campaign, crewPlane.getPlane(), 
                playerFlight.getFlightInformation().getCountry(), crewPlane.getPilot());
        updatedPlaneMcu.setIndex(IndexGenerator.getInstance().getNextIndex());
        updatedPlaneMcu.getEntity().setTarget(flightmember.getLinkTrId());

        return updatedPlaneMcu;
    }

    private PlaneMcu updateLeader(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {        
        PlaneMcu updatedFlightLeader = PlaneMCUFactory.createPlaneMcuByPlaneType(campaign, crewPlane.getPlane(), 
                playerFlight.getFlightInformation().getCountry(), crewPlane.getPilot());
        PlaneMcu flightLeaderPlaneMcu = playerFlight.getFlightPlanes().getFlightLeader();        
        updatedFlightLeader.setIndex(flightLeaderPlaneMcu.getIndex());
        updatedFlightLeader.setLinkTrId(flightLeaderPlaneMcu.getLinkTrId());
        updatedFlightLeader.getEntity().setIndex(flightLeaderPlaneMcu.getEntity().getIndex());
        updatedFlightLeader.setFuel(flightLeaderPlaneMcu.getFuel());

        return updatedFlightLeader;
    }
}

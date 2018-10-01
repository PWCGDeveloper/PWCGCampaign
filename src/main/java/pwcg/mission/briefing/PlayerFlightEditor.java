package pwcg.mission.briefing;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PayloadElementManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPositionHelperPlayerStart;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.plane.PlaneMCUFactory;

public class PlayerFlightEditor
{
    private Campaign campaign;
    private Flight playerFlight;
    private List<PlaneMCU> updatedPlaneSet = new ArrayList<PlaneMCU>();
    
    public PlayerFlightEditor(Campaign campaign, Flight playerFlight)
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
        int numInFormation = 0;
        for (CrewPlanePayloadPairing crewPlane : crewPlanes)
        {
            createPlaneBasedOnBriefingSelections(numInFormation, crewPlane);
            ++numInFormation;
        }
    }

    private void replacePlanesInPlayerFlight() throws PWCGException
    {
        playerFlight.setPlanes(updatedPlaneSet);
    }

    private void resetPlayerFlightInitialPosition() throws PWCGException
    {
        FlightPositionHelperPlayerStart flightPositionHelperPlayerStart = new FlightPositionHelperPlayerStart(campaign, playerFlight);
        flightPositionHelperPlayerStart.createPlayerPlanePosition();
    }

    private void createPlaneBasedOnBriefingSelections(int numInFormation, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        PlaneMCU plane = null;
        if (numInFormation == 0)
        {
            plane = updateLeader(crewPlane);
        }
        else
        {
            plane = updateFlightMember(crewPlane);
        }

        plane.setNumberInFormation(numInFormation);
        plane.setCallsign(playerFlight.getSquadron().determineCurrentCallsign(campaign.getDate()));
        plane.setCallnum(numInFormation + 1);
        setPayloadFromBriefing(plane, crewPlane);
        setModificationsFromBriefing(plane, crewPlane);
        configurePlaneForCrew(plane, crewPlane);

        updatedPlaneSet.add(plane);
    }

    private void setPayloadFromBriefing(PlaneMCU plane, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        IPayloadFactory payloadfactory = PWCGContextManager.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(plane.getType());
        payload.setSelectedPayloadId(crewPlane.getPayloadId());
        plane.setPlanePayload(payload);
    }

    private void setModificationsFromBriefing(PlaneMCU plane, CrewPlanePayloadPairing crewPlane) throws PWCGException
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

    private void configurePlaneForCrew(PlaneMCU plane, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        AiSkillLevel aiLevel = crewPlane.getPilot().getAiSkillLevel();
        SquadronMember squadronMember = campaign.getPersonnelManager().getPlayerPersonnel().getSquadronMember(crewPlane.getPilot().getSerialNumber());
        if (squadronMember.isPlayer())
        {
            aiLevel = AiSkillLevel.PLAYER;
        }

        plane.setName(crewPlane.getPilot().getNameAndRank());
        plane.setDesc(crewPlane.getPilot().getNameAndRank());
        plane.setAiLevel(aiLevel);
    }

    private PlaneMCU updateFlightMember(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        PlaneMCU flightLeader = playerFlight.getLeadPlane();
        
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        PlaneMCUFactory PlaneMCUFactory = new PlaneMCUFactory(campaign, squadron, playerFlight);
        PlaneMCU updatedPlaneMcu = PlaneMCUFactory.createPlaneMcuByPlaneType(crewPlane.getPlane(), playerFlight.getCountry(), crewPlane.getPilot());

        updatedPlaneMcu.setIndex(IndexGenerator.getInstance().getNextIndex());
        updatedPlaneMcu.getEntity().setTarget(flightLeader.getLinkTrId());

        return updatedPlaneMcu;
    }

    private PlaneMCU updateLeader(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        PlaneMCUFactory PlaneMCUFactory = new PlaneMCUFactory(campaign, squadron, playerFlight);
        PlaneMCU modifiedLeadPlane = PlaneMCUFactory.createPlaneMcuByPlaneType(crewPlane.getPlane(), playerFlight.getCountry(), crewPlane.getPilot());

        PlaneMCU flightLeaderPlaneMcu = playerFlight.getLeadPlane();        
        modifiedLeadPlane.setIndex(flightLeaderPlaneMcu.getIndex());
        modifiedLeadPlane.setLinkTrId(flightLeaderPlaneMcu.getLinkTrId());
        modifiedLeadPlane.getEntity().setIndex(flightLeaderPlaneMcu.getEntity().getIndex());

        return modifiedLeadPlane;
    }
}

package pwcg.gui.rofmap.brief.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.plane.payload.IPlanePayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.campaign.plane.payload.PlanePayloadElementManager;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMCUFactory;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.playerunit.crew.CrewVehiclePayloadPairing;

public class BriefingFlightCrewPlaneUpdater
{
    private Campaign campaign;
    private IFlight playerFlight;
    private List<PlaneMcu> updatedPlaneSet = new ArrayList<PlaneMcu>();
    
    public BriefingFlightCrewPlaneUpdater(Campaign campaign, IFlight playerFlight)
    {
        this.campaign = campaign;
        this.playerFlight = playerFlight;
    }

    public void updatePlayerPlanes(List<CrewVehiclePayloadPairing> crewPlanes) throws PWCGException
    {
        updatePlanesFromBriefing(crewPlanes);
        replacePlanesInPlayerFlight();
    }

    private void updatePlanesFromBriefing(List<CrewVehiclePayloadPairing> crewPlanes) throws PWCGException
    {
        int numInFormation = 1;
        for (CrewVehiclePayloadPairing crewPlane : crewPlanes)
        {
            createPlaneBasedOnBriefingSelections(numInFormation, crewPlane);
            ++numInFormation;
        }
    }

    private void replacePlanesInPlayerFlight() throws PWCGException
    {
        playerFlight.getFlightPlanes().setPlanes(updatedPlaneSet);
    }

    private void createPlaneBasedOnBriefingSelections(int numInFormation, CrewVehiclePayloadPairing crewPlane) throws PWCGException
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

    private void setPayloadFromBriefing(PlaneMcu plane, CrewVehiclePayloadPairing crewPlane) throws PWCGException
    {
        IPlanePayloadFactory payloadfactory = PWCGContext.getInstance().getPlanePayloadFactory();
        IPlanePayload payload = payloadfactory.createPayload(plane.getType(), campaign.getDate());
        payload.setSelectedPayloadId(crewPlane.getPayloadId());
        plane.setPlanePayload(payload);
    }

    private void setModificationsFromBriefing(PlaneMcu plane, CrewVehiclePayloadPairing crewPlane) throws PWCGException
    {
        IPlanePayload payload = plane.getPlanePayload();
        payload.clearModifications();

        PlanePayloadElementManager payloadElementManager = new PlanePayloadElementManager();
        for (String modificationDescription : crewPlane.getModifications())
        {
        	PlanePayloadElement modification = payloadElementManager.getPayloadElementByDescription(modificationDescription);
        	payload.selectModification(modification);
        }        
        plane.setPlanePayload(payload);
    }

    private void configurePlaneForCrew(PlaneMcu plane, CrewVehiclePayloadPairing crewPlane) throws PWCGException
    {
        AiSkillLevel aiLevel = crewPlane.getCrewMember().getAiSkillLevel();
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(playerFlight.getSquadron().getCompanyId());
        CrewMember crewMember = squadronPersonnel.getCrewMember(crewPlane.getCrewMember().getSerialNumber());
        if (crewMember == null)
        {
            crewMember = campaign.getPersonnelManager().getCampaignAce(crewPlane.getCrewMember().getSerialNumber());
        }
        
        if (crewMember.isPlayer())
        {
            aiLevel = AiSkillLevel.PLAYER;
        }

        plane.setName(crewPlane.getCrewMember().getNameAndRank());
        plane.setDesc(crewPlane.getCrewMember().getNameAndRank());
        plane.setAiLevel(aiLevel);
    }

    private PlaneMcu updateFlightMember(CrewVehiclePayloadPairing crewPlane) throws PWCGException
    {
        PlaneMcu flightmember = playerFlight.getFlightPlanes().getFlightLeader();
        PlaneMcu updatedPlaneMcu = PlaneMCUFactory.createPlaneMcuByTankType(campaign, crewPlane.getPlane(), 
                playerFlight.getFlightInformation().getCountry(), crewPlane.getCrewMember());
        updatedPlaneMcu.setTarget(flightmember.getLinkTrId());
        updatedPlaneMcu.setFuel(flightmember.getFuel());

        return updatedPlaneMcu;
    }

    private PlaneMcu updateLeader(CrewVehiclePayloadPairing crewPlane) throws PWCGException
    {        
        PlaneMcu updatedFlightLeader = PlaneMCUFactory.createPlaneMcuByTankType(campaign, crewPlane.getPlane(), 
                playerFlight.getFlightInformation().getCountry(), crewPlane.getCrewMember());
        PlaneMcu flightLeaderPlaneMcu = playerFlight.getFlightPlanes().getFlightLeader();        
        updatedFlightLeader.copyEntityIndexFromPlane(flightLeaderPlaneMcu);
        updatedFlightLeader.setLinkTrId(flightLeaderPlaneMcu.getLinkTrId());
        updatedFlightLeader.copyEntityIndexFromPlane(flightLeaderPlaneMcu);
        updatedFlightLeader.setFuel(flightLeaderPlaneMcu.getFuel());

        return updatedFlightLeader;
    }
}

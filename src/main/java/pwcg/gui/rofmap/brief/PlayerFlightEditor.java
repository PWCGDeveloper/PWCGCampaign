package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PayloadElementManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
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

    public List<PlaneMCU> updatePlayerPlanes(List<CrewPlanePayloadPairing> crewPlanes) throws PWCGException
    {
        int numInFormation = 1;
        for (CrewPlanePayloadPairing crewPlane : crewPlanes)
        {
            createPlaneBasedOnBriefingSelections(numInFormation, crewPlane);
            ++numInFormation;
        }

        return updatedPlaneSet;
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
        setPayloadFromBriefing(plane, crewPlane);
        setModificationsFromBriefing(plane, crewPlane);
        configurePlaneForCrew(plane, crewPlane);

        configureForAirStart(playerFlight, plane);

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
        if (crewPlane.getPilot().getSerialNumber() == campaign.getPlayer().getSerialNumber())
        {
            aiLevel = AiSkillLevel.PLAYER;
        }

        plane.setName(crewPlane.getPilot().getNameAndRank());
        plane.setDesc(crewPlane.getPilot().getNameAndRank());
        plane.setAiLevel(aiLevel);
    }

    private void configureForAirStart(Flight playerFlight, PlaneMCU plane)
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int startInAirVal = productSpecificConfiguration.startInAir();
        int startOnRunwayVal = productSpecificConfiguration.startOnRunway();
        plane.setStartInAir(startOnRunwayVal);

        if (playerFlight.isAirstart())
        {
            plane.setStartInAir(startInAirVal);
        }
    }

    private PlaneMCU updateFlightMember(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        PlaneMCU flightLeader = playerFlight.getLeadPlane();

        PlaneType updatedPlaneType = getPlaneForPlayerSquadron(playerFlight, crewPlane.getPlaneType(), crewPlane);
        
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        PlaneMCUFactory PlaneMCUFactory = new PlaneMCUFactory(campaign, squadron, playerFlight);
        PlaneMCU updatedPlane = PlaneMCUFactory.createPlaneByPlaneType(updatedPlaneType, playerFlight.getCountry(), crewPlane.getPilot());

        updatedPlane.setIndex(IndexGenerator.getInstance().getNextIndex());
        updatedPlane.getEntity().setTarget(flightLeader.getLinkTrId());

        return updatedPlane;
    }

    private PlaneMCU updateLeader(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        PlaneType modifiedLeadPlaneType = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(crewPlane.getPlaneType());
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        PlaneMCUFactory PlaneMCUFactory = new PlaneMCUFactory(campaign, squadron, playerFlight);
        PlaneMCU modifiedLeadPlane = PlaneMCUFactory.createPlaneByPlaneType(modifiedLeadPlaneType, playerFlight.getCountry(), crewPlane.getPilot());

        PlaneMCU flightLeader = playerFlight.getLeadPlane();
        flightLeader.setDisplayName(modifiedLeadPlane.getDisplayName());
        flightLeader.setModel(modifiedLeadPlane.getModel());
        flightLeader.setScript(modifiedLeadPlane.getScript());
        flightLeader.setType(modifiedLeadPlane.getType());
        flightLeader.setEndurance(modifiedLeadPlane.getEndurance());
        flightLeader.setCruisingSpeed(modifiedLeadPlane.getCruisingSpeed());

        return flightLeader;
    }

    private PlaneType getPlaneForPlayerSquadron(Flight playerFlight, String planeType, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        List<PlaneType> aircraftTypes = playerFlight.getSquadron().determineCurrentAircraftList(campaign.getDate());
        if (aircraftTypes.size() <= 0)
        {
            throw new PWCGException("No planes for player squadron on this date");
        }

        PlaneType playerSquadronPlaneType = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeType);
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        PlaneMCUFactory PlaneMCUFactory = new PlaneMCUFactory(campaign, squadron, playerFlight);
        PlaneMCU playerFlightPlane = PlaneMCUFactory.createPlaneByPlaneType(playerSquadronPlaneType, playerFlight.getCountry(), crewPlane.getPilot());

        return playerFlightPlane;
    }
}

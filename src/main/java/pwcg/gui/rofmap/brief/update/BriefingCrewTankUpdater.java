package pwcg.gui.rofmap.brief.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayloadElement;
import pwcg.campaign.tank.payload.TankPayloadElementManager;
import pwcg.campaign.tank.payload.TankPayloadFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.PlayerUnit;
import pwcg.mission.playerunit.TankMcu;
import pwcg.mission.playerunit.crew.CrewTankPayloadPairing;
import pwcg.mission.unit.tank.TankMcuFactory;

public class BriefingCrewTankUpdater
{
    private Campaign campaign;
    private PlayerUnit playerUnit;
    private List<TankMcu> updatedTankSet = new ArrayList<>();
    
    public BriefingCrewTankUpdater(Campaign campaign, PlayerUnit playerFlight)
    {
        this.campaign = campaign;
        this.playerUnit = playerFlight;
    }

    public void updatePlayerTanks(List<CrewTankPayloadPairing> crewTanks) throws PWCGException
    {
        updateTanksFromBriefing(crewTanks);
        replaceTanksInPlayerFlight();
    }

    private void updateTanksFromBriefing(List<CrewTankPayloadPairing> crewTanks) throws PWCGException
    {
        int numInFormation = 1;
        for (CrewTankPayloadPairing crewTank : crewTanks)
        {
            createTankBasedOnBriefingSelections(numInFormation, crewTank);
            ++numInFormation;
        }
    }

    private void replaceTanksInPlayerFlight() throws PWCGException
    {
        playerUnit.getUnitTanks().setTanks(updatedTankSet);
    }

    private void createTankBasedOnBriefingSelections(int numInFormation, CrewTankPayloadPairing crewTank) throws PWCGException
    {
        TankMcu tank = null;
        if (numInFormation == 1)
        {
            tank = updateLeader(crewTank);
        }
        else
        {
            tank = updateFlightMember(crewTank);
        }

        tank.setNumberInFormation(numInFormation);
        tank.setCallsign(playerUnit.getCompany().determineCurrentCallsign(campaign.getDate()));
        tank.setCallnum(numInFormation);
        setPayloadFromBriefing(tank, crewTank);
        setModificationsFromBriefing(tank, crewTank);
        configureTankForCrew(tank, crewTank);

        updatedTankSet.add(tank);
    }

    private void setPayloadFromBriefing(TankMcu tank, CrewTankPayloadPairing crewTank) throws PWCGException
    {
        TankPayloadFactory payloadfactory = new TankPayloadFactory();
        ITankPayload payload = payloadfactory.createPayload(tank.getType(), campaign.getDate());
        payload.setSelectedPayloadId(crewTank.getPayloadId());
        tank.setTankPayload(payload);
    }

    private void setModificationsFromBriefing(TankMcu tank, CrewTankPayloadPairing crewTank) throws PWCGException
    {
        ITankPayload payload = tank.getTankPayload();
        payload.clearModifications();

        TankPayloadElementManager payloadElementManager = new TankPayloadElementManager();
        for (String modificationDescription : crewTank.getModifications())
        {
        	TankPayloadElement modification = payloadElementManager.getPayloadElementByDescription(modificationDescription);
        	payload.selectModification(modification);
        }        
        tank.setTankPayload(payload);
    }

    private void configureTankForCrew(TankMcu tank, CrewTankPayloadPairing crewTank) throws PWCGException
    {
        AiSkillLevel aiLevel = crewTank.getCrewMember().getAiSkillLevel();
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(playerUnit.getCompany().getCompanyId());
        CrewMember crewMember = squadronPersonnel.getCrewMember(crewTank.getCrewMember().getSerialNumber());
        if (crewMember == null)
        {
            crewMember = campaign.getPersonnelManager().getCampaignAce(crewTank.getCrewMember().getSerialNumber());
        }
        
        if (crewMember.isPlayer())
        {
            aiLevel = AiSkillLevel.PLAYER;
        }

        tank.setName(crewTank.getCrewMember().getNameAndRank());
        tank.setDesc(crewTank.getCrewMember().getNameAndRank());
        tank.setAiLevel(aiLevel);
    }

    private TankMcu updateFlightMember(CrewTankPayloadPairing crewTank) throws PWCGException
    {
        TankMcu flightmember = playerUnit.getUnitTanks().getUnitLeader();
        TankMcu updatedTankMcu = TankMcuFactory.createTankMcuByTankType(campaign, crewTank.getTank(), 
                playerUnit.getUnitInformation().getCountry(), crewTank.getCrewMember());
        updatedTankMcu.setTarget(flightmember.getLinkTrId());
        updatedTankMcu.setFuel(flightmember.getFuel());

        return updatedTankMcu;
    }

    private TankMcu updateLeader(CrewTankPayloadPairing crewTank) throws PWCGException
    {        
        TankMcu updatedFlightLeader = TankMcuFactory.createTankMcuByTankType(campaign, crewTank.getTank(), 
                playerUnit.getUnitInformation().getCountry(), crewTank.getCrewMember());
        TankMcu flightLeaderTankMcu = playerUnit.getUnitTanks().getUnitLeader();        
        updatedFlightLeader.copyEntityIndexFromTank(flightLeaderTankMcu);
        updatedFlightLeader.setLinkTrId(flightLeaderTankMcu.getLinkTrId());
        updatedFlightLeader.copyEntityIndexFromTank(flightLeaderTankMcu);
        updatedFlightLeader.setFuel(flightLeaderTankMcu.getFuel());

        return updatedFlightLeader;
    }
}

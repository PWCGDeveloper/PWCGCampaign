package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;

public class MissionBlockBuilder
{
    private Mission mission;
    private List<FixedPosition> structuresForMission = new ArrayList<>();

    public MissionBlockBuilder(Mission mission, CoordinateBox structureBorder)
    {
        this.mission = mission;
    }

    public MissionBlocks buildFixedPositionsForMission() throws PWCGException
    {
        getBlocks();
        MissionBlocks missionBlocks = new MissionBlocks(mission, structuresForMission);
        return missionBlocks;
    }

    private void getBlocks() throws PWCGException
    {
        getAirfieldsForPatrol();
        getTrainStationsPatrol();
        getBridgesForPatrol();
        getStandaloneBlocksPatrol();
    }

    private void getTrainStationsPatrol() throws PWCGException
    {
        List<Block> selectedRRStations = new ArrayList<>();
        GroupManager groupData = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        for (Block rrStation : groupData.getRailroadList())
        {
            if (isBlockIncluded(rrStation.getPosition()))
            {
                selectedRRStations.add(rrStation);
            }
        }

        structuresForMission.addAll(selectedRRStations);
    }

    private void getBridgesForPatrol() throws PWCGException
    {
        ArrayList<Bridge> selectedBridges = new ArrayList<>();
        GroupManager groupData = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        List<Bridge> allBridges = groupData.getBridgeFinder().findAllBridges();
        for (Bridge bridge : allBridges)
        {
            if (isBlockIncluded(bridge.getPosition()))
            {
                selectedBridges.add(bridge);
            }
        }

        structuresForMission.addAll(selectedBridges);
    }

    private void getAirfieldsForPatrol() throws PWCGException
    {
        ArrayList<Block> selectedAirfieldStructures = new ArrayList<>();
        GroupManager groupData = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        List<Block> allAirfieldStructures = groupData.getAirfieldBlocks();
        for (Block airfieldStructure : allAirfieldStructures)
        {
            if (isBlockIncluded(airfieldStructure.getPosition()))
            {
                selectedAirfieldStructures.add(airfieldStructure);
            }
        }

        structuresForMission.addAll(selectedAirfieldStructures);
    }

    private void getStandaloneBlocksPatrol() throws PWCGException
    {
        List<Block> selectedBlocks = new ArrayList<>();

        GroupManager groupData = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        for (Block block : groupData.getStandaloneBlocks())
        {
            if (isBlockIncluded(block.getPosition()))
            {
                selectedBlocks.add(block);
            }
        }

        structuresForMission.addAll(selectedBlocks);
    }

    private boolean isBlockIncluded(Coordinate blockPosition) throws PWCGException
    {
        if (mission.getStructureBorders().isInBox(blockPosition))
        {
            return true;
        }
        else if (isBlockNearPlayer(blockPosition))
        {
            return true;
        }
        return false;
    }

    private boolean isBlockNearPlayer(Coordinate blockPosition) throws PWCGException
    {
        for (CrewMember player : mission.getParticipatingPlayers().getAllParticipatingPlayers())
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(player.getCompanyId());
            Airfield airfield = squadron.determineCurrentAirfieldAnyMap(mission.getCampaign().getDate());
            CoordinateBox airfieldBox = CoordinateBox.coordinateBoxFromCenter(airfield.getPosition(), 10000);
            if (airfieldBox.isInBox(blockPosition))
            {
                return true;
            }
        }
        
        return false;
    }
}

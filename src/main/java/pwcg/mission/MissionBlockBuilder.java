package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;

public class MissionBlockBuilder
{
    private Mission mission;
    private List<FixedPosition> positionsForMission = new ArrayList<>();

    public MissionBlockBuilder(Mission mission, CoordinateBox structureBorder)
    {
        this.mission = mission;
    }

    public MissionBlocks buildFixedPositionsForMission() throws PWCGException
    {
        getBlocks();
        MissionBlocks missionBlocks = new MissionBlocks(mission, positionsForMission);
        missionBlocks.adjustBlockStatus();
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

        positionsForMission.addAll(selectedRRStations);
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

        positionsForMission.addAll(selectedBridges);
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

        positionsForMission.addAll(selectedAirfieldStructures);
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

        positionsForMission.addAll(selectedBlocks);
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
        for (SquadronMember player : mission.getParticipatingPlayers().getAllParticipatingPlayers())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
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

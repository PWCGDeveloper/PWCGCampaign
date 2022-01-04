package pwcg.mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.BridgeFinder;
import pwcg.campaign.group.RailroadStationFinder;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationAwayFromFrontFinder;
import pwcg.core.location.PWCGLocation;

public class MissionCenterBuilderRaid implements IMissionCenterBuilder
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    
    public MissionCenterBuilderRaid(Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }

    @Override
    public Coordinate findMissionCenter(int missionBoxRadius) throws PWCGException
    {   
        List<PWCGLocation> raidTargetLocations = getRaidLocations(missionBoxRadius);
        List<PWCGLocation> raidTargetLocationsAwayFromFont = getRaidLocationsAwayFromFront(raidTargetLocations);
        return pickRaidTarget(raidTargetLocationsAwayFromFont);        
    }

    private List<PWCGLocation> getRaidLocations(int missionBoxRadius) throws PWCGException
    {
        Company squadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();        
        Coordinate squadronLocation = squadron.determineCurrentPosition(campaign.getDate());

        List<Airfield> airfieldTargets = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getNearbyOccupiedAirFieldsForSide(
                squadron.determineSide().getOppositeSide(), campaign.getDate(), squadronLocation, missionBoxRadius * 2);
        
        RailroadStationFinder railroadStationFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getRailroadStationFinder();
        List<Block> railwaystations = railroadStationFinder.getTrainPositionWithinRadiusBySide(
                squadron.determineSide().getOppositeSide(), campaign.getDate(), squadronLocation, missionBoxRadius * 2);

        
        BridgeFinder bridgeFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getBridgeFinder();
        List<Bridge> bridges = bridgeFinder.findBridgesForSideWithinRadius(
                squadron.determineSide().getOppositeSide(), campaign.getDate(), squadronLocation, missionBoxRadius * 2);

        
        List<PWCGLocation> allRaidTargets = new ArrayList<>();
        allRaidTargets.addAll(airfieldTargets);
        allRaidTargets.addAll(railwaystations);        
        addLimitedBridges(airfieldTargets, railwaystations, bridges, allRaidTargets);

        return allRaidTargets;
    }

    private void addLimitedBridges(List<Airfield> airfieldTargets, List<Block> railwaystations, List<Bridge> bridges, List<PWCGLocation> allRaidTargets)
    {
        int numBridgesToKeep = (airfieldTargets.size() + railwaystations.size()) / 2;
        if (numBridgesToKeep > bridges.size())
        {
            numBridgesToKeep = bridges.size();
        }
        
        if (numBridgesToKeep > 0)
        {
            for (int i = 0; i < numBridgesToKeep; ++i)
            {
                allRaidTargets.add(bridges.get(i));
            }
        }
        else
        {
            allRaidTargets.addAll(bridges);
        }
    }
    
    private List<PWCGLocation> getRaidLocationsAwayFromFront(List<PWCGLocation> raidLocations) throws PWCGException
    {
        Company squadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();        

        List<PWCGLocation> raidLocationsAwayFromFront = LocationAwayFromFrontFinder.getLocationsAwayFromFront(
                raidLocations, squadron.determineSide().getOppositeSide(), campaign.getDate());

        return raidLocationsAwayFromFront;
    }

    private Coordinate pickRaidTarget(List<PWCGLocation> raidLocationsAwayFromFont) throws PWCGException
    {
        if (!raidLocationsAwayFromFont.isEmpty())
        {
            Collections.shuffle(raidLocationsAwayFromFont);
            return raidLocationsAwayFromFont.get(0).getPosition();
        }
        else
        {
            Company squadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();        
            Coordinate squadronLocation = squadron.determineCurrentPosition(campaign.getDate());
            return squadronLocation;
        }
    }
}

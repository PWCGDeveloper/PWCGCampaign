package pwcg.aar.prelim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

public class PwcgMissionDataEvaluator
{
    private Campaign campaign;
    private AARPreliminaryData aarPreliminarytData;

    public PwcgMissionDataEvaluator (Campaign campaign, AARPreliminaryData aarPreliminarytData) throws PWCGException
    {
        this.campaign = campaign;
        this.aarPreliminarytData = aarPreliminarytData;
    }

    public List<String> determineAxisPlaneTypesInMission() throws PWCGException
    {
        return determinePlaneTypesForSide(Side.AXIS);
    }

    public List<String> determineAlliedPlaneTypesInMission() throws PWCGException
    {
        return determinePlaneTypesForSide(Side.ALLIED);
    }

    private List<String> determinePlaneTypesForSide(Side side) throws PWCGException
    {
        Set<String> uniquePlanesForSide = new HashSet<>();

        for (PwcgGeneratedMissionPlaneData missionPlane : aarPreliminarytData.getPwcgMissionData().getMissionPlanes().values())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(missionPlane.getSquadronId());            
            if (squadron.determineSquadronCountry(campaign.getDate()).getSide() == side)
            {
                uniquePlanesForSide.add(missionPlane.getAircraftType());
            }
        }

        return new ArrayList<String>(uniquePlanesForSide);
    }

    public PwcgGeneratedMissionPlaneData getPlaneForPilotBySerialNumber(Integer serialNumber) throws PWCGException
    {
        return aarPreliminarytData.getPwcgMissionData().getMissionPlane(serialNumber);
    }
    
    public PwcgGeneratedMissionPlaneData getPlaneForPilotByName(String name) throws PWCGException
    {
        for (PwcgGeneratedMissionPlaneData missionPlane : aarPreliminarytData.getPwcgMissionData().getMissionPlanes().values())
        {
            SquadronMember squadronMember = campaign.getPersonnelManager().getAnyCampaignMember(missionPlane.getPilotSerialNumber());
            if (squadronMember.isPilotName(name))
            {
                return missionPlane;
            }
        }
        
        throw new PWCGException("No missionPlane found for name " + name);
    }

    public boolean wasPilotAssignedToMission(int squadronId, String name) throws PWCGException
    {
        SquadronMember squadronMember = campaign.getPersonnelManager().getSquadronPersonnel(squadronId).getSquadronMembersWithAces().getSquadronMemberByName(name);
        return wasPilotAssignedToMission(squadronMember.getSerialNumber());
    }

    public boolean wasPilotAssignedToMission(Integer serialNumber) throws PWCGException
    {
        for (SquadronMember pilotInMission : aarPreliminarytData.getCampaignMembersInMission().getSquadronMemberCollection().values())
        {
            if (pilotInMission.getSerialNumber() == serialNumber)
            {
                return true;
            }
        }

        return false;
    }

    public boolean wasPilotAssignedToMissionByName(String pilotName) throws PWCGException
    {
        for (SquadronMember pilotInMission : aarPreliminarytData.getCampaignMembersInMission().getSquadronMemberCollection().values())
        {
            if (pilotInMission.isPilotName(pilotName))
            {
                return true;
            }
        }

        return false;
    }
}

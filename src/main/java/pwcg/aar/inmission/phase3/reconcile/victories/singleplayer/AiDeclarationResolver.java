package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogEntityPlaneResolver;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class AiDeclarationResolver  extends PlayerVictoryResolver
{
    private ConfirmedVictories confirmedAiVictories = new ConfirmedVictories();
    private Campaign campaign;
    private AARContext aarContext;

    public AiDeclarationResolver (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public ConfirmedVictories determineAiAirResults  (VictorySorter victorySorter) throws PWCGException 
    {
        for (LogVictory resultVictory : victorySorter.getFirmAirVictories())
        {
            resolveAiFirmClaim(resultVictory);
        }
        
        for (LogVictory resultVictory : victorySorter.getFirmBalloonVictories())
        {
            resolveAiFirmClaim(resultVictory);
        }
        
        for (LogVictory resultVictory : victorySorter.getFuzzyAirVictories())
        {
            resolveAiClaimByProximity(resultVictory);
        }
        
        for (LogVictory resultVictory : victorySorter.getAllUnconfirmed())
        {
            resolveRandomAssignment(resultVictory);
        }
        
        return confirmedAiVictories;
    }

    private void resolveAiFirmClaim(LogVictory resultVictory) throws PWCGException 
    {
        if (resultVictory.getVictor() instanceof LogPlane)
        {
            LogPlane victorPlanePlane = (LogPlane)resultVictory.getVictor();
            SquadronMember squadronMemberVictor = campaign.getPersonnelManager().getAnyCampaignMember(victorPlanePlane.getPilotSerialNumber());
            if (squadronMemberVictor != null)
            {
                LogPlane victoriousPlane = LogEntityPlaneResolver.getPlaneForEntity(resultVictory.getVictor());
                if (!PlayerVictoryResolver.isPlayerVictory(squadronMemberVictor, victoriousPlane))
                {
                    if (!resultVictory.isConfirmed())
                    {
                        createAiVictory(resultVictory, squadronMemberVictor);
                    }
                }
            }
        }
    }

    private void resolveAiClaimByProximity(LogVictory resultVictory) throws PWCGException 
    {
        if (!resultVictory.isConfirmed())
        {
            if (resultVictory.getVictor() instanceof LogUnknown)
            {
                SquadronMember pilotVictor = flightMemberForVictory(resultVictory);
                if (pilotVictor != null)
                {
                    createAiVictory(resultVictory, pilotVictor);
                }
            }
        }
    }

    private void resolveRandomAssignment(LogVictory resultVictory) throws PWCGException 
    {
        if (!resultVictory.isConfirmed())
        {
            if (resultVictory.getVictor() instanceof LogUnknown)
            {
                LogUnknown missionEntityUnknown = (LogUnknown)resultVictory.getVictor();
                if (missionEntityUnknown.getUnknownVictoryAssignment() == UnknownVictoryAssignments.RANDOM_ASSIGNMENT)
                {
                    SquadronMember pilotVictor = flightMemberForVictory(resultVictory);
                    if (pilotVictor != null)
                    {
                        createAiVictory(resultVictory, pilotVictor);
                    }
                }
            }
        }
    }

    private SquadronMember flightMemberForVictory(LogVictory resultVictory) throws PWCGException
    {
        ICountry victimCountry = resultVictory.getVictim().getCountry();
        SquadronMembers squadronMembersInMissionOtherThanPlayer = getAiMissionSquadronMembers();
        for (SquadronMember pilotVictor: squadronMembersInMissionOtherThanPlayer.getSquadronMemberList())
        {
            if (pilotVictor.determineCountry().getSide() != victimCountry.getSide())
            {
                if (pilotVictor != null)
                {
                    if (!alreadyhasVictory(pilotVictor.getSerialNumber()))
                    {
                        return pilotVictor;
                    }
                }
            }
        }
        
        return null;
    }

    private SquadronMembers getAiMissionSquadronMembers() throws PWCGException
    {
        Map<Integer, SquadronMember> campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission().getSquadronMemberCollection();
        List<Squadron> playerSquadronsInMission = aarContext.getPreliminaryData().getPlayerSquadronsInMission();
        SquadronMembers squadronMembersInMissionOtherThanPlayer = new SquadronMembers();
        for (Squadron squadron : playerSquadronsInMission)
        {
            SquadronMembers squadronMembersForSquadron = SquadronMemberFilter.filterActiveAIForSquadron(campaignMembersInMission, campaign.getDate(), squadron.getSquadronId());
            squadronMembersInMissionOtherThanPlayer.addSquadronMembers(squadronMembersForSquadron);
        }
        return squadronMembersInMissionOtherThanPlayer;
    }

    private boolean alreadyhasVictory(Integer serialNumber) throws PWCGException
    {
        for (LogVictory confirmedAiVictory : confirmedAiVictories.getConfirmedVictories())
        {
            if (confirmedAiVictory.getVictor() instanceof LogPlane)
            {
                LogPlane victorPlanePlane = (LogPlane)confirmedAiVictory.getVictor();
                if (victorPlanePlane.getPilotSerialNumber() == serialNumber)
                {
                    return true;
                }
            }
        }

        return false;
   }

    private void createAiVictory(LogVictory resultVictory, SquadronMember pilotVictor) throws PWCGException
    {
        AARMissionEvaluationData evaluationData = aarContext.getMissionEvaluationData();
        LogPlane squadronMemberPlane = evaluationData.getPlaneInMissionBySerialNumber(pilotVictor.getSerialNumber());
        if (squadronMemberPlane != null)
        {
            resultVictory.setVictor(squadronMemberPlane);
            resultVictory.setConfirmed(true);
                    
            confirmedAiVictories.addVictory(resultVictory);
        }
    }
}

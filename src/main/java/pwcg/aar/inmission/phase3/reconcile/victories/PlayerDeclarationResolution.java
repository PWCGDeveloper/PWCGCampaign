package pwcg.aar.inmission.phase3.reconcile.victories;

import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;

public class PlayerDeclarationResolution
{
    private ConfirmedVictories confirmedPlayerVictories = new ConfirmedVictories();
    private AARMissionEvaluationData evaluationData;
    private Campaign campaign;
    private VictorySorter victorySorter;

    private PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
    private PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
    
    public PlayerDeclarationResolution (Campaign campaign, AARMissionEvaluationData evaluationData, VictorySorter victorySorter)
    {
        this.campaign = campaign;
        this.evaluationData = evaluationData;
        this.victorySorter = victorySorter;
    }

    public ConfirmedVictories determinePlayerAirResultsWithClaims  (PlayerDeclarations playerDeclarations) throws PWCGException 
    {
        for (PlayerVictoryDeclaration playerDeclaration : playerDeclarations.getPlayerDeclarations())
        {
            boolean resolved = false;
            
            if (!playerDeclaration.getAircraftType().equals(PlaneType.BALLOON))
            {
                resolvePlayerAircraftClaim(playerDeclaration, victorySorter);
            }
            else
            {
                resolvePlayerBalloonClaim(victorySorter, playerDeclaration, resolved);
            }
        }

        PlayerVictoryReassigner playerVictoryReassigner = new PlayerVictoryReassigner(campaign);
        playerVictoryReassigner.resetUnclamedPlayerVictoriesForAssignmentToOthers(victorySorter);
        
        return confirmedPlayerVictories;
    }

    private void resolvePlayerAircraftClaim(PlayerVictoryDeclaration playerDeclaration, VictorySorter victorySorter) throws PWCGException
    {        
        boolean hasBeenConfirmed = resolveAsFirmVictory( playerDeclaration, victorySorter);
   
        if (!hasBeenConfirmed)
        {
            hasBeenConfirmed = resolveAsFirmVictoryNotExact(playerDeclaration, victorySorter);
        }
        
        if (!hasBeenConfirmed)
        {
            hasBeenConfirmed = resolveAsFuzzyVictory(playerDeclaration, victorySorter);
        }
        
        if (!hasBeenConfirmed)
        {
            hasBeenConfirmed = resolveAsFuzzyVictoryNotExact(playerDeclaration, victorySorter);
        }
    }

    private boolean resolveAsFirmVictory(PlayerVictoryDeclaration playerDeclaration, VictorySorter victorySorter) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFirmAirVictories())
        {
            if (!resultVictory.isConfirmed())
            {
                String shotDownPlaneDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(campaign.getPlayer(), playerDeclaration, resultVictory);
                if (!shotDownPlaneDisplayName.isEmpty())
                {
                    generatePlayerVictoryIfNotAlreadyConfirmed(playerDeclaration, resultVictory, shotDownPlaneDisplayName);
                    return true;
                }
            }
        }
        
        return false;
    }

    private boolean resolveAsFirmVictoryNotExact(PlayerVictoryDeclaration playerDeclaration, VictorySorter victorySorter) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFirmAirVictories())
        {
            String shotDownPlaneDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(playerDeclaration, resultVictory);
            if (!shotDownPlaneDisplayName.isEmpty())
            {
                generatePlayerVictoryIfNotAlreadyConfirmed(playerDeclaration, resultVictory, shotDownPlaneDisplayName);
                return true;
            }
        }
        
        return false;
    }

    private boolean resolveAsFuzzyVictory(PlayerVictoryDeclaration playerDeclaration, VictorySorter victorySorter) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFuzzyAirVictories())
        {
            String shotDownPlaneDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzy(playerDeclaration, resultVictory);
            if (!shotDownPlaneDisplayName.isEmpty())
            {
                generatePlayerVictoryIfNotAlreadyConfirmed(playerDeclaration, resultVictory, shotDownPlaneDisplayName);
                return true;
            }
        }
        
        return false;
    }

    private boolean resolveAsFuzzyVictoryNotExact(PlayerVictoryDeclaration playerDeclaration, VictorySorter victorySorter) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFuzzyAirVictories())
        {
            String shotDownPlaneDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(playerDeclaration, resultVictory);
            if (!shotDownPlaneDisplayName.isEmpty())
            {
                generatePlayerVictoryIfNotAlreadyConfirmed(playerDeclaration, resultVictory, shotDownPlaneDisplayName);
                return true;
            }
        }
        
        return false;
    }

    private void resolvePlayerBalloonClaim(VictorySorter victorySorter, PlayerVictoryDeclaration playerDeclaration, boolean resolvedByFirmMatch) throws PWCGException
    {
        resolveAsFirmBalloonVictory(playerDeclaration, victorySorter);
        if (!resolvedByFirmMatch)
        {
            resolvedByFirmMatch = resolveAsFuzzyBalloonVictory(playerDeclaration, victorySorter);
        }
    }

    private boolean resolveAsFirmBalloonVictory(PlayerVictoryDeclaration playerDeclaration, VictorySorter victorySorter) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFirmBalloonVictories())
        {
            if (resultVictory.getVictor() instanceof LogPlane)
            {
                LogPlane victorPlanePlane = (LogPlane)resultVictory.getVictor();
                if (PlayerVictoryResolver.isPlayerVictory(campaign.getPlayer(), victorPlanePlane.getSerialNumber()))
                {
                    generatePlayerVictoryIfNotAlreadyConfirmed(playerDeclaration, resultVictory, resultVictory.getVictim().getVehicleType());
                    return true;
                }
            }
        }
        
        return false;
    }

    private boolean resolveAsFuzzyBalloonVictory(PlayerVictoryDeclaration playerDeclaration, VictorySorter victorySorter) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFuzzyBalloonVictories())
        {
            generatePlayerVictoryIfNotAlreadyConfirmed(playerDeclaration, resultVictory, resultVictory.getVictim().getVehicleType());
            return true;
        }
        
        return false;
    }

    private void generatePlayerVictoryIfNotAlreadyConfirmed(PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory, String shotDownPlaneName) throws PWCGException
    {
        if (!resultVictory.isConfirmed())
        {
            playerDeclaration.confirmDeclaration(true, shotDownPlaneName);
    
            LogPlane playerPlane = evaluationData.getPlaneInMissionBySerialNumber(campaign.getPlayer().getSerialNumber());
            if (playerPlane != null)
            {
                resultVictory.setVictor(playerPlane);
                resultVictory.setConfirmed(true);
        
                confirmedPlayerVictories.addVictory(resultVictory);
            }
        }
    }

    public void setClaimResolverFirm(PlayerClaimResolverFirm claimResolverFirm)
    {
        this.claimResolverFirm = claimResolverFirm;
    }

    public void setClaimResolverFuzzy(PlayerClaimResolverFuzzy claimResolverFuzzy)
    {
        this.claimResolverFuzzy = claimResolverFuzzy;
    }
}

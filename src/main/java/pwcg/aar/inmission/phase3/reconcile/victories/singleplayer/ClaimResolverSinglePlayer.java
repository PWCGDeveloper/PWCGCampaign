package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictoryEventHandler;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;

public class ClaimResolverSinglePlayer implements IClaimResolver
{
    private Campaign campaign;
    private VerifiedVictoryGenerator verifiedVictoryGenerator;
    private ClaimDenier claimDenier;
    private ReconciledMissionVictoryData reconciledMissionData = new ReconciledMissionVictoryData();
    private Map<Integer, PlayerDeclarations> playerDeclarations;

    public ClaimResolverSinglePlayer(
                    Campaign campaign,
                    VerifiedVictoryGenerator verifiedVictoryGenerator,
                    ClaimDenier claimDenier,
                    Map<Integer, PlayerDeclarations> playerDeclarations) 
    {        
        this.campaign = campaign;
        this.verifiedVictoryGenerator = verifiedVictoryGenerator;
        this.claimDenier = claimDenier;
        this.playerDeclarations = playerDeclarations;
    }

    @Override
    public ReconciledMissionVictoryData resolvePlayerClaims() throws PWCGException
    {
        confirmVictories(playerDeclarations);
        claimsDenied(playerDeclarations);       
        return reconciledMissionData;
    }

    private void confirmVictories(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        ConfirmedVictories verifiedVictories = verifiedVictoryGenerator.createVerifiedictories(playerDeclarations);
        
        VictoryEventHandler victoryHandler = new VictoryEventHandler(campaign);
        Map<Integer, List<Victory>> victories = victoryHandler.recordVictories(verifiedVictories);
        reconciledMissionData.addVictoryAwards(victories);
    }

    private void claimsDenied(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException 
    {
        for (Integer playerSerialNumber : playerDeclarations.keySet())
        {
            PlayerDeclarations playerDeclaration = playerDeclarations.get(playerSerialNumber);
            for (PlayerVictoryDeclaration declaration : playerDeclaration.getDeclarations())
            {
                ClaimDeniedEvent claimDenied = claimDenier.determineClaimDenied(playerSerialNumber, declaration);
                if (claimDenied != null)
                {
                    reconciledMissionData.addClaimDenied(claimDenied);
                }
            }
        }
    }
}

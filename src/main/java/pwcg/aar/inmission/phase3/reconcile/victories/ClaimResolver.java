package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;

public class ClaimResolver
{
    private Campaign campaign;
    private VerifiedVictoryGenerator verifiedVictoryGenerator;
    private ClaimDenier claimDenier;
    private ReconciledVictoryData reconciledMissionData = new ReconciledVictoryData();

    public ClaimResolver(
                    Campaign campaign,
                    VerifiedVictoryGenerator verifiedVictoryGenerator,
                    ClaimDenier claimDenier) 
    {        
        this.campaign = campaign;
        this.verifiedVictoryGenerator = verifiedVictoryGenerator;
        this.claimDenier = claimDenier;
    }
    
    public ReconciledVictoryData resolvePlayerClaims(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
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

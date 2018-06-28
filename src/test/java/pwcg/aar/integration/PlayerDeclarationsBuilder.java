package pwcg.aar.integration;

import pwcg.aar.inmission.phase3.reconcile.victories.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.PlayerVictoryDeclaration;

public class PlayerDeclarationsBuilder
{
    private PlayerDeclarations playerDeclarations = new PlayerDeclarations();
    
    public PlayerDeclarationsBuilder ()
    {
    }
    
    public PlayerDeclarations makePlayerDeclarations()
    {
        PlayerVictoryDeclaration victoryDeclaration1 = new PlayerVictoryDeclaration();
        victoryDeclaration1.setAircraftType("yak1s69");

        PlayerVictoryDeclaration victoryDeclaration2 = new PlayerVictoryDeclaration();
        victoryDeclaration2.setAircraftType("il2m41");
        
        playerDeclarations.addPlayerDeclaration(victoryDeclaration1);
        playerDeclarations.addPlayerDeclaration(victoryDeclaration2);

        return playerDeclarations;
    }
}

package pwcg.aar;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerVictoryDeclaration;
import pwcg.campaign.squadmember.SquadronMember;

public class PlayerDeclarationsBuilder
{    
    public PlayerDeclarationsBuilder ()
    {
    }
    
    public Map<Integer, PlayerDeclarations> makePlayerDeclarations(SquadronMember player, String playerShotDownPlane1, String playerShotDownPlane2)
    {
        Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
        PlayerDeclarations playerDeclarationSet = new PlayerDeclarations();
        PlayerVictoryDeclaration victoryDeclaration1 = new PlayerVictoryDeclaration();
        victoryDeclaration1.setAircraftType(playerShotDownPlane1);

        PlayerVictoryDeclaration victoryDeclaration2 = new PlayerVictoryDeclaration();
        victoryDeclaration2.setAircraftType(playerShotDownPlane2);
        
        playerDeclarationSet.addDeclaration(victoryDeclaration1);
        playerDeclarationSet.addDeclaration(victoryDeclaration2);

        playerDeclarations.put(player.getSerialNumber(), playerDeclarationSet);
        return playerDeclarations;
    }
}

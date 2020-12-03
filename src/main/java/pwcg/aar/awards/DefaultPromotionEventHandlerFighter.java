package pwcg.aar.awards;

public class DefaultPromotionEventHandlerFighter extends PromotionEventHandlerFighter
{
    public DefaultPromotionEventHandlerFighter()
    {
        PilotRankMedVictories = 1; // Number of victories to advance pilot Rank
                                   // from low to medium
        PilotRankHighMinVictories = 3; // Number of victories to advance pilot
                                       // Rank from medium to high
        PilotRankExecVictories = 8; // Number of victories to advance pilot Rank
                                    // from high to exec
        PilotRankCommandVictories = 12; // Number of victories to advance pilot
                                        // Rank from exec to command

        PilotRankMedMinMissions = 20; // Number of missions to advance pilot
                                      // Rank from low to medium
        PilotRankHighMinMissions = 40; // Number of victories to advance pilot
                                       // Rank from medium to high
        PilotRankExecMinMissions = 70; // Number of victories to advance pilot
                                       // Rank from medium to exec
        PilotRankCommandMinMissions = 90; // Number of victories to advance
                                          // pilot Rank from exec to command
    }
}

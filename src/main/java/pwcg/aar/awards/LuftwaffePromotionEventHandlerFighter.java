package pwcg.aar.awards;

public class LuftwaffePromotionEventHandlerFighter extends PromotionEventHandlerFighter
{
    public LuftwaffePromotionEventHandlerFighter()
    {
        PilotRankMedVictories = 3; // Number of victories to advance pilot Rank
                                   // from low to medium
        PilotRankHighMinVictories = 15; // Number of victories to advance pilot
                                       // Rank from medium to high
        PilotRankExecVictories = 25; // Number of victories to advance pilot Rank
                                    // from high to exec
        PilotRankCommandVictories = 35; // Number of victories to advance pilot
                                        // Rank from exec to command

        PilotRankMedMinMissions = 40; // Number of missions to advance pilot
                                      // Rank from low to medium
        PilotRankHighMinMissions = 90; // Number of victories to advance pilot
                                       // Rank from medium to high
        PilotRankExecMinMissions = 120; // Number of victories to advance pilot
                                       // Rank from medium to exec
        PilotRankCommandMinMissions = 150; // Number of victories to advance
                                          // pilot Rank from exec to command
    }
}

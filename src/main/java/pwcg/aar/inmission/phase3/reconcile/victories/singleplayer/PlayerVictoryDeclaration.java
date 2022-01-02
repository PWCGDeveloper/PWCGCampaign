package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.campaign.crewmember.Victory;

public class PlayerVictoryDeclaration
{
	private String aircraftType = "";
	private int victoryType = Victory.AIRCRAFT;
	private boolean confirmed = false;

    public PlayerVictoryDeclaration ()
    {
        
    }

	public PlayerVictoryDeclaration copy()
	{
	    PlayerVictoryDeclaration cloneVictoryDeclaration = new PlayerVictoryDeclaration();
        cloneVictoryDeclaration.aircraftType = aircraftType;
        cloneVictoryDeclaration.victoryType = victoryType;
        cloneVictoryDeclaration.confirmed = confirmed;
        
        return cloneVictoryDeclaration;
	}

    public String getAircraftType()
    {
        return aircraftType;
    }

    public void setAircraftType(String type)
    {
        this.aircraftType = type;
    }

    public int getVictoryType()
    {
        return victoryType;
    }

    public void setVictoryType(int victoryType)
    {
        this.victoryType = victoryType;
    }

    public boolean isConfirmed()
    {
        return confirmed;
    }

    public void confirmDeclaration(boolean confirmed, String shotDownPlaneName)
    {
        this.confirmed = confirmed;
        this.aircraftType = shotDownPlaneName;
    }
}

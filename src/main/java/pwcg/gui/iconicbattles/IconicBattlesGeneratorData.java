package pwcg.gui.iconicbattles;

import pwcg.mission.ground.vehicle.VehicleDefinition;

public class IconicBattlesGeneratorData
{
    private String iconicBattleName = "";
    private int selectedSquadron = 0;
    private String selectedVehicleOrSquadron = "";
    private VehicleDefinition playerVehicleDefinition = null;

    public String getIconicBattleName()
    {
        return iconicBattleName;
    }

    public void setIconicBattleName(String iconicBattleKey)
    {
        this.iconicBattleName = iconicBattleKey;
    }

    public int getSelectedSquadron()
    {
        return selectedSquadron;
    }

    public void setSelectedSquadron(int selectedSquadron)
    {
        this.selectedSquadron = selectedSquadron;
    }

    public String getSelectedVehicleOrSquadron()
    {
        return selectedVehicleOrSquadron;
    }

    public void setSelectedVehicleOrSquadron(String selectedVehicleOrSquadron)
    {
        this.selectedVehicleOrSquadron = selectedVehicleOrSquadron;
    }

    public VehicleDefinition getPlayerVehicleDefinition()
    {
        return playerVehicleDefinition;
    }

    public void setPlayerVehicleDefinition(VehicleDefinition playerVehicleDefinition)
    {
        this.playerVehicleDefinition = playerVehicleDefinition;
    }

}
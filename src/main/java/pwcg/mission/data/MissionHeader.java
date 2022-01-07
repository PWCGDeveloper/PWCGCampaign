package pwcg.mission.data;

public class MissionHeader
{
    private String missionFileName = "";
    private String company = "";
    private String date = "";
    private String time = "";
    private String vehicleType = "";
    private String mapName;
    private String base = "";
    private String duty = "";

    public String getSquadron()
    {
        return company;
    }

    public void setSquadron(String squadron)
    {
        this.company = squadron;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getMapName()
    {
        return mapName;
    }

    public void setMapName(String mapName)
    {
        this.mapName = mapName;
    }

    public String getBase()
    {
        return base;
    }

    public void setBase(String base)
    {
        this.base = base;
    }

    public String getDuty()
    {
        return duty;
    }

    public void setDuty(String duty)
    {
        this.duty = duty;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public String getMissionFileName()
    {
        return missionFileName;
    }

    public void setMissionFileName(String missionFileName)
    {
        this.missionFileName = missionFileName;
    }
}

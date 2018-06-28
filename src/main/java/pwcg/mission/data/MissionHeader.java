package pwcg.mission.data;

public class MissionHeader
{
    private String missionFileName = "";
    private String squadron = "";
    private String date = "";
    private String time = "";
    private String aircraftType = "";
    private String mapName;
    private String airfield = "";
    private String duty = "";
    private int altitude = 100;

    public String getSquadron()
    {
        return squadron;
    }

    public void setSquadron(String squadron)
    {
        this.squadron = squadron;
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

    public String getAirfield()
    {
        return airfield;
    }

    public void setAirfield(String airfield)
    {
        this.airfield = airfield;
    }

    public String getDuty()
    {
        return duty;
    }

    public void setDuty(String duty)
    {
        this.duty = duty;
    }

    public int getAltitude()
    {
        return altitude;
    }

    public void setAltitude(int altitude)
    {
        this.altitude = altitude;
    }

    public String getAircraftType()
    {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType)
    {
        this.aircraftType = aircraftType;
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

package pwcg.campaign.crewmember;

import java.util.Date;

/**
 * Victory is was is stored in the campaign file.
 * A victory object can be created from a MissionResultVictory (from the logs)
 * or from straight forward IO of a victory object (See VictoryIO)
 * 
 * @author Patrick Wilson
 *
 */
public class Victory implements Comparable<Victory>
{
    public static final int AIRCRAFT = 1;
    public static final int VEHICLE = 2;
    public static final int UNSPECIFIED_VICTORY = 3;

    private Date date = null;
    private String location = "";
    private boolean crashedInSight = false;

    private VictoryEntity victim = new VictoryEntity();
    private VictoryEntity victor = new VictoryEntity();

    public Victory()
    {
    }
 
    @Override
    public int compareTo(Victory otherVictory)
    {
        if (this.date.before(otherVictory.date))
        {
            return -1;
        }
        else if (this.date.after(otherVictory.date))
        {
            return 1;
        }
        return 0;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public VictoryEntity getVictim()
    {
        return victim;
    }

    public void setVictim(VictoryEntity victim)
    {
        this.victim = victim;
    }

    public VictoryEntity getVictor()
    {
        return victor;
    }

    public void setVictor(VictoryEntity victor)
    {
        this.victor = victor;
    }

    public boolean isCrashedInSight()
    {
        return crashedInSight;
    }

   public void setCrashedInSight(boolean crashedInSight)
    {
        this.crashedInSight = crashedInSight;
    }

    public boolean getCrashedInSight()
    {
        return crashedInSight;
    }
}

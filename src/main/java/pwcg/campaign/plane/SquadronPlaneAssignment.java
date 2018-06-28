package pwcg.campaign.plane;

import java.util.Date;

public class SquadronPlaneAssignment implements Cloneable
{
    protected String type = "";
    protected Date squadronIntroduction;
    protected Date squadronWithdrawal;

    public SquadronPlaneAssignment()
    {
    }

    public SquadronPlaneAssignment copy()
    {
        SquadronPlaneAssignment planeAssignment = new SquadronPlaneAssignment();
        
        planeAssignment.type = this.type;
        planeAssignment.squadronIntroduction = this.squadronIntroduction;
        planeAssignment.squadronWithdrawal = this.squadronWithdrawal;

        return planeAssignment;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Date getSquadronIntroduction()
    {
        return squadronIntroduction;
    }

    public void setSquadronIntroduction(Date introduction)
    {
        this.squadronIntroduction = introduction;
    }

    public Date getSquadronWithdrawal()
    {
        return squadronWithdrawal;
    }

    public void setSquadronWithdrawal(Date withdrawal)
    {
        this.squadronWithdrawal = withdrawal;
    }
}

package pwcg.campaign.plane;

import java.util.Date;

public class SquadronPlaneAssignment implements Cloneable
{
    protected String archType = "";
    protected Date squadronIntroduction;
    protected Date squadronWithdrawal;

    public SquadronPlaneAssignment()
    {
    }

    public SquadronPlaneAssignment copy()
    {
        SquadronPlaneAssignment planeAssignment = new SquadronPlaneAssignment();
        
        planeAssignment.archType = this.archType;
        planeAssignment.squadronIntroduction = this.squadronIntroduction;
        planeAssignment.squadronWithdrawal = this.squadronWithdrawal;

        return planeAssignment;
    }

    public String getArchType()
    {
        return archType;
    }

    public void setArchType(String archType)
    {
        this.archType = archType;
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

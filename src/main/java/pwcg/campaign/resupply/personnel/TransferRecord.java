package pwcg.campaign.resupply.personnel;

import pwcg.campaign.crewmember.CrewMember;

public class TransferRecord
{
    private CrewMember crewMember;
    private int transferFrom;
    private int transferTo;

    public TransferRecord(CrewMember crewMember, int transferFrom, int transferTo)
    {
        this.crewMember  = crewMember;
        this.transferFrom  = transferFrom;
        this.transferTo  = transferTo;
    }
    
    public CrewMember getCrewMember()
    {
        return crewMember;
    }

    public int getTransferFrom()
    {
        return transferFrom;
    }

    public int getTransferTo()
    {
        return transferTo;
    }

    public void setCrewMember(CrewMember crewMember)
    {
        this.crewMember = crewMember;
    }
}

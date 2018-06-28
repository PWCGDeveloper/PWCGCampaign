package pwcg.aar.outofmission.phase2.transfer;

import pwcg.campaign.squadmember.SquadronMember;

public class TransferRecord
{
    private SquadronMember squadronMember;
    private int transferFrom;
    private int transferTo;

    public TransferRecord(SquadronMember squadronMember, int transferFrom, int transferTo)
    {
        this.squadronMember  = squadronMember;
        this.transferFrom  = transferFrom;
        this.transferTo  = transferTo;
    }
    
    public SquadronMember getSquadronMember()
    {
        return squadronMember;
    }

    public int getTransferFrom()
    {
        return transferFrom;
    }

    public int getTransferTo()
    {
        return transferTo;
    }
}

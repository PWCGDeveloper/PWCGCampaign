package pwcg.aar.inmission.phase2.logeval.missionresultentity;

public abstract class LogBase
{
	protected int sequenceNum;
	
	public LogBase(int sequenceNum)
	{
	    this.sequenceNum = sequenceNum;
	}

	public int getSequenceNum() {
		return sequenceNum;
	}
}

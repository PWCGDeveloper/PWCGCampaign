package pwcg.core.constants;

public enum Callsign {
	NONE(0),

	STORK(1),
	GANNET(2),
	RAVEN(3),
	ROOK(4),
	THRUSH(5),
	CRANE(6),
	FINCH(7),
	ORIOLE(8),
	CANARY(9),
	SWAN(10),
	KITTIWAKE(11),
	EAGLE(12),
	PELICAN(13),
	SWIFT(14),
	REDSHANK(15),
	DUCK(16),
	PHEASANT(17),
	SEAGULL(18),
	BRAMBLING(19),
	HAWK(20),

	ACACIA(21),
	BEECH(22),
	ELM(23),
	OAK(24),
	HORNBEAM(25),
	SPRUCE(26),
	JASMINE(27),
	WILLOW(28),
	MAPLE(29),
	LINDEN(30),
	MAGNOLIA(31),
	ALDER(32),
	FIR(33),
	ROWAN(34),
	PINE(35),
	THUJA(36),
	PISTACHIO(37),
	MULBERRY(38),
	EUCALYPTUS(39),
	ASH(40),

	STORM(41),
	TYPHOON(42),
	HURRICANE(43),
	CYCLONE(44),
	VOLCANO(45);

	private Integer callsignNum;

	private Callsign(final Integer num)
	{
		callsignNum = num;
	}

	public Integer getNum()
	{
		return callsignNum;
	}

	public String toString()
	{
		return this.name().substring(0,1) + this.name().substring(1).toLowerCase();
	}
}

package pwcg.mission.flight.recon;

import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.recon.ReconFlight.ReconFlightTypes;

public interface IReconFlight extends IFlight{

	ReconFlightTypes getReconFlightType();

}
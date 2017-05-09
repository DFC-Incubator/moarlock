/**
 * 
 */
package org.irods.jargon.moarlock;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.service.AbstractJargonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mcc
 *
 */
public class MoarlockServiceImpl extends AbstractJargonService {

	private final AnalysisParams analysisParams;
	public static final Logger log = LoggerFactory.getLogger(MoarlockServiceImpl.class);

	/**
	 * @param irodsAccessObjectFactory
	 * @param irodsAccount
	 */
	public MoarlockServiceImpl(final IRODSAccessObjectFactory irodsAccessObjectFactory, final IRODSAccount irodsAccount,
			final AnalysisParams analysisParams) {
		super(irodsAccessObjectFactory, irodsAccount);
		if (analysisParams == null) {
			throw new IllegalArgumentException("null analysisParams");
		}
		this.analysisParams = analysisParams;
	}

	public void processAnalysis() throws JargonException {
		log.info("processAnalysis()");
		log.info("using configuration:{}", analysisParams);

		stageOutputFiles();
		markOutputWithTrackingData();
		stageMetadata();

	}

	private void stageMetadata() {
		// TODO Auto-generated method stub

	}

	private void markOutputWithTrackingData() {
		// TODO Auto-generated method stub

	}

	private void stageOutputFiles() {
		// TODO Auto-generated method stub

	}

}

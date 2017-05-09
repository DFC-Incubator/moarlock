/**
 * 
 */
package org.irods.jargon.moarlock;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSFileSystem;
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
		log.info("stageMetadata()");

	}

	private void markOutputWithTrackingData() {
		log.info("markOutputWithTrackingData()");

	}

	private void stageOutputFiles() {
		log.info("stageOutputFiles()");

	}

	/**
	 * run processor
	 * 
	 * @param args
	 * 
	 * 
	 *            <ul>
	 *            <li>arg0=outputdir</li>
	 *            <li>arg1=host</li>
	 *            <li>arg2=port</li>
	 *            <li>arg3=zone</li>
	 *            <li>arg4=user</li>
	 *            <li>arg5=password</li>
	 *            <li>arg6=guid</li>
	 *            <li>arg7=parm1</li>
	 *            <li>arg8=parm2</li>
	 *            <li>arg9=parm3</li>
	 * 
	 *            </ul>
	 * 
	 * 
	 * @throws JargonException
	 */
	public static void main(final String[] args) throws JargonException {

		if (args.length == 0) {
			log.error("no args provided");
			throw new JargonException("no args provided");
		}

		final AnalysisParams analysisParams = new AnalysisParams();
		final IRODSAccount irodsAccount = null;
		final IRODSFileSystem irodsFileSystem = IRODSFileSystem.instance();
		// fill in with args
		final MoarlockServiceImpl moarlockServiceImpl = new MoarlockServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount, analysisParams);
		moarlockServiceImpl.processAnalysis();

	}

}

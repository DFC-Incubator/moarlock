/**
 * 
 */
package org.irods.jargon.moarlock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.CollectionAO;
import org.irods.jargon.core.pub.DataTransferOperations;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.domain.AvuData;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.service.AbstractJargonService;
import org.irods.jargon.core.utils.LocalFileUtils;
import org.irods.jargon.datautils.metadatamanifest.MetadataManifestProcessor;
import org.irods.jargon.datautils.metadatamanifest.MetadataManifestProcessorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mcc
 *
 */
public class MoarlockServiceImpl extends AbstractJargonService {

	private final AnalysisParams analysisParams;
	public static final Logger log = LoggerFactory.getLogger(MoarlockServiceImpl.class);
	public static final String MD_MANIFEST_FILE = "mdmanifest.json";

	public static final String MOARLOCK_UNIT = "iRODS:Analysis";
	public static final String MOARLOCK_PARAM_UNIT = "iRODS:Analysis:Param";

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

	}

	private void stageMetadata(final File metadataManifest) throws JargonException {
		log.info("stageMetadata()");

		try {
			final String json = LocalFileUtils.fileContentsAsString(metadataManifest);
			final MetadataManifestProcessor processor = new MetadataManifestProcessorImpl(
					this.getIrodsAccessObjectFactory(), this.getIrodsAccount());
			processor.processManifest(processor.stringJsonToMetadataManifest(json));
		} catch (final IOException e) {
			log.error("io exception reading json", e);
			throw new JargonException("error reading json manifest", e);
		}
	}

	/**
	 * Take available analysisParams and record metadata
	 * 
	 * @throws JargonException
	 */
	private void markOutputWithTrackingData() throws JargonException {
		log.info("markOutputWithTrackingData()");
		final List<AvuData> bulkData = new ArrayList<>();
		bulkData.add(AvuData.instance("GUID", analysisParams.getGUID(), MOARLOCK_UNIT));
		bulkData.add(AvuData.instance("USER", irodsAccount.getUserName(), MOARLOCK_UNIT));
		bulkData.add(AvuData.instance("DATE_OF_ANALYSIS", new Date().toGMTString(), MOARLOCK_UNIT));
		bulkData.add(AvuData.instance("INPUT", analysisParams.getInputMount(), MOARLOCK_UNIT));
		final Set<Object> keys = analysisParams.getParams().keySet();
		for (final Object key : keys) {
			bulkData.add(
					AvuData.instance((String) key, (String) analysisParams.getParams().get(key), MOARLOCK_PARAM_UNIT));
		}

		final CollectionAO collectionAO = this.getIrodsAccessObjectFactory().getCollectionAO(getIrodsAccount());
		log.info("adding params:{}", bulkData);
		log.info("to iRODS file:{}", analysisParams.getOutputMount());
		collectionAO.addBulkAVUMetadataToCollection(analysisParams.getOutputMount(), bulkData);

	}

	private void stageOutputFiles() throws JargonException {
		log.info("stageOutputFiles()");
		final IRODSFile irodsAnalysisDir = this.getIrodsAccessObjectFactory().getIRODSFileFactory(getIrodsAccount())
				.instanceIRODSFile(analysisParams.getOutputMount());
		irodsAnalysisDir.mkdirs();
		final File localOutput = new File(analysisParams.getInputMount());
		File mdManifestFile = null;
		if (localOutput.exists()) {
			log.error("cannot find output directory for app:{}", localOutput);
			throw new JargonException("cannot find local output to stage");
		}

		final DataTransferOperations dto = this.getIrodsAccessObjectFactory()
				.getDataTransferOperations(getIrodsAccount());

		// TODO: consider recursive ops, right now just do flat

		for (final File file : localOutput.listFiles()) {
			log.info("file:{}", file);
			if (file.getName().equals(MD_MANIFEST_FILE)) {
				log.info("found mdManifest...park until all files processed");
				mdManifestFile = file;
			} else {
				log.info("staging file");
				dto.putOperation(file, irodsAnalysisDir, null, null);
			}
		}

		if (mdManifestFile != null) {
			log.info("have manifest, stage metadata");
			stageMetadata(mdManifestFile);

		}

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

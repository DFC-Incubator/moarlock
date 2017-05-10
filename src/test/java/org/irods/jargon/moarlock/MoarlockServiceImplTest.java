package org.irods.jargon.moarlock;

import java.io.File;
import java.util.Properties;
import java.util.UUID;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.pub.io.IRODSFileFactory;
import org.irods.jargon.core.utils.LocalFileUtils;
import org.irods.jargon.datautils.metadatamanifest.MetadataManifest;
import org.irods.jargon.datautils.metadatamanifest.MetadataManifest.Action;
import org.irods.jargon.datautils.metadatamanifest.MetadataManifest.FailureMode;
import org.irods.jargon.datautils.metadatamanifest.MetadataManifestOperation;
import org.irods.jargon.datautils.metadatamanifest.MetadataManifestProcessor;
import org.irods.jargon.datautils.metadatamanifest.MetadataManifestProcessorImpl;
import org.irods.jargon.testutils.AssertionHelper;
import org.irods.jargon.testutils.IRODSTestSetupUtilities;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.irods.jargon.testutils.filemanip.FileGenerator;
import org.irods.jargon.testutils.filemanip.ScratchFileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MoarlockServiceImplTest {

	private static Properties testingProperties = new Properties();
	private static TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	private static ScratchFileUtils scratchFileUtils = null;
	public static final String IRODS_TEST_SUBDIR_PATH = "MoarlockServiceImplTest";
	private static IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static AssertionHelper assertionHelper = null;
	private static IRODSFileSystem irodsFileSystem;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
		scratchFileUtils = new ScratchFileUtils(testingProperties);
		scratchFileUtils.clearAndReinitializeScratchDirectory(IRODS_TEST_SUBDIR_PATH);
		irodsTestSetupUtilities = new IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		assertionHelper = new AssertionHelper();
		irodsFileSystem = IRODSFileSystem.instance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@After
	public void afterEach() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Test
	public void testProcessAnalysis() throws Exception {

		final String rootCollection = "testProcessAnalysis";
		final String localCollectionAbsolutePath = scratchFileUtils
				.createAndReturnAbsoluteScratchPath(IRODS_TEST_SUBDIR_PATH + '/' + rootCollection);

		final String irodsCollectionRootAbsolutePath = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(testingProperties,
						IRODS_TEST_SUBDIR_PATH + "/" + rootCollection);

		FileGenerator.generateManyFilesAndCollectionsInParentCollectionByAbsolutePath(localCollectionAbsolutePath,
				"coll", 1, 1, 1, "testFile", ".txt", 2, 2, 1, 2);

		final IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);

		final IRODSFileFactory irodsFileFactory = irodsFileSystem.getIRODSFileFactory(irodsAccount);
		final IRODSFile destFile = irodsFileFactory.instanceIRODSFile(irodsCollectionRootAbsolutePath);

		final AnalysisParams analysisParams = new AnalysisParams();
		analysisParams.setGUID(UUID.randomUUID().toString());
		analysisParams.setInputMount(localCollectionAbsolutePath + "/" + "colllvl1nbr0");
		analysisParams.setOutputMount(irodsCollectionRootAbsolutePath);
		final Properties props = new Properties();
		props.put("prop1", "val1");
		props.put("props2", "val2");
		analysisParams.setParams(props);
		final MoarlockServiceImpl moarlockService = new MoarlockServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount, analysisParams);
		final MetadataManifest manifest = new MetadataManifest();
		manifest.setFailureMode(FailureMode.FAIL_FAST);
		manifest.setParentIrodsTargetPath(irodsCollectionRootAbsolutePath);
		final MetadataManifestOperation op = new MetadataManifestOperation();
		op.setAction(Action.ADD);
		op.setAttribute("atr1");
		op.setValue("val1");
		op.setIrodsPath("testFile0.txt");

		manifest.getOperation().add(op);
		final MetadataManifestProcessor processor = new MetadataManifestProcessorImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);

		final String json = processor.metadataManifestToJson(manifest);
		final File manifestFile = new File(analysisParams.getInputMount() + "/" + "mdmanifest.json");
		LocalFileUtils.stringToFile(manifestFile, json);
		moarlockService.processAnalysis();

	}

}

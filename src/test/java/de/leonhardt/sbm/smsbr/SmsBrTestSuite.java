package de.leonhardt.sbm.smsbr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.leonhardt.sbm.core.service.MessageConverterService;
import de.leonhardt.sbm.core.service.MessageIOService;

/**
 * Test suite for SMS Backup & Restore modules
 * <ul>
 * <li>{@link MessageIOService} implementation: {@link SmsBrIO}</li>
 * <li>{@link MessageConverterService} implementation: {@link SmsBrConverter}</li>
 * </ul>
 * 
 * @author Frederik Leonhardt
 *
 */
@RunWith(Suite.class)
@SuiteClasses({SmsBrIOTest.class, SmsBrConverterTest.class})
public class SmsBrTestSuite {

}

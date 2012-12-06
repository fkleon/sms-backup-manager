package de.leonhardt.sbm;

import static org.junit.Assert.*;

import javax.swing.ImageIcon;

import org.junit.BeforeClass;
import org.junit.Test;

import de.leonhardt.sbm.gui.resource.FlagLoader;

public class FlagLoaderTest {

	private static FlagLoader fl;
	
	@BeforeClass
	public static void setUp() {
		fl = new FlagLoader();
	}
	//TODO
	@Test
	public void testLoadValidFlag() {
		ImageIcon flag;
		String countryCode = "DE";
		
		flag = fl.getFlag(countryCode);
		
		assertNotNull(flag);
		assertNotNull(flag.getDescription());
		assertTrue("Expected german flag.",countryCode.toLowerCase().equals(flag.getDescription().toLowerCase()));
	}
	
	@Test
	public void testLoadInvalidFlag() {
		ImageIcon flag;

		flag = fl.getFlag("ZIZ");
		
		assertNotNull(flag);
		assertNotNull(flag.getDescription());
		assertTrue("Expected default flag.","default".equals(flag.getDescription().toLowerCase()));
	}
	
}

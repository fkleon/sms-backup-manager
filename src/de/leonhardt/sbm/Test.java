package de.leonhardt.sbm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;


import de.leonhardt.sbm.store.MessageStore;
import de.leonhardt.sbm.util.MapUtil;
import de.leonhardt.sbm.util.Utils;
import de.leonhardt.sbm.util.comparator.MessageDateComparator;
import de.leonhardt.sbm.xml.model.Contact;
import de.leonhardt.sbm.xml.model.Sms;
import de.leonhardt.sbm.xml.model.Smses;

public class Test {

	public static void main(String[] args) throws Exception {
		String[] paths = {	"sms-20121011143146.xml", 
				 			"sms-20121013230111.xml", 
				 			"sms-20121015220445.xml",
				 			"sms-20121103115131.xml",
				 			"sms-20121203092757.xml",
				 			"sms-20121204104031.xml"};
		
		String[] paths2 = { "fixtures/sms-dupes.xml",
							//"test.xml"
							};
		
		BackupManager bm = new BackupManager();
		MessageIO smsio = new MessageIO(true);
		
		for (String s: paths) {
			Smses smses = smsio.readFromXML(s);
			bm.importMessages(smses);
		}

		//smsio.writeToXML(smses, path2);
		
		/*
		int i = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		for (Sms s: smses.getSms()) {
			Date d = new Date(s.getDate());
			System.out.println((i++) + ": " + sdf.format(d));
		}
		*/
		
		System.out.println(bm);
		
		System.out.println("Size before: "+ bm.getMS().size());
		//Map<Sms, Integer> dups = ms.findDuplicates();
		//dumpMap(dups);
		Collection<Sms> cleared = bm.getMS().clearDuplicates();
		
		System.out.println("Size after: "+ cleared.size());
		
		System.out.println(bm);

		for (Entry<Contact, MessageStore> entry: bm.getCS().entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().size() + " messages.");
		}
		
		
		ArrayList<Sms> clearedList = new ArrayList<Sms>(cleared);
		Smses smses = new Smses(clearedList);
		
		Collections.sort(smses.getSms(), new MessageDateComparator());
		
		smsio.writeToXML(smses, "test.xml");
	}
	
	public static void dumpMap(Map<?, ?> map) {
		System.out.println("Dumping map with size " + map.size() + "..");
		for (Entry<?, ?> e: map.entrySet()) {
			System.out.println(e.getKey() + ": " + e.getValue());
		}
		System.out.println("Dumped map with size " + map.size() + ".");
	}
	
}

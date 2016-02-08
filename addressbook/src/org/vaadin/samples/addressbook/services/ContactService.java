package org.vaadin.samples.addressbook.services;

import java.util.*;
import org.apache.commons.beanutils.BeanUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.samples.addressbook.model.Contact;

public class ContactService {
	// dummy data
	static String[] fnames = { "Peter", "Alice", "John", "Mike", "Olivia", "Nina", "Alex", "Rita", "Dan", "Umberto",
			"Henrik", "Rene", "Lisa", "Linda", "Timothy", "Daniel", "Brian", "George", "Scott", "Jennifer" };

	static String[] lnames = { "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore",
			"Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Young", "King",
			"Robinson" };

	private static ContactService instance;

	public static ContactService createService() {
		if (instance == null) {

			final ContactService contactService = new ContactService();

			Random r = new Random(0);
			Calendar cal = Calendar.getInstance();
			for (int i = 0; i < 100; i++) {
				Contact contact = new Contact();
				contact.setFirstName(fnames[r.nextInt(fnames.length)]);
				contact.setLastName(lnames[r.nextInt(fnames.length)]);
				contact.setEmail(
						contact.getFirstName().toLowerCase() + "@" + contact.getLastName().toLowerCase() + ".com");
				contact.setPhone("+ 358 555 " + (100 + r.nextInt(900)));
				cal.set(1930 + r.nextInt(70), r.nextInt(11), r.nextInt(28));
				contact.setBirthDate(cal.getTime());
				contactService.save(contact);
			}
			instance = contactService;
		}

		return instance;
	}

	private HashMap<Long, Contact> contacts = new HashMap<>();
	private long nextId = 0;

	public synchronized List<Contact> findAll(String stringFilter) {
		ArrayList<Contact> arrayList = new ArrayList<Contact>();
		for (Contact contact : contacts.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(ContactService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		Collections.sort(arrayList, new Comparator<Contact>() {
			@Override
			public int compare(Contact o1, Contact o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized long count() {
		return contacts.size();
	}

	public synchronized void delete(Contact value) {
		contacts.remove(value.getId());
	}

	public synchronized void save(Contact entry) {
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Contact) BeanUtils.cloneBean(entry);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		contacts.put(entry.getId(), entry);
	}
}

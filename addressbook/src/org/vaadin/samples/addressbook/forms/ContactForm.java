package org.vaadin.samples.addressbook.forms;

import org.vaadin.samples.addressbook.AddressbookUI;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.samples.addressbook.model.Contact;

public class ContactForm extends FormLayout {
	private static final long serialVersionUID = -2605514860380210001L;
	
	Button save = new Button("Save", this::save);
	Button cancel = new Button("Cancel", this::cancel);
	TextField firstName = new TextField("First Name");
	TextField lastName = new TextField("Last Name");
	TextField phone = new TextField("Phone");
	TextField email = new TextField("Email");
	DateField birthDate = new DateField("Birth Date");
	
	Contact contact;
	
	BeanFieldGroup<Contact> formFieldBindings;
	
	public ContactForm() {
		configure();
		buildLayout();
	}
	
	private void buildLayout() {
		setSizeUndefined();
		setMargin(true);
		
		HorizontalLayout actions = new HorizontalLayout(save, cancel);
		actions.setSpacing(true);
		addComponents(actions, firstName, lastName, phone, email, birthDate);
	}

	private void configure() {
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		setVisible(false);
	}
	
	public void cancel(Button.ClickEvent event){
		Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
		getUI().contactList.select(null);
	}
	
	public void save(Button.ClickEvent event){
		try{
			formFieldBindings.commit();
			getUI().service.save(contact);
			String message = String.format("Saved '%s %s'.", contact.getFirstName(), contact.getLastName());
			Notification.show(message, Type.TRAY_NOTIFICATION);
			getUI().refreshContacts();
		} catch(FieldGroup.CommitException ex){
			// validation exception related processing goes here...
		}
	}
	
	public void edit(Contact contact){
		this.contact = contact;
		if(contact != null){
			formFieldBindings = BeanFieldGroup.bindFieldsBuffered(contact, this);
			firstName.focus();
		}
		setVisible(contact != null);
	}
	
	@Override
	public AddressbookUI getUI(){
		return (AddressbookUI) super.getUI();
	}
}

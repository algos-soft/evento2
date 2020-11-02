package it.algos.evento.ui.company;

import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.LoginEvent;
import it.algos.webbase.web.login.LoginListener;
import it.algos.webbase.web.login.UserIF;

public class CompanyLogin extends VerticalLayout {

	public CompanyLogin() {

		super();

		createUI();


	}

	private void createUI(){

		setWidth("100%");
		setHeight("100%");

		// horizontal: spacer left + image + spacer right
		HorizontalLayout logoLayout = new HorizontalLayout();
		logoLayout.setWidth("100%");
		addSpacer(logoLayout);
		Resource res=(LibResource.getImgResource("splash_image.png"));
		if (res!=null) {
			Image img = LibImage.getImage(res);
			logoLayout.addComponent(img);
		}
		addSpacer(logoLayout);

		// vertical: spacer top + image layout + button panel + spacer bottom
		addSpacer(this);
		addComponent(logoLayout);
		addSpacer(this, 0.2f);
		addComponent(createButtonLayout());
		addSpacer(this, 0.2f);
		addSpacer(this);
	}

	private Component createButtonLayout(){
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		addSpacer(layout);
		layout.addComponent(createLoginButton());
		addSpacer(layout);
		return layout;
	}


	private Button createLoginButton(){
		Button button=new Button("Login");
		button.setStyleName("loginbutton");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				// attacco il listener al Login
				// (lo faccio qui perché l'oggetto Login potrebbe
				// essere annullato a causa di un login fallito,
				// quindi non posso farlo una volta sola alla costruzione della GUI)
				Login.getLogin().setLoginListener(new LoginListener() {
					@Override
					public void onUserLogin(LoginEvent e) {
						doLogin();
					}
				});

				Login.getLogin().showLoginForm();
			}
		});
		return button;
	}

	/**
	 * Aggiunge a un layout una label con larghezza espandibile al 100%
	 */
	private void addSpacer(AbstractOrderedLayout layout){
		addSpacer(layout, 1.0f);
	}

	/**
	 * Aggiunge a un layout una label con larghezza espandibile
	 */
	private void addSpacer(AbstractOrderedLayout layout, float ratio){
		Label label = new Label();
		layout.addComponent(label);
		layout.setExpandRatio(label, ratio);
	}

	private void doLogin(){

		// registra la company nella sessione in base all'utente loggato
		UserIF user = Login.getLogin().getUser();
		boolean success= CompanySessionLib.registerCompanyByUser(user);

		if(success){
			UI.getCurrent().setContent(new CompanyHome());
		}else{
			CompanySessionLib.setCompany(null);
			CompanySessionLib.setLogin(null);
			Notification.show("L'utente "+user+" è registrato ma non c'è l'azienda corrispondente.\nContattateci per creare la vostra azienda.", Notification.Type.ERROR_MESSAGE);
		}

	}



}

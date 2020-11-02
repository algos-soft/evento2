package it.algos.evento.test;

import com.vaadin.ui.Notification;
import it.algos.evento.demo.DemoDataGenerator;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StressTest implements Runnable {

	@Override
	public void run() {
		//Company comp=EventoApp.COMPANY;
		BaseCompany comp= CompanySessionLib.getCompany();
		if (comp!=null) {
			ArrayList<Prenotazione> prenotazioni = testCreate();
			testRead();
			testModify();
		}else{
			Notification.show("Per eseguire lo stress test occorre avere una Company attiva.", Notification.TYPE_ERROR_MESSAGE);
		}
		//testDelete(prenotazioni);
	}
	
	// test create
	private ArrayList<Prenotazione> testCreate(){
		ArrayList<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
		EntityManager manager = EM.createEntityManager();
		manager.getTransaction().begin();
		try {
			prenotazioni.addAll(DemoDataGenerator.creaPrenotazioni(null, manager));
			manager.getTransaction().commit();
		}catch (Exception e){
			manager.getTransaction().rollback();
		}
		manager.close();
		return prenotazioni;
	}
	
	// test delete
	private void testDelete(ArrayList<Prenotazione> prenotazioni){
		for(Prenotazione pren : prenotazioni){
			System.out.println("delete -> "+pren);
			pren.delete();
		}
	}


	
	// test read
	private void testRead(){
		int iterations = 2000;
		List<Prenotazione> lista = (List<Prenotazione>) CompanyQuery.getList(Prenotazione.class);
		for(int i=0; i<iterations; i++){
			Prenotazione pren = (Prenotazione)getEntityRandom(lista);
			System.out.println("read "+i+" -> "+pren);
		}
	}

	
	// test modify
	private void testModify(){
		int iterations = 200;
		List<Prenotazione> lista = (List<Prenotazione>)CompanyQuery.getList(Prenotazione.class);
		for(int i=0; i<iterations; i++){
			Prenotazione pren = (Prenotazione)getEntityRandom(lista);
			pren.setTelRiferimento(""+i);
			pren.save();
			System.out.println("modify "+i+"  -> "+pren);
		}
		
	}
	
	private static BaseEntity getEntityRandom(List lista){
	    int min = 0;
	    int max = lista.size()-1;
	    int randomNum = new Random().nextInt((max - min) + 1) + min;
	    return (BaseEntity)lista.get(randomNum);
	}


	

}

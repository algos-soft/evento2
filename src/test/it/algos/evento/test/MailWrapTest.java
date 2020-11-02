package it.algos.evento.test;

import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.mailing.DestWrap;
import it.algos.evento.entities.mailing.MailWrap;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by gac on 14 lug 2015.
 * Test
 */
public class MailWrapTest {

    // alcuni valori di riferimento
    private static String TITOLO = "Mailing pasquale";
    private static String INDIRIZZO = "gac@algos.it";
    private static String INDIRIZZO2 = "alex@algos.it";
    private static String INDIRIZZO3 = "gac@algos.it";
    private static String CHIAVE = " nome ";
    private static String VALORE = " Guido ";
    private static String CHIAVE2 = " cognome ";
    private static String VALORE2 = " Ceresa ";

    // alcuni parametri utilizzati
    private String sorgente = "";
    private String previsto = "";
    private String ottenuto = "";

    @Test
    public void testDestWrap() {
        DestWrap wrap = null;
        wrap = new DestWrap(INDIRIZZO, CHIAVE, VALORE);

        ottenuto = wrap.getIndirizzo();
        assertEquals(ottenuto, INDIRIZZO);

        ottenuto = wrap.getMappa().get(CHIAVE);
        assertEquals(ottenuto, VALORE);
    }// end of single test

    @Test
    public void testDestWrap2() {
        DestWrap wrap = null;
        HashMap<String, String> mappa = new HashMap<String, String>();
        mappa.put(CHIAVE, VALORE);

        wrap = new DestWrap(INDIRIZZO, mappa);

        ottenuto = wrap.getIndirizzo();
        assertEquals(ottenuto, INDIRIZZO);

        ottenuto = wrap.getMappa().get(CHIAVE);
        assertEquals(ottenuto, VALORE);
    }// end of single test

    @Test
    public void testDestWrap3() {
        DestWrap wrap = null;
        HashMap<String, String> mappa = new HashMap<String, String>();
        mappa.put(CHIAVE, VALORE);
        mappa.put(CHIAVE2, VALORE2);

        wrap = new DestWrap(INDIRIZZO, mappa);

        ottenuto = wrap.getIndirizzo();
        assertEquals(ottenuto, INDIRIZZO);

        ottenuto = wrap.getMappa().get(CHIAVE);
        assertEquals(ottenuto, VALORE);

        ottenuto = wrap.getMappa().get(CHIAVE2);
        assertEquals(ottenuto, VALORE2);
    }// end of single test

    @Test
    public void testDestWrap4() {
        DestWrap wrap = null;
        Object mappa;

        wrap = new DestWrap(INDIRIZZO);

        ottenuto = wrap.getIndirizzo();
        assertEquals(ottenuto, INDIRIZZO);

        mappa = wrap.getMappa();
        assertNull(mappa);
    }// end of single test

    @Test
    public void testMailtWrap() {
        Lettera lettera = new Lettera();
        MailWrap wrapper = null;
        DestWrap wrap = new DestWrap(INDIRIZZO, CHIAVE, VALORE);
        ArrayList<DestWrap> lista = new ArrayList<DestWrap>();
        boolean destinatariUnici;
        Lettera letteraOttenuta;
        ArrayList<DestWrap> listaOttenuta;

        lista.add(wrap);
        wrapper = new MailWrap(TITOLO, lettera, lista);

        destinatariUnici = wrapper.isDestinatariUnici();
        assertEquals(destinatariUnici, true);

        ottenuto = wrapper.getTitolo();
        assertEquals(ottenuto, TITOLO);

        letteraOttenuta = wrapper.getLettera();
        assertEquals(letteraOttenuta, lettera);

        listaOttenuta = wrapper.getLista();
        assertEquals(listaOttenuta, lista);
    }// end of single test

    @Test
    public void testMailtWrap2() {
        MailWrap wrapper = null;
        ArrayList<DestWrap> lista = new ArrayList<DestWrap>();
        boolean destinatariUnici;
        Lettera letteraOttenuta;
        ArrayList<DestWrap> listaOttenuta;

        lista.add(new DestWrap(INDIRIZZO, CHIAVE, VALORE));
        lista.add(new DestWrap(INDIRIZZO2, CHIAVE, VALORE));
        lista.add(new DestWrap(INDIRIZZO3, CHIAVE, VALORE));
        wrapper = new MailWrap(TITOLO, null, lista);

        destinatariUnici = wrapper.isDestinatariUnici();
        assertEquals(destinatariUnici, true);

        letteraOttenuta = wrapper.getLettera();
        assertNull(letteraOttenuta);

        listaOttenuta = wrapper.getLista();
        assertEquals(lista.size(), 3);
        assertEquals(listaOttenuta.size(), 2);
    }// end of single test

}// end of test class
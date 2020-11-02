package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.mailing.MailingModulo;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.importexport.ExportManager;
import it.algos.webbase.web.importexport.ExportProvider;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import java.math.BigDecimal;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class PrenotazioneTablePortal extends TablePortal {

    public PrenotazioneTablePortal(ModulePop modulo) {
        super(modulo);

        TableToolbar toolbar = getToolbar();

        // bottone Altro...
        MenuBar.MenuItem item = toolbar.addButton("Altro...", FontAwesome.BARS, null);

        item.addItem("Mostra prenotazioni scadute", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPrenotazioniScadute();
                Container.Filterable cont = getTable().getFilterableContainer();
                cont.removeAllContainerFilters();
                getTable().refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

        item.addItem("Mostra conferme pagamento scadute", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPagamentiDaConfermare();
                Container.Filterable cont = getTable().getFilterableContainer();
                cont.removeAllContainerFilters();
                getTable().refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class


        item.addItem("Mostra prenotazioni congelate", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPrenCongelate();
                Container.Filterable cont = getTable().getFilterableContainer();
                cont.removeAllContainerFilters();
                getTable().refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

        item.addItem("Mostra prenotazioni con pagamento non ricevuto", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPrenPagamentoNonRicevuto();
                Container.Filterable cont = getTable().getFilterableContainer();
                cont.removeAllContainerFilters();
                getTable().refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class


        item.addSeparator();

        item.addItem(Prenotazione.CMD_RIEPILOGO_OPZIONE, Prenotazione.ICON_RIEPILOGO_OPZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedEntity() != null) {
                    getPrenotazioneTable().inviaRiepilogoPrenotazione();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class

        item.addItem(Prenotazione.CMD_MEMO_INVIO_SCHEDA_PREN, Prenotazione.ICON_MEMO_INVIO_SCHEDA_PREN, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedEntity() != null) {
                    getPrenotazioneTable().inviaMemoConfermaPren();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class

        item.addItem(Prenotazione.CMD_MEMO_SCAD_PAGA, Prenotazione.ICON_MEMO_SCAD_PAGA, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedEntity() != null) {
                    getPrenotazioneTable().inviaPromemoriaScadenzaPagamento();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class

        item.addItem(Prenotazione.CMD_ATTESTATO_PARTECIPAZIONE, Prenotazione.ICON_ATTESTATO_PARTECIPAZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedEntity() != null) {
                    getPrenotazioneTable().inviaAttestatoPartecipazione();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class


        item.addItem(Prenotazione.CMD_CONFERMA_PRENOTAZIONE, Prenotazione.ICON_CONFERMA_PRENOTAZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                getPrenotazioneTable().confermaPrenotazione();
            }
        });// end of anonymous class

        item.addItem(Prenotazione.CMD_REGISTRA_PAGAMENTO, Prenotazione.ICON_REGISTRA_PAGAMENTO, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                getPrenotazioneTable().registraPagamento();
            }
        });// end of anonymous class


        item.addItem(Prenotazione.CMD_CONGELA_OPZIONE, Prenotazione.ICON_CONGELA_OPZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedEntity() != null) {
                    getPrenotazioneTable().congelaPrenotazione();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class


        item.addItem(Prenotazione.CMD_SPOSTA_AD_ALTRA_DATA, Prenotazione.ICON_SPOSTA_AD_ALTRA_DATA, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                BaseEntity[] entities = getTable().getSelectedEntities();
                if ((entities != null) && (entities.length > 0)) {
                    getPrenotazioneTable().spostaAdAltraData();
                } else {
                    Notification.show("Seleziona prima le prenotazioni da spostare.");
                }
            }
        });// end of anonymous class


        item.addSeparator();


        item.addItem(Prenotazione.CMD_EXPORT, Prenotazione.ICON_EXPORT, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {

                Container container = getTable().getContainerDataSource();
                ExportProvider provider = new PrenExportProvider();
                ExportConfiguration conf = new ExportConfiguration(Prenotazione.class, "prenotazioni.xls", container, provider);
                new ExportManager(conf, PrenotazioneTablePortal.this).show();

//                PrenExportManager exportManager=new PrenExportManager(conf, PrenotazioneTablePortal.this);
//
//                /// inject the totals
//                PrenExportSource source=exportManager.getExportSource();
//                source.setTotImporto(getTotImporto());
//                source.setTotPosti(getTotPosti());
//
//                // show the export manager
//                exportManager.show();

            }
        });


        if (LibSession.isDeveloper()) {
            item.addItem(Prenotazione.CMD_GESTIONE_MAILING, Prenotazione.ICON_GESTIONE_MAILING, new MenuBar.Command() {
                public void menuSelected(MenuItem selectedItem) {
                    Object[] selected = getTable().getSelectedIds();
                    ArrayList<Long> selezionati = getSelIds(selected);
                    if (selected != null) {
                        MailingModulo.gestioneMailing(selezionati, getUI());
                    } else {
                        msgNoSelection();
                    }
                }
            });
        }


        item.addItem("Test lettera", null, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                BaseEntity entity = getTable().getSelectedEntity();
                if (entity != null) {
                    Prenotazione pren = (Prenotazione) entity;
                    PrenotazioneModulo.testLettera(pren);
                } else {
                    msgNoSelection();
                }
            }
        });


//        item.addItem("Test local storage", null, new MenuBar.Command() {
//            public void menuSelected(MenuItem selectedItem) {
//                test();
//            }
//        });


        // questa tabella ha il bottone Opzioni
        toolbar.setOptionsButtonVisible(true);


    }// end of method


    private int getTotPosti(){
        BigDecimal bd = getPrenotazioneTable().getTotalForColumnNoFilter(Prenotazione_.numTotali.getName());
        return bd.intValue();
    }

    private float getTotImporto(){
        BigDecimal bd = getPrenotazioneTable().getTotalForColumnNoFilter(Prenotazione_.importoDaPagare.getName());
        return bd.floatValue();
    }


//    private void test() {
//
//        LocalStorage s = LocalStorage.get();
//        s.put("k1", "value1", new LocalStorageCallback() {
//            @Override
//            public void onSuccess(String value) {
//                int a = 87;
//            }
//
//            @Override
//            public void onFailure(FailureEvent error) {
//                int a = 87;
//            }
//        });
//        s.put("k2", "value2");
//        s.put("k3", "value3");
//
//        LocalStorage.detectValue("k1",
//                new LocalStorageCallback() {
//                    public void onSuccess(String value) {
//                        Notification.show("Value received:" + value);
//                    }
//
//                    public void onFailure(FailureEvent error) {
//                        Notification.show("Value retrieval failed: " + error.getMessage());
//                    }
//                });
//
//
//    }


    /**
     * Ritorna la table specifica
     */
    private PrenotazioneTable getPrenotazioneTable() {
        PrenotazioneTable pTable = null;
        ATable table = getTable();
        if (table != null && table instanceof PrenotazioneTable) {
            pTable = (PrenotazioneTable) table;
        }
        return pTable;
    }

    /**
     * Gli oggetti selezionati sono SEMPRE dei valori Long
     * Trasformo in un array di long, più facile da gestire
     */
    private ArrayList<Long> getSelIds(Object[] selected) {
        ArrayList<Long> selezionati = null;

        if (selected != null && selected.length > 0) {
            selezionati = new ArrayList<Long>();

            for (Object obj : selected) {
                if (obj instanceof Long) {
                    selezionati.add((long) obj);
                }// fine del blocco if
            } // fine del ciclo for-each

        }// fine del blocco if

        return selezionati;
    }// end of method

    private void msgNoSelection() {
        Notification.show("Seleziona prima una prenotazione.");
    }// end of method

}

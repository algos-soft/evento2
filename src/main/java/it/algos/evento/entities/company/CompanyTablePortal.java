package it.algos.evento.entities.company;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import it.algos.evento.daemons.PrenChecker;
import it.algos.evento.demo.DemoDataGenerator;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.Toolbar;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class CompanyTablePortal extends TablePortal {

    public CompanyTablePortal(ModulePop modulo) {
        super(modulo);

        Toolbar toolbar = getToolbar();

        // bottone Altro...
        MenuItem item = toolbar.addButton("Altro...", FontAwesome.BARS, null);


        // crea nuova azienda e utente
        item.addItem("Attiva nuova azienda", FontAwesome.HOME, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {

//                Company company = new Company();
                ActivateCompanyForm form = new ActivateCompanyForm(getModule(), null);
                form.addFormListener(new AForm.FormListener() {
                    @Override
                    public void cancel_() {
                        form.getWindow().close();
                    }

                    @Override
                    public void commit_() {
                        form.getWindow().close();
                        BaseCompany company = (BaseCompany)form.getEntity();
                        CompanyService.activateCompany(company, form.getPassword(), form.isCreateData());
                        getTable().refresh();
                    }
                });

                Window window = new Window("Attivazione azienda", form);
                window.setResizable(false);

                form.setHeightUndefined();
                window.center();
                UI.getCurrent().addWindow(window);


            }
        });

        item.addSeparator();

        // crea dati demo
        item.addItem("Crea dati demo", FontAwesome.GEARS, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {

                BaseCompany company = (BaseCompany)getTable().getSelectedEntity();
                if (company != null) {
                    ConfirmDialog dialog = new ConfirmDialog("Crazione dati","Confermi la creazione dei dati demo per l'azienda "+company+"?",new ConfirmDialog.Listener() {
                        @Override
                        public void onClose(ConfirmDialog dialog, boolean confirmed) {
                            if(confirmed){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DemoDataGenerator.createDemoData(company);
                                        Notification.show("Creazione dati demo completata per l'azienda "+company+".");
                                    }
                                }).start();
                            }
                        }
                    });
                    dialog.show(UI.getCurrent());
                } else {
                    msgNoSelection();
                }

            }
        });

        item.addItem("Cancella dati azienda", FontAwesome.TRASH_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                BaseCompany company = (BaseCompany)getTable().getSelectedEntity();
                if (company != null) {
                    ConfirmDialog dialog = new ConfirmDialog("Eliminazione dati","Confermi l'eliminazione di tutti dati dell'azienda "+company+"?",new ConfirmDialog.Listener() {
                        @Override
                        public void onClose(ConfirmDialog dialog, boolean confirmed) {
                            if(confirmed){
                                company.deleteAllData();
                                Notification.show("Tutti i dati di " + company + " sono stati cancellati.");
                            }
                        }
                    });
                    dialog.show(UI.getCurrent());
                } else {
                    msgNoSelection();
                }

            }
        });// end of anonymous class


        item.addItem("Esegui PrenChecker per l'azienda selezionata", FontAwesome.GEARS, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                BaseCompany company = (BaseCompany)getTable().getSelectedEntity();
                if (company != null) {
                    ConfirmDialog dialog = new ConfirmDialog("Controllo prenotazioni","Vuoi eseguire il controllo prenotazioni per l'azienda "+company+"?<br>(Attenzione, potrebbe inviare i solleciti e congelare delle prenotazioni!)",new ConfirmDialog.Listener() {
                        @Override
                        public void onClose(ConfirmDialog dialog, boolean confirmed) {
                            if(confirmed){
                                PrenChecker checker = new PrenChecker(LibDate.today());
                                checker.run(company);
                            }
                        }
                    });
                    dialog.show(UI.getCurrent());
                } else {
                    msgNoSelection();
                }

            }
        });// end of anonymous class



    }// end of method

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
        Notification.show("Seleziona prima una azienda.");
    }// end of method

}

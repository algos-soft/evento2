package it.algos.evento.entities.company;


import it.algos.webbase.domain.company.BaseCompanyModule;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.table.TablePortal;

/**
 * Company module specifico
 */
public class CompanyModule extends BaseCompanyModule {

    public CompanyModule() {
        super(Company.class);
    }

    public TablePortal createTablePortal() {
        return new CompanyTablePortal(this);
    }

    /**
     * Create button pressed in table
     * <p>
     * Create a new item and edit it in a form
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void create() {

        String msg = "<b>Normalmente le aziende si creano tramite la funzione Altro->Attiva nuova azienda.</b><p>";
        msg+="Se crei una azienda manualmente dovrai anche creare l'utente e i dati.<br>";
        msg+="Vuoi continuare?<br>";

        ConfirmDialog dialog = new ConfirmDialog("Creazione azienda", msg, new ConfirmDialog.Listener() {
            @Override
            public void onClose(ConfirmDialog dialog, boolean confirmed) {
                if(confirmed){
                    CompanyModule.super.create();
                }
            }
        });

        dialog.show();

    }// end of method



}


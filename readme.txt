Build -> Build Project
effettua la build, rigenera target/, rigenera out/
cancellare /out per essere sicuri che rigeneri tutto

fullDeploy.sh
copia gli artifacts nella directory webapps/evento/ del tomcat installato sul sistema
(non occorre restartare tomcat)

tomcat-start.sh e tomcat-stop.sh
avviano e stoppano il tomcat di sistema

Per un redeploy completo su macchina di sviluppo:
=============================
- delete /out
- Build Project
- fullDeploy.sh


Per il deploy in produzione:
=============================

1) su macchina di sviluppo:
- cd /opt/apache-tomcat-8.5.23/webapps
- zip -r evento.zip evento/
- con FileZilla: copiare evento.zip su server produzione OVH2 in /opt/tomcat/webapps

2) su server produzione:
- ssh root@54.37.157.241
- cd /opt/tomcat/webapps
- stoppare evento su tomcat manager
- rm -rf evento
- unzip evento.zip
- chown -R tomcat:tomcat evento/
- rm -rf evento.zip
- riavviare evento su tomcat manager (se non è già partito da solo)

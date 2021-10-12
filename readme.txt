Build -> Build Project
effettua la build, rigenera target/, rigenera out/
cancellare /out per essere sicuri che rigeneri tutto

fullDeploy.sh
copia gli artifacts nella directory webapps/evento/ del tomcat installato sul sistema
(non occorre restartare tomcat)

tomcat-start.sh e tomcat-stop.sh
avviano e stoppano il tomcat di sistema

Quindi per un redeploy completo:
- delete /out
- Build Project
- fullDeploy.sh
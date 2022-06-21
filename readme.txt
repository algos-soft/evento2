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


When setting up a new environment:
=============================
- certified Mysql server version: 5.7.29
- certified Tomcat version: Apache Tomcat/8.5.23
- check that Tomcat is running with the correct Java version (Java 8)
Tomcat Manager -> Server Status -> JVM Version should read something
like "1.8.0_332-internal-b09"
- catalog "evento" must be present in MySql

Maven HTTP repositories problem
--------------------------------
Maven blocks external HTTP repositories by default since version 3.8.1
(see https://maven.apache.org/docs/3.8.1/release-notes.html)

add this <mirror> to ~/.m2/settings.xml
(also add the whole .xml file if not present)

<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <mirrors>
        <mirror>
            <id>maven-default-http-blocker</id>
            <mirrorOf>dummy</mirrorOf>
            <name>Dummy mirror to override default blocking mirror that blocks http</name>
            <url>http://0.0.0.0/</url>
        </mirror>
    </mirrors>

</settings>




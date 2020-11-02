xcopy /E /I /Y webapp C:\Apps\apache-tomcat-8.5.23\webapps\evento

cd C:\Apps\apache-tomcat-8.5.23\bin
call C:\Apps\apache-tomcat-8.5.23\bin\catalina.bat stop

cd C:/Users/avalbonesi/IdeaProjects/evento
call waitForPort8080free.bat

cd C:\Apps\apache-tomcat-8.5.23\bin
call catalina.bat jpda run

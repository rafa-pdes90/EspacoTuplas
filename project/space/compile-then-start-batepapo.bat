javac -cp ..\..\lib\jini-core.jar;..\..\lib\jini-ext.jar;..\..\lib\reggie.jar;..\..\lib\outrigger.jar;. *.java

if %errorlevel% == 0 (
	java -cp ..\..\lib\jini-core.jar;..\..\lib\jini-ext.jar;..\..\lib\reggie.jar;..\..\lib\outrigger.jar;. BatePapo
)

PAUSE
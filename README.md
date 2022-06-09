<img align="right" src="https://github.com/julihuber0/Wizard/blob/master/Resources/wizardgame.png?raw=true" width="238" height="320">

# Anleitung

## Kapitel

1. [Java-Installation](#java-installation)
2. [Download](#download)
3. [Spielstart](#spielstart)
4. [Spieloberfläche](#spieloberfläche)
5. [Server-Erstellung](#erstellung-eines-servers)
6. [Sonstiges](#sonstiges)
7. [Geplante Funktionen für die nächsten Versionen](#geplante-funktionen-für-die-nächsten-versionen)

## Java-Installation

Damit die Spieldatei ausgeführt werden kann, muss Java (mindestens Java JRE 8) auf dem Computer installiert sein. Ist dies nicht der Fall, muss Java heruntergeladen und installiert werden ([Windows-Download](https://www.oracle.com/java/technologies/downloads/#jdk17-windows), [Mac-Download](https://www.oracle.com/java/technologies/downloads/#jdk17-mac)). Dabei wird für Windows der "x64 Installer MSI", für Macs mit Intel Prozessor der "x64 DMG Installer" und bei Macs mit Apple Silicon Prozessor der "ARM 64 DMG Installer" benötigt. Anschließend den Installer ausführen (bei Windows 10/11: erstes Pop Up-Fenster mit "Ja" bestätigen und dann im nächsten Fenster auf "Installieren" klicken, bei MacOs nach dem Öffnen der dmg-Datei auf den Karton im sich öffnenden Fenster doppelklicken, Sicherheitswarnung bestätigen, im nächsten Fenster das Passwort für das Benutzerkonto auf dem Mac eingeben und im anschließenden Dialog auf installieren klicken).

Wenn Wizard über die .exe Datei installiert wurde, kann Java auch über den entsprechenden Startmenü-Eintrag installiert werden!
Wichtig: Dieser Schritt muss nur einmalig ausgeführt werden!

## Download

Um das Spiel herunterzuladen, einfach dem Link folgen oder rechts auf "Releases" klicken. Dann beim oversten Release die Asset-Liste unter der Beschreibung aufklappen und die "Wizard_x.y.z.zip" herunterladen. Dieses zip-Archiv muss unter Windows noch entpackt werden. Dazu einfach Rechtsklick auf das zip-Archiv, "Alle extrahieren" und bestätigen. Im sich anschließend öffnenden Fenster kann dann die "Wizard-Game.jar"-Datei mit einem Doppelklick geöffnet werden. Alternativ wird auch bei vielen Releases für Windows eine ".exe"-Datei angeboten. Dies ist der empfohlene Weg, um die Anwendung auf Windows zu installieren. Dazu einfach die Datei herunterladen, ausführen, alle Anweisungen Schritt für Schritt befolgen und nach erfolgreicher Installation sollte sich eine Verknüpfung zum Spiel auf dem Desktop befinden.

## Spielstart

Nach erfolgreicher Java-Installation sollte sich das Symbol bei der "Wizard-Game.jar"-Datei geändert haben (falls nicht, Windows Explorer/Finder
schließen und Wizard-Verzeichnis neu aufrufen, Computer neu starten) und kann nun geöffnet werden. Wenn das Spiel 
über die .exe Datei installiert wurde, sollte eine Wizard-Verknüpfung auf dem Desktop erstellt worden sein, sowie weitere Verknüpfungen im Startmenü unter
Wizard. Das Spiel kann dann auch über diese Einträge gestartet werden. Das Spielfenster taucht nicht in der Mitte
des Bildschirms auf, um das komplette Fenster sehen zu können, kann durch ein Drücken der Alt-Taste die Maus aus dem Fenster befreit werden
und eben jenes am oberen weißen Rand korrekt verschoben werden. Wenn die Maus wieder in das Spielfenster bewegt wird, wird die Maus automatisch
wieder darin gefangen, durch ein Drücken der Alt-Taste kann diese aber immer befreit werden (Funktioniert nicht unter MacOs, hier muss über das
Trackpad oder über die Kombination "Cmd+Tab" ein anderes Fenster in den Vordergrund geholt werden, um die Maus zu befreien).

Um einem Spiel beizutreten, muss auf "Beitreten" geklickt werden. Es öffnet sich ein Fenster, in welchem die IP-Adresse eingegeben werden
muss, die der Spielleiter (Server-Host) bekannt gegeben hat (Anleitung zur Servererstellung folgt weiter unten). Wurde die Adresse richtig
eingegeben, erscheint ein weiteres Fenster, wo der Spieler seinen Namen eingeben kann. Nach der Bestätigung wird eine Liste mit allen schon
beigetretenen Spielern angezeigt.

Sobald der Spielleiter das Spiel im Server startet, beginnt das Spiel. Die Spielregeln können bei Bedarf in dem mitgelieferten PDF
nachgelesen werden.

## Spieloberfläche

Die Spieloberfläche ist wie folgt aufgebaut: Oben werden alle Mitspieler aufgereiht, samt Namen, angesagte Stiche, gemachte Stiche und
aktuelle Punktzahlen. Die eigenen Werte und der eigene Name werden links unten angezeigt. Der Spieler, der aktuell an der Reihe ist, wird
durch ein blaues Rechteck hinter dem Spieler-Avatar markiert. Die eigene Hand wird am unteren Bildschirmrand angezeigt, 
der aktuelle Stich in der Mitte, die aufgedeckte Trumpfkarte links in der Mitte. Zu Beginn jeder Runde müssen alle 
Spieler ihre Stiche ansagen, dazu erscheint wieder ein Fenster, in welchem die gewünschte Stichzahl eingegeben 
werden kann (bei fehlerhafter Eingabe, wie z.B. negative Zahlen, Buchstaben etc. erscheint das Fenster so oft wieder,
bis eine gültige Eingabe getätigt wurde). Es wurde so eingestellt, dass sich die angesagten Stiche nicht aufgehen 
dürfen, dementsprechend wird dem letzten Spieler, der seine Stiche ansagen muss, eine Einschränkung im 
Stichansage-Fenster angezeigt, die dort angezeigt Zahl wird auch als fehlerhafte Eingabe erkannt. Diese Einstellung 
kann allerdings vom Server-Host ausgeschaltet werden. Sollte als Trumpfkarte ein Zauberer aufgedeckt werden, wird 
der "Geber" durch ein weiteres Fenster dazu aufgefordert, eine Trumpffarbe einzugeben. Nachdem alle Spieler ihre 
Stiche angesagt haben, kann der Spieler, an der Reihe ist durch Anklicken einer Karte diese ausspielen. Nicht 
spielbare Karten (weil z.B. zugegeben werden muss) werden ausgegraut und können nicht angeklickt werden. Wenn alle 
Karten in den Stich gelegt wurden, wird der Spieler, der gestochen hat, kurz grün markiert und darf dann ausspielen. 
Am Ende einer Runde werden alle Spieler, die richtig angesagt haben, kurz grün markiert, die restlichen Spieler rot. Eine
genaue Spielstatistik mit allen Punkten und angesagten Stichen (vgl. Zettel) kann über die Schaltfläche "Scoreboard" ein- und wieder
ausgeblendet werden. Über das Schließen-Kreuz und den anschließenden Bestätigungsdialog kann das Spiel verlassen werden.
Am Ende des Spiels wird in einem Dialog der Gewinner angezeigt und gefragt, ob ein neues Spiel gestartet werden soll.

## Erstellung eines Servers

Um einen Server hosten zu können, muss zuerst in der Router-Konfiguration der Port 7654 für TCP und UDP freigegeben 
werden. Anschließend kann die Datei "Wizard-Server.jar" ausgeführt werden (um den Server mit Konsolenausgabe zu 
starten, die ServerStart.bat anstatt der jar-Datei ausführen). Im sich öffnenden Fenster wird im Normalfall die 
eigene öffentliche IP-Adresse angezeigt, über die dem Spiel beigetreten werden kann. Sollte diese nicht angezeigt 
werden, muss über eine Website, wie "wieistmeineip.de" die eigene IP-Adresse abgerufen werden. Über das 
Kontrollkästchen darunter kann eingestellt werden, ob sich die Stiche aufgehen dürfen oder nicht. Wenn alle Spieler 
beigetreten sind, kann das Spiel durch den Start-Button gestartet werden (das Spiel startet nicht, wenn weniger als 3 
oder mehr als 6 Spieler beigetreten sind).

## Sonstiges

- Im Ordner "Resources" sind alle Bilder gespeichert, die für das Spiel benötigt werden, dieser darf nicht gelöscht werden, da das Spiel
  sonst nicht mehr startet
- Die Spiel- oder Serverdatei darf nicht an einen anderen Ort verschoben werden, bzw. muss der "Resources"-Ordner 
  mit verschoben werden, damit die benötigten Bilder noch gefunden werden können
- Ab und zu kann es sein, dass manche Karten flackern, dies hört normalerweise zeitnah wieder auf und ändert nichts am Spielgeschehen

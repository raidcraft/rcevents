# RCEvents

Ermöglicht es "globale" Events zu erstellen die auf bestimmte Trigger reagieren können. Funktioniert im Prinzip wie Achievements nur ohne das Achievement.

Kommt aus dem Vorschlag: https://git.faldoria.de/tof/plugins/raidcraft/rcquests/issues/22

## Getting Started

Im `RCEvents` Plugin unterhalb des `events/` Ordners können neue Events als einzelne Dateien ohne ein `.event.yml` Suffix angelegt werden. Im Quest Plugin benötigen Events das `.event.yml` Suffix.
Das Event Plugin stellt im Prinzip nur einen Wrapper um alle gängigen Funktionen der [ART API](https://git.faldoria.de/tof/plugins/raidcraft/raidcraft-api/blob/master/docs/ART-API.md) bereit. Daher sollte man damit vertraut sein, bevor man Events schreibt.

Aktuell gibt es `globale` und `player` Events. Je nachdem was gewählt wird beziehen sich die Trigger in der Event Config auf alle oder nur auf den einzelnen Spieler. Wenn man z.B. einen speziellen Boss spawnen lassen möchte, sobald ein Spieler ein Gebiet betritt, sollte man ein globales Event anlegen, da sonst für jeden Spieler der gleiche Boss gespawnt wird.

Eine Event Config sieht wie folgt aus. Alle Werte hinter den Parametern sind die Default Werte.

```yml
enabled: true
# Wird einer der Trigger ausgelöst pausiert der gesamte Event für die Zeit des Cooldowns für den auslösenden Spieler.
# Oder alle Spieler wenn der Event global ist.
# Wird in 2d 30m 2s etc angegeben.
cooldown: 0
# Wenn true betrifft der Cooldown des Events alle Spieler.
# Die direkten Actions des Events betreffen aber immer nur den auslösenden Spieler.
# Requirements werden auch immer nur auf der direkten Spieler Ebene geprüft.
global: false
# Wenn 0 kann das Event unedlich ausgeführt werden.
# Ansonsten wird es nur so oft wie angegeben ausgeführt.
# Wenn das Event global ist zählt jeder ausführende Spieler den counter um eins hoch.
execution-count: 0
# Actions die ausgeführt werden wenn einer der Trigger ausgelöst wurde.
actions:
  flow: ...
# Grundlegende Requirements damit der Event für einen Spieler aktiviert wird.
# Vorsicht mit countern in globalen Events: counter werden immer nur für Spieler gezählt und nicht für alle.
# z.B. ?quest.active aufstieg-zum-tianbaum
requirements:
  flow: ...
# Eine Liste mit Triggern die den Event auslösen
# Das löst wiederum einen @event trigger aus. Der Trigger wird erst NACH der Ausführung der Event Actions ausgelöst.
# Dabei muss auf darauf achten keine Endlosschleife zu erzeugen.
# Multiple Trigger werden alle parallel registriert. Hier kann man wie in anderen ART Configs auch Requirement und Action Groups verwenden.
# Wenn man außerdem kompliziertere Trigger verwenden will sollte man auf Trigger Groups zurückgreifen.
trigger:
  flow: ...
```

### Beispiel

In folgendem Beispiel erhält der Spieler beim Betreten des Tianbaums einen Speed Buff. Sobald er ihn verlässt wird der Buff entfernt.

```yml
enabled: true
cooldown: 0
global: false
execution-count: 0
trigger:
  flow:
    - '@player.move baumeingang-unten'
    - '!cast.override this.tianbaum-segen'
    - '@player.move baumausgang-unten'
    - '!effect.remove "Segen des Tianbaum"'
    - '@player.move baumeingang-oben'
    - '!cast.override this.tianbaum-segen'
    - '@player.move baumausgang-oben'
    - '!effect.remove "Segen des Tianbaum"'
```

## Referenzen

- [**A**ctions **R**equirements **T**rigger API](https://git.faldoria.de/tof/plugins/raidcraft/raidcraft-api/blob/master/docs/ART-API.md)
- [Action und Requirement Grouppen](https://git.faldoria.de/tof/plugins/raidcraft/raidcraft-api/blob/master/docs/ART-API.md#alias-groups)
- [Trigger Gruppen](https://git.faldoria.de/tof/plugins/raidcraft/raidcraft-api/blob/master/docs/ART-API.md#alias-groups)
- [Quests](https://git.faldoria.de/tof/plugin-configs/quests/blob/develop/docs/QUEST-DEVELOPER.md)
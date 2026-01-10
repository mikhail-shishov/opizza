# Dôležitá informácia o spúštení projektu

## DB:
Meno databázy je **opizza**. 

V root priečinku projekta sú .sql súbory podľa zadania:

- create_script.sql – vytvorenie celej databázy bez dát,
- demo_data.sql – len dáta,
- db_full.sql – kombinovaný súbor obsahujúci vytvorenie DB aj vloženie dát.


## Použivatelia:

test.user@opizza.sk - obyčajný

cook@opizza.sk - kuchár

driver@opizza.sk - kurier

shishovmike96@gmail.com - admin

Heslo ku všetkým použivateľom je A1bcdefg

Ak to nejde kvôli PasswordEncoder, reset hesla funguje na http://localhost:8080/auth/forgot-password. Mail sa odosiela na môj Mailtrap, takže v projekte je **implementácia odoslania mailov**. Maily sa odosielajú v prípade zabudnutého hesla, keď použivateľ sa zaregistruje, a keď urobí objednavku.


## Maily:
https://mailtrap.io/inboxes/4278125/messages

Mailtrap login:
shishovmike96@gmail.com
A1bcdefghijk!

Ak bude žiadať kód, píšte mi hocikedy, a pošlem ho. Som dostupný na Instagrame @shishov.mike, a samozrejme odpovedám na maily mikhail.shishov@student.ukf.sk.

Alebo keď budete registrovať sa nový profil, kľudne mi napište, aby som poslal vám screenshot, že odoslanie mailov funguje.
Am folosit fisiere de tip .txt pentru fiecare clasa pentru a stoca informatiile despre utilizatori(pe cate o linie este username-ul si parola),despre comentarii(pe
fiecare linie este username-ul si id-ul postarii si  textul comentariului),despre postari(pe fiecare linie este username-ul,parola si textul postarii),
despre like-uri(pe fiecare linie este username-ul si id-ul postarii),despre like-uri la comentarii(pe fiecare linie este username-ul si id-ul comentariului),si
despre followeri(pe fiecare linie este username-ul celui care urmareste si username-ul celui urmarit).
Am tratat fiecare caz in parte,conform cerintei fiecarei functii.Mai intai verificam primul argument pentru a vedea ce tip de comanda este,apoi in functie de
aceasta rezolvam fiecare subpunct in parte.Primul pas era sa verificam daca am primit parametrii -u si -p.Dupa aceea,trebuia verificat daca acel utilizator
exista si daca parola este corecta pentru a se loga.De asemenea,daca ultimul argument specific comenzii nu era dat,puteam decat sa verificam daca utilizatorul
s-a logat si sa afisam un mesaj ca nu am primit ultimul argument.Ultimul caz pentru fiecare comanda in parte este acela in care primim toate argumentele corecte
conform enuntului.Astfel,dupa ce verificam daca s-a logat utilizatorul,putem sa facem ceea ce ne cere comanda respectiva.Si aici exista cazul in care utilizatorul
nu exista sau cazul in care ultimul argument este invalid.In cazul in care totul este corect,se executa comanda.Pentru a rezolva comenzile,parcurgem fisierele
de care avem nevoie linie cu linie si extragem informatiile necesare pentru a rezolva fiecare subpunct in parte. De exemplu,mai intai folosim fisierul utilizatori.txt
pentru a verifica utilizatorul,dupa putem folosi un al doilea fisier in care se afla informatiile despre comanda respectiva.Exista si cazuri in care au fost
parcurse 3 fisiere diferite pentru a rezolva o comanda,cum ar fi comanda pentru like-comment,deoarece verificam utilizatorul,apoi verificam daca exista comentariul
si abia la final putem sa adaugam acel like in fisierul cu like-uri la comentarii.
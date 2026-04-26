# Audi Dealership Management System

## Descriere

Acest proiect implementeaza Etapa I pentru o aplicatie Java OOP de gestiune a unui dealership auto Audi. Sistemul functioneaza exclusiv in memorie, fara baza de date, fara fisiere de persistenta, fara GUI si fara framework-uri externe.

Pachetul de baza folosit in implementare este:

`com.pao.proiect.audidealership`

## Actiuni si interogari disponibile

1. Adauga o masina Audi in inventar
2. Sterge o masina din inventar dupa VIN
3. Cauta o masina dupa VIN
4. Listeaza toate masinile din inventar
5. Listeaza toate masinile disponibile
6. Listeaza masinile sortate dupa pret
7. Marcheaza o masina ca vanduta
8. Inregistreaza un client nou
9. Sterge un client dupa ID
10. Cauta un client dupa ID
11. Cauta clienti dupa nume
12. Listeaza toti clientii
13. Creeaza o comanda pentru un client
14. Finalizeaza o vanzare
15. Listeaza comenzile unui client
16. Listeaza toate comenzile
17. Programeaza un test drive
18. Listeaza test drive-urile unui client
19. Listeaza toate test drive-urile programate

## Tipuri de obiecte modelate

1. `Vehicle`
2. `AudiCar`
3. `ElectricAudiCar`
4. `Person`
5. `Customer`
6. `Employee`
7. `SalesAgent`
8. `Order`
9. `TestDrive`
10. `Showroom`
11. `VIN`

## Organizare

Codul este organizat astfel:

```text
src/com/pao/proiect/audidealership/
  Main.java
  model/
  service/
  exception/
```

## Elemente OOP implementate

- ierarhie de mostenire: `Vehicle -> AudiCar -> ElectricAudiCar`
- ierarhie de mostenire: `Person -> Employee -> SalesAgent` si `Person -> Customer`
- interfata: `Identifiable`
- clasa abstracta: `Vehicle`
- clasa abstracta: `Person`
- clasa imutabila: `VIN`
- exceptii custom: `VehicleNotFoundException`, `CustomerNotFoundException`, `VehicleAlreadySoldException`, `InvalidVinException`

## Colectii folosite

- `List<AudiCar>` pentru inventar
- `Set<Customer>` pentru clienti
- `Map<String, AudiCar>` pentru indexare dupa VIN
- `Map<String, List<Order>>` pentru comenzile unui client
- sortare cu `Comparator` pentru masinile ordonate dupa pret

## Demonstratie

Clasa `Main` demonstreaza complet toate actiunile de mai sus:

- creeaza masini Audi, inclusiv masini electrice
- creeaza clienti si un agent de vanzari
- adauga masini in inventar
- cauta, sterge si listeaza masini
- sorteaza inventarul dupa pret
- creeaza si finalizeaza comenzi
- programeaza test drive-uri
- trateaza exceptiile custom prin `try/catch`

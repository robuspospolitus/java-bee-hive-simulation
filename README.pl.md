# Multi-Agent Bee Simulation

[![eng](https://img.shields.io/badge/lang-eng-blue.svg)](README.md)

Symulacja oparta na architekturze wieloagentowej, modelująca zachowania kolonii pszczół w ulu oraz ich interakcje ze środowiskiem zewnętrznym. Projekt został zrealizowany w języku Java i przedstawia mechanizmy autonomicznego podejmowania decyzji przez poszczególne jednostki w systemie rozproszonym.

## Funkcje systemu

* **Królowa (Queen):** Zarządza strukturą ula, monitoruje stan zasobów, kontroluje populację i koordynuje ogólny podział ról.
* **Zbieraczki (Foragers):** Odpowiadają za eksplorację mapy, lokalizowanie źródeł pyłku, efektywne zbieranie surowców oraz nawigację powrotną.
* **Magazynierzy (Storers):** Odbierają zebrany pyłek od zbieraczek przy wejściu do ula, transportują go do komór magazynowych i zarządzają zapasami.
* **Zarządzanie energią:** Każdy agent posiada ograniczony zasób energii, który zużywa się podczas wykonywania akcji (lot, praca). Spadek energii do zera wymaga powrotu do ula i regeneracji.
* **Autonomiczna nawigacja:** Implementacja algorytmów poruszania się i wyszukiwania drogi w celu optymalizacji tras pomiędzy ulem a polami kwiatowymi.

## Struktura projektu

Projekt został zaimplementowany z zachowaniem czystego podziału odpowiedzialności w oparciu o wzorzec architektoniczny MVC (Model-View-Controller) oraz system modułów Javy (JPMS). Struktura katalogów w pakiecie głównym prezentuje się następująco:

* `src/main/java/Simulation/` - główny moduł logiczny aplikacji
    * `Controller/` - warstwa pośrednicząca, odpowiadająca za kontrolę i sterowanie przebiegiem symulacji (klasa `SimulationController`).
    * `Logger/` - komponenty odpowiedzialne za rejestrowanie zdarzeń systemowych (klasa `Logger`) oraz generowanie statystyk (klasa `Statistics`).
    * `Model/` - rdzeń logiczny aplikacji, zawierający definicję świata oraz reguły zachowań
        * `Agents/` - implementacja autonomicznej logiki poszczególnych typów pszczół.
        * `BoardCells/` - struktury reprezentujące poszczególne pola i kafelki na planszy.
        * `MovementStrategies/` - algorytmy oraz strategie poruszania się agentów w przestrzeni.
        * Klasy bazowe środowiska: `Board`, `Hive` oraz klasy pomocnicze symulacji.
    * `View/` - warstwa prezentacji odpowiedzialna za interfejs graficzny użytkownika (klasy `MainWindow`, `GridBoard`).
* `src/main/resources/` - katalog zasobów, zawierający podfolder `Images` z grafikami oraz plik `styles.css` ze stylami wykorzystywanymi przez interfejs graficzny.
## Wymagania systemowe

* Java Development Kit (JDK) 17 lub nowszy
* IntelliJ IDEA (zalecane) lub dowolne inne środowisko IDE wspierające język Java
* Gradle (opcjonalnie, w projekcie znajduje się Gradle Wrapper)

## Quick Start

1. **Klonowanie repozytorium**

   Wpisz w terminalu następujące komendy, aby sklonować repozytorium na swoje urządzenie.
   ```bash
   git clone https://github.com/robuspospolitus/java-bee-hive-simulation.git
   cd java-bee-hive-simulation
   ```
2. **Kompilacja projektu**

   Projekt korzysta z mechanizmu Gradle Wrapper, dzięki czemu nie musisz mieć zainstalowanego systemu Gradle lokalnie na komputerze. Aby zbudować aplikację wpisz w terminalu:

    ```bash
    ./gradlew build
    ```

3. **Uruchomienie symulacji**

   Aby wystartować aplikację za pomocą wtyczki aplikacyjnej Gradle, wykonaj w terminalu:
    ```Bash
    ./gradlew run
    ```
## Sample Run
Po poprawnym uruchomieniu aplikacji w konsoli pojawi się log z inicjalizacji środowiska oraz dynamiczny przebieg akcji wykonywanych przez poszczególnych agentów w czasie rzeczywistym:

```plaintext
 A new log file has been created: simulation_2026-06-16_15-18-50.log
Simulation initialized
Starting simulation execution for 1 steps.
Storer 0 moves around the hive
Storer 0 random move performed, current position [10, 1]
Forager 1 is empty, full and goes through the exit to the meadow. Energy: 100.0
Forager 1 hates portals
Forager 2 heads towards the hive exit. Energy: 100.0
Forager 2 is flying to its destination. Left: 0.0
Forager 3 heads towards the hive exit. Energy: 100.0
Forager 3 is flying to its destination. Left: 1.0
The eggs were laid by the queen on 4, 7
Steps ran
Simulation run completed.
1 steps ran
```

Poza wypisywanymi akcjami w konsoli, powinien w katalogu projektu utworzyć się plik .log z dodatkowo wypisaną godziną pojawienia się poszczególnych zdarzeń:
```plaintext
[15:18:50] Simulation initialized
[15:18:50] Starting simulation execution for 1 steps.
[15:18:50] Storer 0 moves around the hive
[15:18:50] Storer 0 random move performed, current position [10, 1]
[15:18:50] Forager 1 is empty, full and goes through the exit to the meadow. Energy: 100.0
[15:18:50] Forager 1 hates portals
[15:18:50] Forager 2 heads towards the hive exit. Energy: 100.0
[15:18:50] Forager 2 is flying to its destination. Left: 0.0
[15:18:50] Forager 3 heads towards the hive exit. Energy: 100.0
[15:18:50] Forager 3 is flying to its destination. Left: 1.0
[15:18:50] The eggs were laid by the queen on 4, 7
[15:18:50] Steps ran
[15:18:50] Simulation run completed.
[15:18:50] 1 steps ran
```

Co 10 ticków symulacji dopisują się kolejne kolumny obecnych statystyk w pliku `simulation_statistics.csv`.

## Konfiguracja symulacji

Poza właściwymi zmiennymi znajdującymi się bezpośrednio w GUI aplikacji dostępnego dla użytkownika, dodatkowe parametry takie jak długość życia pszczół, zużycie energii itp. można konfigurować w pliku pod ścieżką `src/main/java/Simulation/Model/SimulationConfig.java`.
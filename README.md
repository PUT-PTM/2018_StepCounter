# 2018_StepCounter
Projekt licznika kroków na zajęcia z Podstaw Techniki Mikroprocesorowej.

## Opis:
Projekt ma na celu przesyłanie wykonanych kroków do aplikacji na telefonie za pomocą modułu bluetooth. 
Zebrane kroki sa nastepnie przeliczane na doświadczenie i umożliwiają użytkownikowi na obserwowanie swojego postępu poprzez zwiększający się poziom oraz jego wizualizacje w postaci pokemona.

## Narzędzia:
- płytka STM32F4DISCOVERY
- moduł bluetooth HC-06
- Android Studio
- System Workbench for STM32

## Konfiguracja oraz uruchomienie:

Piny modułu bluetooth należy podłączyć w następujący sposób do pinów na płytce:

Vcc -> 3.3V

GND -> GND

TxD -> PC11

RxD -> PC10

W ten sposób przygotowaną płytkę wystarczy uruchomić, a w aplikacji wybrać opcję *Connect*, która sama wyszuka pożądane urządzenie i się z nim połączy.
Swój postęp należy zapisywać za pomocą przycisku *Save*.

## Napotkane problemy:

Problem z wyborem pomiędzy 2 sposobami przesyłu kroków:

-wysyłanie sumy kroków co określony interwał czasowy,

-wysyłanie pojedyńczego zdarzenia.

Wybraliśmy rozwiązanie 2, ponieważ lepiej odzwierciedla on sposób działania pedometru oraz utrata danych w przypadku awarii płytki jest mniejsza. Rozwiązanie 2 nie wymaga przechowywania sumy kroków na płytce - pozwala to na reset płytki bez utraty postępu w aplikacji.

## Autorzy:
* Ewa Dziembowska
* Szymon Jędrzejewski

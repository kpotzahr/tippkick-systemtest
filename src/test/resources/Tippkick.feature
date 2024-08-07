@systemtest @skip @cleanData
Feature: Kompletter Ablauf von Spielplan erfassen über Wettabgabe bis zur ersten Wertung

  @ui
  Scenario: Spielplan erfassen, Wetten und Werten via UI
    Given Spielplan:
      | Spiel                 | Startzeit |
      | Deutschland-Brasilien | <nachher> |
    When Es werden folgende Tipps abgegeben:
      | Tipper    | Spiel                 | Ergebnis |
      | Ronaldo   | Deutschland-Brasilien | 0:1      |
      | KnowItAll | Deutschland-Brasilien | 7:1      |
    And Spiel "Deutschland-Brasilien" endet "7:1"
    And Es wird folgende Rangliste angezeigt:
      | Tipper    | Score |
      | KnowItAll | 3     |
      | Ronaldo   | 0     |

  Scenario: Spielplan erfassen, Wetten und Werten via API
    Given Spielplan:
      | Spiel                 | Startzeit |
      | Deutschland-Brasilien | <nachher> |
    When Es werden folgende Tipps abgegeben:
      | Tipper    | Spiel                 | Ergebnis |
      | Ronaldo   | Deutschland-Brasilien | 0:1      |
      | KnowItAll | Deutschland-Brasilien | 7:1      |
    And Spiel "Deutschland-Brasilien" endet "7:1"
    And Es wird folgende Rangliste berechnet:
      | Tipper    | Score |
      | KnowItAll | 3     |
      | Ronaldo   | 0     |

  Scenario: Nach automatischem Spielstart kann kein Tipp mehr abgegeben werden
    Given Spielplan:
      | Spiel                 | Startzeit |
      | Deutschland-Brasilien | <sofort>  |
    When "TooLate" gibt für "Deutschland-Brasilien" den Tipp "7:1" ab
    Then Der Tipp wird nicht angenommen

  Scenario: Nur zum Zeigen eines undefinierten Steps
    Given dies ist nur ein Dummy

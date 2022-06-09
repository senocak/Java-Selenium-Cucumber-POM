@Login
Feature: Login tests
  As Customer, I can login to fleetvaluationsystem.com

  @fail1
  Scenario: Enter Credentials
    Given going to "login" page
    Given Enter username and password
    | username | password  |
    | anil     | anil      |
    When Click login button
    Then I should see dashboard page

  @google
  Scenario: Deneme
    Given going to "home" page
    And User enters search text as "Cosmosboard (@cosmos.board)"
    When User clicks on Google Search button
    Then Search Results is displayed
    And Test retrieves details of "3" result
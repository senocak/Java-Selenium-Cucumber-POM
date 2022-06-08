Feature: Login tests
  As Customer B2B2B, I can login to fleetvaluationsystem.com

  Background: Home page of fleetvaluationsystem.com
    Given Open browser and go to homepage
    Given Enter username
    Given Enter password
    When Click login button
    Then I should see dashboard page

  @login
  Scenario: Enter Credentials
    Given Login page is displayed
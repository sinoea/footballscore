# Spring WebFlux Football Score API

This example shows how to build a Football Score Spring WebFlux API with real-time score updates.


## Prerequisites

+ JDK 8 or above
+ Maven 3 or above

## Getting Started

Follow the below steps to clone the repo and run the application:

1. git clone https://github.com/sinoea/footballscore.git
2. Open your terminal and navigate to the root folder of the repo (cd {Your path}/footballscore)
3. Run the application by the following command: mvn spring-boot:run
4. To see if the application is running visit the below URIs in your browser:
    - http://localhost:8080/scores (To view scores for all teams)
    - http://localhost:8080/live/scores (To stream score updates)

## Test Application
1. When you visit http://localhost:8080/scores , you will see 4 scores listed.
   To create another score, open your terminal and run the below curl command:
   curl -X POST -H 'Content-Type:application/json' http://localhost:8080/scores -d '{"id": null,"teamA": "Monaco","teamB": "Rome","teamAGoals": 10,"teamBGoals": 2}'

   Now refresh the scores page (http://localhost:8080/scores) to see the newly added score.

2. The Live Score streaming URI shows score updates in realtime.
   To test the Live Score realtime updates, first visit the URI in your browser (http://localhost:8080/live/scores).
   You need to update a score to see updates in realtime.
   To update a score , copy the ID of one of the listed scores in the scores page and run the below command (replacing {id} with the ID value you copied):
   curl -X PUT -H 'Content-Type:application/json' http://localhost:8080/scores/{id} -d '{"id": "{id}","teamA": "Henke","teamB": "Kaabi","teamAGoals": 10,"teamBGoals": 20}'

   You should now see a new record in the Live Score URI listing the updated records.
   To verify that the score has been updated, please refresh the scores page and look for the score that you updated to see the new changes.

3. To delete a score , copy the ID of one of the listed scores in the scores page and run the below command (replacing {id} with the ID value you copied):
   curl -X DELETE -H 'Content-Type:application/json' http://localhost:8080/scores/{id}

   Now refresh the scores page to verify that the score has indeed been deleted.
   
   
This completes the testing of the application that supports the GET, POST, DELETE and PUT methods for the /scores endpoint and GET for the /live/scores endpoint.

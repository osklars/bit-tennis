# Pingu
The table tennis tracker. Keeps track of the ball and the score. 

Payloads are found in `model.api.in` and `model.api.out`.
POST `/new` - start a new match with payload 

POST `/event` - send detected events

POST `/input` - send player button inputs 

GET `/state` - stream state updates
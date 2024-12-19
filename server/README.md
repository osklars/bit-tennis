# Pingu
The table tennis tracker. Keeps track of the ball and the score. 

POST `/new` - start a new match 
```json 
{
  "playerA": "Oskar",
  "playerB": "Daniel",
  "bestOf": 3,
  "firstServer": "A",
  "timestamp": 1702569432000
}
```

POST `/event` - send detected events
```json 
{
  "detection": "Racket",
  "player": "A",
  "timestamp": 1702569432000
}
```

GET `/history` - stream state updates with history
```json
[
  {
    "event": {
      "detection": {
        "detection": "Throw",
        "player": "A",
        "timestamp": 1702569432000
      },
      "input": null
    },
    "state": {
      "playerA": "Oskar",
      "playerB": "Daniel",
      "bestOf": 3,
      "set": {
        "game": {
          "rallyState": "ToServe",
          "possession": "A",
          "points": {
            "A": 0,
            "B": 0
          },
          "firstServer": "A"
        },
        "points": {
          "A": 0,
          "B": 0
        },
        "firstServer": "A"
      }
    }
  },
  {
    "event": {
      "detection": null,
      "input": {
        "input": "NewMatch",
        "player": null,
        "timestamp": 1702569432000
      }
    },
    "state": {
      "playerA": "Oskar",
      "playerB": "Daniel",
      "bestOf": 3,
      "set": {
        "game": {
          "rallyState": "Idle",
          "possession": "A",
          "points": {
            "A": 0,
            "B": 0
          },
          "firstServer": "A"
        },
        "points": {
          "A": 0,
          "B": 0
        },
        "firstServer": "A"
      }
    }
  }
]
```
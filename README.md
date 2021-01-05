# ManzBot :beer:
A beerful bot for Manz!

## Running :running:
`gradle run`

## Building :construction_worker:
`gradle build`

## Config :book:
Example of config.json file, must be under `bin/configs/config.json` for **prod** or under `app/configs/config.json` for **dev**

```json
{
  "webdriver": {
    "path": "drivers/geckodriver"
  },
  "tradePlatform": {
    "version": "5",
    "credentials": {
      "user": "awesomeuser",
      "password": "awesomepassword",
      "server": "awesomeserver"
    },
    "symbols": {
      "show": ["Crypto"],
      "hide": ["Forex"]
    },
    "maxActionDeltaTime": 30
  },
  "investers": {
    "assets": [
      {
        "asset": {
          "id": "1",
          "code":"EURUSD"
        },
        "parameters": {
          "volumeQuantity": 0.01,
          "tpDelta": 0.00060
        },
        "technicalSettings": {
          "timeFrames": [60, 300, 900],
          "indicators": {
            "buy": ["Buy", "Strong Buy"],
            "sell": ["Sell", "Strong Sell"]
          }
        },
        "timePeriod": 300
      },
      {
        "asset": {
          "id": "945629",
          "code":"BTCUSD"
        },
        "parameters": {
          "volumeQuantity": 0.01,
          "tpDelta": 70.0
        },
        "technicalSettings": {
          "timeFrames": [60, 300, 900],
          "indicators": {
            "buy": ["Buy", "Strong Buy"],
            "sell": ["Sell", "Strong Sell"]
          }
        },
        "timePeriod": 300
      }
    ]
  }
}
```

## Drivers :carousel_horse:
Ony Firefox browser is supported. Geckodriver can be found [here](https://github.com/mozilla/geckodriver/releases/tag/v0.28.0)

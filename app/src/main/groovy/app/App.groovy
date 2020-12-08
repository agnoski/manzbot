package app

class App {
    String getGreeting() {
        return 'Hello World!'
    }

    static void main(String[] args) {
        println new App().greeting

        System.setProperty("webdriver.gecko.driver", "/home/agno/Scaricati/geckodriver28");
        def browserBot = new BrowserBot()
        browserBot.start()
        def investersManager = new InvestersManager()
        println("Dirty job done...")
    }
}

/*
        Browser.drive {
          to MetaTrader 

          waitFor(20) { webTerminalIframe.displayed }
          if(cookiesBanner.displayed) { 
            closeCookiesBanner.click()
          }

          withFrame(webTerminalIframe) {
              waitFor(10) { buyButton.displayed }

//start button
              buyButton.click()
              
              waitFor(15) { checkTermsAndConditions.displayed }
              checkTermsAndConditions.click()
              acceptTermsAndConditions.click()
              sleep(5000) 
              buyButton.click()
              sleep(5000) 
              volumeInput.click()
              volumeInput << "8"
              buyButton.click()
              sleep(5000) 
              volumeInput.click()
              volumeInput << "4"
              sellButton.click()
              sleep(5000)
//end button
              symbolsButton.click()
              forex.click()
              hideSelectedSymbol.click()
              crypto.click()
              showSelectedSymbol.click()
              closeSymbols.click()
              sleep(5000)
              waitFor(5) { btcEur.displayed }
              btcEur.click()
              newOrderButton.click()
              waitFor(5) { orderVolumeInput.displayed }
              orderVolumeInput.click()
              orderVolumeInput << "8"
              sleep(1000)
              orderTPInput.click()
              orderTPInput << "16100"
              sleep(2000)
              orderBuyButton.click()
              sleep(2000)
              waitFor(5) { orderOkButton.displayed}
              orderOkButton.click()

          }
          sleep(5000)
        }
*/

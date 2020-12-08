package app

import geb.Browser
import org.openqa.selenium.Keys
import java.time.Instant

class BrowserBot extends Thread {
    private def browser

    BrowserBot() {
        this.browser = new Browser()
        this.browser = this.openWebTerminal()
        this.browser = this.setupIndexes()
    }

    private def openWebTerminal() {
        return Browser.drive(this.browser) {
          to MetaTrader 

          waitFor(20) { webTerminalIframe.displayed }
          if(cookiesBanner.displayed) { 
            closeCookiesBanner.click()
          }
        }
    }

    private def setupIndexes() {
        return Browser.drive(this.browser) {
            withFrame(webTerminalIframe) {
                waitFor(30) { buyButton.displayed }
                login()
                symbolsButton.click()
                forex.click()
                hideSelectedSymbol.click()
                crypto.click()
                showSelectedSymbol.click()
                closeSymbols.click()
            }
        }
    }

    def placeOrder(action) {
        return Browser.drive(this.browser) {
            withFrame(webTerminalIframe) {
                if(action.action == Action.Type.SELL || action.action == Action.Type.BUY) {
                    waitFor(5) { indexRow.displayed }
                    indexRow.click()
                    newOrderButton.click()
                    waitFor(5) { orderVolumeInput.displayed }
                    try {
                      orderVolumeInput.click()
                      orderVolumeInput << (action.parameters.volumeQuantity as String)
                      if(action.action == Action.Type.BUY) {
                        def priceTP = getBuyPriceTP(action.parameters.tpDelta) as String
                        orderTPInput.click()
                        orderTPInput << priceTP
                        orderBuyButton.click()
                      } else {
                        def priceTP = getSellPriceTP(action.parameters.tpDelta) as String
                        orderTPInput.click()
                        orderTPInput << priceTP
                        orderSellButton.click()
                      }
                      println("Well done: ${Instant.now()}")
                      sleep(2000)
                      def okButton = getButton("OK")
                      if(okButton) {
                        okButton.click()
                      } else {
                        println("Trying to close window")
                        orderCloseButton.click()
                      }
                    } catch(Exception e) {
                        println("Something went terribly wrong, closing the order window")
                        orderCloseButton.click()
                    }
                }
            }
        }
    }

    @Override
    void run() {
        while(true) {
            println("Ready to get an action")
            def action = ActionsService.getInstance().take()
            println("Got an action ${Instant.now()}, placing the order")
            this.browser = placeOrder(action)
        }
    }
}

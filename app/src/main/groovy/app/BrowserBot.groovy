package app

import geb.Browser
import org.openqa.selenium.Keys

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
                    orderVolumeInput.click()
                    orderVolumeInput << (action.parameters.volumeQuantity as String)
                    //sleep(1000)
                    //orderTPInput.click()
                    //orderTPInput << "16100"
                    //sleep(2000)
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
                    //sleep(2000)
                    waitFor(5) { orderOkButton.displayed || orderAcceptButton.displayed || orderRejectButton.displayed }
                    if(orderOkButton.displayed) {
                      orderOkButton.click()
                    } else {
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
            println("Got an action, placing the order")
            this.browser = placeOrder(action)
        }
    }
}

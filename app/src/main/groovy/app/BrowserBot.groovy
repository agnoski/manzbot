package app

import geb.Browser
import org.openqa.selenium.Keys
import java.time.Instant

class BrowserBot extends Thread {
    private def secondsFactor = 1000
    private def browser
    private def maxActionDeltaTime
    private def actionValidFunctions = [
        { action -> action.action in [Action.Type.SELL, Action.Type.BUY] },
        { action -> this.deltaTime(action) < this.maxActionDeltaTime }
    ]

    BrowserBot(config) {
        this.browser = new Browser()
        this.maxActionDeltaTime = config.maxActionDeltaTime * this.secondsFactor
        this.browser = this.openWebTerminal()
        this.browser = this.setupLogin(config.credentials)
        this.browser = this.setupSymbolsCategories(config.symbols)
    }

    private def deltaTime(action) {
        def actionDate = Date.from(action.time)
        def nowDate = Date.from(Instant.now())
        println("Now date: ${nowDate}")
        return nowDate.time - actionDate.time
    }

    private def openWebTerminal() {
        return Browser.drive(this.browser) {
          to MetaTrader 

          waitFor(20) {
            webTerminalIframe.displayed
          }

          closeCookiesBannerIfPresent()
        }
    }

    private def setupLogin(credentials) {
        return Browser.drive(this.browser) {
            withFrame(webTerminalIframe) {
                waitFor(30) { buyButton.displayed }
                login(credentials)
            }
        }
    }

    private def setupSymbolsCategories(categories) {
        return Browser.drive(this.browser) {
            withFrame(webTerminalIframe) {
                symbolsButton.click()
                categories.show.forEach {
                    clickIndexCategory(it)
                    clickButton("Show")
                }
                categories.hide.forEach {
                    clickIndexCategory(it)
                    clickButton("Hide")
                }
                clickButton("Close")
            }
        }
    }

    private def isActionValid(action) {
        return actionValidFunctions.every { func -> func(action) }
    }

    def placeOrder(action) {
        return Browser.drive(this.browser) {
            withFrame(webTerminalIframe) {
                if(isActionValid(action)) {
                    closeWindow()
                    newOrderButton.click()
                    waitFor(5) { orderVolumeInput.displayed }
                    try {
                      selectOrderSymbol(action.index.code)
                      orderVolumeInput.click()
                      orderVolumeInput << (action.parameters.volumeQuantity as String)
                      switch(action.action) {
                        case Action.Type.BUY:
                          def priceTP = getBuyPriceTP(action.parameters.tpDelta) as String
                          orderTPInput.click()
                          orderTPInput << priceTP
                          clickButton("Buy")
                          break
                        case Action.Type.SELL:
                          def priceTP = getSellPriceTP(action.parameters.tpDelta) as String
                          orderTPInput.click()
                          orderTPInput << priceTP
                          clickButton("Sell")
                          break
                        default:
                          println("Action ${action.action}: nothing to do")
                      }
                      def time = Instant.now()
                      def okButton = getButton("OK")
                      if(okButton) {
                        okButton.click()
                        println("Well done: $time")
                      } else {
                        println("Something has changed")
                        closeWindow()
                      }
                    } catch(Exception e) {
                        println("Something went terribly wrong, closing the order window")
                        closeWindow()
                    } finally {
                        println("Finally")
                        closeWindow()
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

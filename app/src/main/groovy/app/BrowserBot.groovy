package app

import geb.Browser
import org.openqa.selenium.Keys
import java.time.Instant

class BrowserBot extends Thread {
    private def secondsFactor = 1000
    private def version
    private def browser
    private def maxActionDeltaTime
    private def actionValidFunctions = [
        { action -> action.action in [Action.Type.SELL, Action.Type.BUY] },
        { action -> this.deltaTime(action) < this.maxActionDeltaTime }
    ]

    BrowserBot(config) {
        this.browser = new Browser()
        this.maxActionDeltaTime = config.maxActionDeltaTime * this.secondsFactor
        this.version = config.version
        this.browser = this.openWebTerminal()
        this.browser = this.selectMetaTraderPlatform()
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
          waitFor('slow') { webTerminalIframe.displayed }
          closeCookiesBannerIfPresent()
        }
    }

    private def selectMetaTraderPlatform() {
        return Browser.drive(this.browser) {
            withFrame(webTerminalIframe) {
                waitFor('slow') { buyButton.displayed }
                menuBar.file.click()
                if(!fileMenu.clickMetaTraderPlatform(this.version)) {
                  waitFor('slow') { loader.displayed }
                  waitFor('slow') { !loader.displayed }
                  waitFor('slow') { buyButton.displayed }
                }
                sleep(3000)
            }
        }
    }

    private def setupLogin(credentials) {
        return Browser.drive(this.browser) {
            withFrame(webTerminalIframe) {
                waitFor('slow') { buyButton.displayed }
                menuBar.file.click()
                fileMenu.clickLoginToTradeAccount()
                loginWindow.login(credentials)
                sleep(3000)
            }
        }
    }

    private def setupSymbolsCategories(categories) {
        return Browser.drive(this.browser) {
            withFrame(webTerminalIframe) {
                toolBar(this.version).symbols.click()
                sleep(3000)
                symbolsWindow.showOrHideCategories(categories)
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
                    try {
                      closeWindow()
                      toolBar(this.version).newOrder.click()
                      orderWindow(this.version).placeOrder(action)
                      sleep(5000)
                      def okButton = getButton("OK")
                      if(okButton) {
                        orderWindow(this.version).showOrderInfo()
                        okButton.click()
                        def time = Instant.now()
                        println("Well done: $time")
                      } else {
                        println("Something has changed")
                        closeWindow()
                      }
                    } catch(Exception e) {
                        println("Something went terribly wrong: $e, closing the order window")
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

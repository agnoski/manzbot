package app

import groovy.util.logging.Log
import geb.Browser
import org.openqa.selenium.Keys
import java.time.Instant

@Log
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
                        log.info("Probably well done!")
                      } else {
                        log.warning("Something has changed")
                        closeWindow()
                      }
                    } catch(Exception e) {
                        log.severe("Something went terribly wrong: $e, closing order window")
                        closeWindow()
                    } finally {
                        log.info("Finally, if order window still open, close it")
                        closeWindow()
                    }
                }
            }
        }
    }

    @Override
    void run() {
        while(true) {
            log.info("Ready to get an action")
            def action = ActionsService.getInstance().take()
            log.info("Got an action, placing the order")
            this.browser = placeOrder(action)
        }
    }
}

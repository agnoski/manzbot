package app

import geb.Page

class MetaTrader extends Page {
  static url = "https://www.mql5.com/en/trading" 

  static at = { title == "WebTerminal for the MetaTrader trading platform – Forex trading terminal" } 

  static content = {
    webTerminalIframe(page: WebTerminal) { $("iframe#webTerminalHost") }
    cookiesBanner { $("div#floatVerticalPanel") }
    closeCookiesBanner { $("div#floatVerticalPanel span.float-vertical-panel__cross") }
  }
}

package app

import geb.Module

class FileMenu extends Module {
  static content = {
    metaTrader { version -> $("span.label", text: "MetaTrader $version Platform") }
    loginToTradeAccount { $("span.label", text: "Login to Trade Account") }
  }

  void clickLoginToTradeAccount() {
    clickItem(loginToTradeAccount)
  }

  void clickMetaTraderPlatform(version) {
    clickItem(metaTrader(version))
  }

  private void clickItem(item) {
    waitFor('slow') { item.displayed }
    interact { moveToElement(item) }
    item.click()
  }
}

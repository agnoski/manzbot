package app

import geb.Module

class FileMenu extends Module {
  static content = {
    metaTrader { version -> $("span.children div.item span.label", text: "MetaTrader $version Platform") }
    loginToTradeAccount { $("span.children div.item span.label", text: "Login to Trade Account") }
  }

  void clickLoginToTradeAccount() {
    clickItem(loginToTradeAccount)
  }

  boolean clickMetaTraderPlatform(version) {
    def metaTraderPlatform = metaTrader(version)
    def checked = metaTraderPlatform.parent().hasClass("checked")

    clickItem(metaTraderPlatform)

    return checked
  }

  private void clickItem(item) {
    waitFor('slow') { item.displayed }
    //TODO why is interact-moveToElement needed and just using item.click() is not working?
    interact {
      moveToElement(item, -(item.width/2 + 1) as int, -(item.height/2 + 1) as int)
      click(item)
    }
  }
}

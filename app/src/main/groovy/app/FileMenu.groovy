package app

import geb.Module

class FileMenu extends Module {
  static content = {
    loginToTradeAccount { $("span.label", text: "Login to Trade Account") }
  }

  void clickLoginToTradeAccount() {
    clickItem(loginToTradeAccount)
  }

  private void clickItem(item) {
    waitFor('slow') { item.displayed }
    interact { moveToElement(item) }
    item.click()
  }
}

package app

import geb.Page

class WebTerminal extends Page {
  static content = {
    sellButton { $("div.input-trade-button")[0] }
    buyButton { $("div.input-trade-button")[1] }
    checkTermsAndConditions { $("#one-click-accept") }
    acceptTermsAndConditions { $("div.page-window div.w div.b button.input-button").find { it.displayed } }
    volumeInput { $("input#chart-one-click-volume") }

    symbolsButton { $("a.at-symbols-button") }
    newOrderButton { $("a.at-new-order-button") }

    orderVolumeInput { $("input#order-ie-dialog-volume") }
    orderTPInput { $("input#order-ie-dialog-tp") }
    orderSLInput { $("input#order-ie-dialog-sl") }
    orderValues { $("div.page-block div.page-block div.page-text span") }

    menuFile { $("div.page-menu div.menu div.first")[0].find(text: "File") }
    loginToTradeAccount { $("span.label", text: "Login to Trade Account") }
    loginInput { $("input#login") }
    passwordInput { $("input#password") }
  }

  def getButton(text) {
    def button = $("button.input-button", text: text)
    if(button) {
      return button.find { it.displayed }
    } else {
      return null
    }
  }

  def getWindowCloseButton() {
    def button = $("div.page-window.modal div.w div.wx span.i")
    if(button) {
      return button.find { it.displayed }
    } else {
      return null
    }
  }

  void closeWindow() {
    def windowCloseButton = getWindowCloseButton()
    if(windowCloseButton) {
      windowCloseButton.click()
      println("Tried to close window")
    } else {
      println("No widows to close")
    }
  }

  void clickButton(text) {
    def button = getButton(text)
    if(button) {
      button.click()
    }
    else {
      println("Button $text not found")
    }
  }
  
  def getIndexRow(index) { 
    return $("tr#$index td#symbol")
  }

  def getBuyPriceTP(delta) {
    def price = getBuyPrice() as BigDecimal
    println("Buy price: $price")
    return price + delta
  }

  def getSellPriceTP(delta) {
    def price = getSellPrice() as BigDecimal
    println("Sell price: $price")
    return price - delta
  }

  void clickIndexCategory(indexCategoryName) {
    def indexCategory = $("span.label", text: indexCategoryName)
    indexCategory.click()
  }

  void login(credentials) {
    menuFile.click()
    waitFor(5) { loginToTradeAccount.displayed }
    loginToTradeAccount.click()
    //loginToTradeAccount.click()
    println("Clicked login to trade account")
    sleep(3000)

    waitFor(5) { loginInput.displayed  }
    loginInput.click()
    loginInput.value(credentials.user)
    passwordInput.click()
    passwordInput.value(credentials.password)
    clickButton("OK")
    sleep(3000)
  }

  private def getBuyPrice() {
    return this.getPrices()[1]
  }

  private def getSellPrice() {
    return this.getPrices()[0]
  }

  private getPrices() {
    def text = orderValues.inject("") { acc, val ->
      acc + val.text()
    }
    println("Text: $text")

    def prices = text.split(" / ")
    println("Prices: $prices")
    return prices
  }
}

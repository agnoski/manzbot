package app

import geb.Page
import geb.module.Select

class WebTerminal extends Page {
  static content = {
    menuBar { module MenuBar }
    toolBar { module ToolBar }

    loginWindow { module LoginWindow }

    buyButton { $("div.input-trade-button")[1] }

    symbolsItems { $("div.items") }

    orderVolumeInput { $("input#order-ie-dialog-volume") }
    orderTPInput { $("input#order-ie-dialog-tp") }
    orderSLInput { $("input#order-ie-dialog-sl") }
    orderValues { $("div.page-block div.page-block div.page-text span") }

    loginToTradeAccount { $("span.label", text: "Login to Trade Account") }
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
  
  void selectOrderSymbol(index) {
    def orderSymbolSelect = $("#order-dialog-symbol").module(Select)
    orderSymbolSelect.selected = index
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
    waitFor('quick') { symbolsItems.displayed }
    def indexCategory = $("span.label", text: indexCategoryName)
    indexCategory.click()
  }

  void login(credentials) {
    menuBar.file.click()
    waitFor('slow') { loginToTradeAccount.displayed }
    interact { moveToElement(loginToTradeAccount) }
    loginToTradeAccount.click()
    loginWindow.login(credentials)
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

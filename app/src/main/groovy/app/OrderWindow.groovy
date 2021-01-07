package app

import geb.module.Select

class OrderWindow extends BaseWindow {
  static content = {
    orderSymbolSelect { $("#order-dialog-symbol").module(Select) }
    
    orderVolumeInput { $("input#order-ie-dialog-volume") }
    orderTPInput { $("input#order-ie-dialog-tp") }
    orderSLInput { $("input#order-ie-dialog-sl") }
    
    orderValues { $("div.page-block div.page-block div.page-text span") }
  }

  void placeOrder(action) {
    waitFor('quick') {
      orderVolumeInput.displayed
      orderTPInput.displayed
      orderSLInput.displayed
    }

    this.selectOrderSymbol(action.index.code)
    orderVolumeInput.click()
    orderVolumeInput << (action.parameters.volumeQuantity as String)
    switch(action.action) {
      case Action.Type.BUY:
        def priceTP = this.getBuyPriceTP(action.parameters.tpDelta) as String
        orderTPInput.click()
        orderTPInput << priceTP
        button("Buy").click()
        break
      case Action.Type.SELL:
        def priceTP = this.getSellPriceTP(action.parameters.tpDelta) as String
        orderTPInput.click()
        orderTPInput << priceTP
        button("Sell").click()
        break
      default:
        println("Action ${action.action}: nothing to do")
    }
  }

  private void selectOrderSymbol(index) {
    orderSymbolSelect.selected = index
  }

  private def getBuyPriceTP(delta) {
    def price = this.getBuyPrice() as BigDecimal
    println("Buy price: $price")
    return price + delta
  }

  private def getSellPriceTP(delta) {
    def price = this.getSellPrice() as BigDecimal
    println("Sell price: $price")
    return price - delta
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

package app

import geb.module.Select

class OrderWindow extends BaseWindow {
  String version

  static content = {
    orderSymbolSelect { version == "4" ? $("#symbol").module(Select) : $("#order-dialog-symbol").module(Select) }
    
    orderVolumeInput { version == "4" ? $("input#volume") : $("input#order-ie-dialog-volume") }
    orderTPInput { version == "4" ? $("input#tp") : $("input#order-ie-dialog-tp") }
    orderSLInput { version == "4" ? $("input#sl") : $("input#order-ie-dialog-sl") }
    
    orderValues { version == "4" ? $("div.w div.b div.page-text span") : $("div.page-block div.page-block div.page-text span") }
    orderInfo { version == "4" ? $("div.w div.b div.page-text", text: ~/.+/) : $("div.page-block div.page-block div.page-text", text: ~/.+/) }
  }

  void showOrderInfo() {
    if(orderInfo) {
      println("Order info (${orderInfo.size()}):")
      orderInfo.each {
        println("\t${it.text()}")
      }
    }
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

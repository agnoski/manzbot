package app

import groovy.util.logging.Log
import geb.module.Select

@Log
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
      log.info("Order info (${orderInfo.size()}):")
      orderInfo.each {
        log.info("--> ${it.text()}")
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
        log.warning("Unexpected action ${action.action}: nothing to do")
    }
  }

  private void selectOrderSymbol(index) {
    orderSymbolSelect.selected = index
  }

  private def getBuyPriceTP(delta) {
    def price = this.getBuyPrice() as BigDecimal
    def tpPrice = price + delta
    log.info("Buy price: $price -> TP price: ${tpPrice}")
    return tpPrice
  }

  private def getSellPriceTP(delta) {
    def price = this.getSellPrice() as BigDecimal
    def tpPrice = price - delta
    log.info("Sell price: $price -> TP price: ${tpPrice}")
    return tpPrice
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

    def prices = text.split(" / ")
    return prices
  }
}

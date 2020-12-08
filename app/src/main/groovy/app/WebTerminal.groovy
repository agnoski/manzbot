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
    forex { $("span.label").find { it.text() == "Forex" } }
    crypto { $("span.label").find { it.text() == "Crypto" } }
    showSelectedSymbol { $("button.input-button").find { it.displayed && it.text() == "Show" } }
    hideSelectedSymbol { $("button.input-button").find { it.displayed && it.text() == "Hide" } }
    closeSymbols { $("button.input-button").find { it.displayed && it.text() == "Close" } }

    indexRow { $("tr#EURUSD td#symbol") }

    orderVolumeInput { $("input#order-ie-dialog-volume") }
    orderTPInput { $("input#order-ie-dialog-tp") }
    orderSLInput { $("input#order-ie-dialog-sl") }
    orderSellButton { $("button.input-button").find { it.displayed && it.text() == "Sell" } }
    orderBuyButton { $("button.input-button").find { it.displayed && it.text() == "Buy" } }
    orderOkButton { $("button.input-button").find { it.displayed && it.text() == "OK" } }
    orderAcceptButton { $("button.input-button").find { it.displayed && it.text() == "Accept" } }
    orderRejectButton { $("button.input-button").find { it.displayed && it.text() == "Reject" } }
    orderCloseButton { $("div.page-window div.w div.wx").find { it.displayed } }
    orderValues { $("div.page-block div.page-block div.page-text span") }

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

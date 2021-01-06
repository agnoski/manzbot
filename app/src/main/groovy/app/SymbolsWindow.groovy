package app

import geb.Module

class SymbolsWindow extends Module {
  static content = {
    button { text -> $("button.input-button", text: text).find { it.displayed } }

    symbolsItems { $("div.items") }
    indexCategory { text -> $("span.label", text: text) }
  }

  void showOrHideCategories(categories) {
    waitFor('quick') { symbolsItems.displayed }

    categories.show.forEach {
      indexCategory(it).click()
      button("Show").click()
    }

    categories.hide.forEach {
      indexCategory(it).click()
      button("Hide").click()
    }

    button("Close").click()
  }
}

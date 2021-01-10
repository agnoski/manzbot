package app

class SymbolsWindow extends BaseWindow {
  static content = {
    symbolsItems { $("div.items") }
    indexCategory { text -> $("span.label", text: text).find { it.displayed } }
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

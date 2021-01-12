package app

class SymbolsWindow extends BaseWindow {
  static content = {
    symbolsItems { $("div.items") }
    indexCategory { text -> $("span.label", text: text).find { it.displayed } }
  }

  void showOrHideCategories(categories) {
    waitFor('quick') { symbolsItems.displayed }

    this.performAction(categories.show, "Show")
    this.performAction(categories.hide, "Hide")

    button("Close").click()
  }

  private void performAction(categories, action) {
    categories.forEach {
      indexCategory(it).click()
      button(action).click()
    }
  }
}

package app

import geb.Page

class HomePage extends Page {
  static url = "http://gebish.org" 

  static at = { title == "Geb - Very Groovy Browser Automation" } 

  static content = {
    manualsMenu { module(ManualMenu) } 
  }
}

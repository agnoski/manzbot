package app

import geb.Module

class LoginWindow extends Module {
  static content = {
    button { text -> $("button.input-button", text: text).find { it.displayed } }

    loginInput { $("input#login") }
    passwordInput { $("input#password") }
    serverInput { $("input#server") }
  }

  void login(credentials) {
    waitFor('quick') {
      loginInput.displayed
      passwordInput.displayed
      serverInput.displayed
    }

    loginInput.click()
    loginInput.value(credentials.user)

    passwordInput.click()
    passwordInput.value(credentials.password)

    serverInput.click()
    serverInput.value(credentials.server)

    button("OK").click()
  }
}

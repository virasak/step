package org.scalatra

import org.scalatest.matchers.ShouldMatchers

class ErrorHandlerTestServlet extends ScalatraServlet {
  get("/") {
    throw new RuntimeException
  }

  error {
    "handled " + caughtThrowable.getClass.getName
  }
}

class ErrorHandlerTest extends ScalatraSuite with ShouldMatchers {
  route(classOf[ErrorHandlerTestServlet], "/*")

  test("result of error handler should be rendered") {
    get("/") {
      body should equal ("handled java.lang.RuntimeException")
    }
  }

  test("response status should be set to 500 on error") {
    get("/") {
      status should equal (500)
    }
  }
}
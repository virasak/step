package org.scalatra

import org.scalatest.matchers.ShouldMatchers

class SessionTestServlet extends ScalatraServlet {
  get("/session") {
    session("val") match {
      case Some(v:String) => v
      case _ => "None"
    }
  }

  post("/session") {
    session("val") = params("val")
    session("val") match {
      case Some(v:String) => v
      case _ => "None"
    }
  }

  get("/session-option") {
    sessionOption map { _ => "Some" } getOrElse "None"
  }
}

class SessionTest extends ScalatraSuite with ShouldMatchers {
  route(classOf[SessionTestServlet], "/*")

  test("GET /session with no session should return 'None'") {
    get("/session") {
      body should equal ("None")
    }
  }

  test("POST /session with val=yes should return 'yes'") {
    post("/session", "val" -> "yes") {
      body should equal ("yes")
    }
  }

  test("GET /session with the session should return the data set in POST /session") {
    val data = "session_value"
    session {
      post("/session", "val" -> data) {
        body should equal (data)
      }
      get("/session") {
        body should equal (data)        
      }
    }
  }

  test("sessionOption should be None when no session exists") {
    session {
      get("/session-option") {
        body should equal ("None")
      }
    }
  }

  test("sessionOption should be Some when a session is active") {
    session {
      post("/session") {}

      get("/session-option") {
        body should equal("Some")
      }
    }
  }
}


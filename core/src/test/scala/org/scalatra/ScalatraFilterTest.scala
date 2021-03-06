package org.scalatra

import org.scalatest.matchers.ShouldMatchers

/*
 * There are four types of servlet mappings: path, extension, default, and exact-match.  Test them all, as they can all
 * cause different splits of pathInfo vs. servletPath.
 */

class ScalatraFilterTestFilter extends ScalatraFilter {
  get("/path-mapped/filtered") {
    "filter"
  }

  get("/filtered.do") {
    "filter"
  }

  get("/filtered") {
    "filter"
  }

  get("/exact-match/filtered") {
    "filter"
  }
}

class ScalatraFilterTestPathMappedServlet extends ScalatraServlet {
  get("/filtered") {
    "path-mapped"
  }

  get("/unfiltered") {
    "path-mapped"
  }
}

class ScalatraFilterTestExtensionMappedServlet extends ScalatraServlet {
  get("/filtered.do") {
    "extension-mapped"
  }

  get("/unfiltered.do") {
    "extension-mapped"
  }
}

class ScalatraFilterTestDefaultServlet extends ScalatraServlet {
  get("/filtered") {
    "default"
  }

  get("/unfiltered") {
    "default"
  }
}

class ScalatraFilterTestExactMatchServlet extends ScalatraServlet {
  get("/exact-match/filtered") {
    "exact match"
  }

  get("/exact-match/unfiltered") {
    "exact match"
  }
}

class ScalatraFilterTest extends ScalatraSuite with ShouldMatchers {
  routeFilter(classOf[ScalatraFilterTestFilter], "/*")

  // See SRV.11.2 of Servlet 2.5 spec for the gory details of servlet mappings
  route(classOf[ScalatraFilterTestPathMappedServlet], "/path-mapped/*")
  route(classOf[ScalatraFilterTestExtensionMappedServlet], "*.do")
  route(classOf[ScalatraFilterTestDefaultServlet], "/")
  route(classOf[ScalatraFilterTestExactMatchServlet], "/exact-match/filtered")
  route(classOf[ScalatraFilterTestExactMatchServlet], "/exact-match/unfiltered")

  test("should filter matching request to path-mapped servlet") {
    get("/path-mapped/filtered") {
      body should equal("filter")
    }
  }

  test("should pass through unmatched request to path-mapped servlet") {
    get("/path-mapped/unfiltered") {
      body should equal("path-mapped")
    }
  }

  test("should filter matching request to extension-mapped servlet") {
    get("/filtered.do") {
      body should equal("filter")
    }
  }

  test("should pass through unmatched request to extension-mapped servlet") {
    get("/unfiltered.do") {
      body should equal("extension-mapped")
    }
  }

  test("should filter matching request to default servlet") {
    get("/filtered") {
      body should equal("filter")
    }
  }

  test("should pass through unmatched request to default servlet") {
    get("/unfiltered") {
      body should equal("default")
    }
  }

  test("should filter matching request to exact-match-mapped servlet") {
    get("/exact-match/filtered") {
      body should equal("filter")
    }
  }

  test("should pass through unmatched request to exact-match-mapped servlet") {
    get("/exact-match/unfiltered") {
      body should equal("exact match")
    }
  }
}

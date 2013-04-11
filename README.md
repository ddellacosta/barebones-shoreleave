# getting-started-with-aws-s3

This is me learning how to access S3 via Clojure, and also get up and running with Shoreleave at the same time.

Too much at once?  We'll see.

## Notes

Shoreleave Baseline has a lot of libs it doesn't seem like I need.  Let's see if I can't just get it down to the bare minimum.

Using cljsbuild config from Shoreleave.  I like having my cljs in the same src dir as clj code.  And I like having output in `resources/assets/js`.

Alright, let's try to add some freaking code!  For now I'll just steal some stuff from shoreleave-baseline, how about?

```clojure
;; src/getting_started_with_aws_s3/client/main.cljs
(ns getting-started-with-aws-s3.main
  (:require-macros
   [shoreleave.remotes.macros :as srm]))

;; ### Confirm we have remote-calling activated
(srm/rpc
 (api/ping-the-api "Testing...") [pong-response]
 (js/alert pong-response))
```

...and, noting that you shouldn't use "cemerick.shoreleave.rpc" per [this issue](https://github.com/shoreleave/shoreleave-remote-ring/issues/6), in my Compojure server code:

```clojure
(ns getting-started-with-aws-s3.handler
  (:use
   compojure.core)
  (:require
   [shoreleave.middleware.rpc :refer [defremote wrap-rpc]]
   [compojure.handler :as handler]
   [compojure.route :as route]))

;; https://github.com/shoreleave/shoreleave-remote-ring
(defremote ping [pingback]
  (str "You have hit the API with: " pingback))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-rpc)
      (ring.middleware.anti-forgery/wrap-anti-forgery)
      (handler/site)))
```

Start it up with `lein ring server-headless`:

    Exception in thread "main" java.io.FileNotFoundException: Could not locate shoreleave/middleware/rpc__init.class or shoreleave/middleware/rpc.clj on classpath:

Hmm.  Okay, so, this

```clojure
[shoreleave "0.3.0"]
```

...doesn't include `shoreleave/middleware/rpc`. Let's try:

```clojure
[com.cemerick/shoreleave-remote-ring "0.0.2"]
```

    Exception in thread "main" java.io.FileNotFoundException: Could not locate shoreleave/middleware/rpc__init.class or shoreleave/middleware/rpc.clj on classpath: 

Huh? Hmm. Uh, okay, so it *has* been updated but the docs have not ([here](https://github.com/shoreleave/shoreleave-remote-ring/issues/6) and [here](https://github.com/shoreleave/shoreleave-remote-ring/issues/5)).  Let's try:

```clojure
[shoreleave/shoreleave-remote-ring "0.3.0"]]
```

There we go!

Okay, that's all well and fine but how does shoreleave-baseline actually allow you to trigger the JS?

Um...first you request a new page at "/test" with just the javascript loaded, and that triggers interaction with the server?

Hmm.

Let's try it a bit differently, something more similar to how a user may actually interact with the server via AJAX.

Our code was:

```clojure
(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/resources "/")
  (route/not-found "Not Found"))
```

We'll change it to:

```clojure
(defroutes app-routes
  (GET "/" [] "<a href='#' id='click'>Click me!</a>")
  (route/resources "/")
  (route/not-found "Not Found"))
```

Now let's wire that up via Clojurescript in a way that reflects how we may do this in another situation:

...TO BE CONTINUED...



## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server-headless

## License

Copyright © 2013 FIXME

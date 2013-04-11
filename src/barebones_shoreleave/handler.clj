(ns barebones-shoreleave.handler
  (:use
   compojure.core)
  (:require
   [shoreleave.middleware.rpc :refer [defremote wrap-rpc]]
   [ring.middleware.anti-forgery]
   [ring.middleware.session.cookie :refer [cookie-store]]
   [compojure.handler :as handler]
   [compojure.route :as route]
   [net.cgrand.enlive-html :as html]))

;; Enlive
(html/deftemplate main-layout
  "public/templates/index.html"
  [text]
  [:div#content] (html/html-content text))

;; https://github.com/shoreleave/shoreleave-remote-ring
(defremote ping [pingback]
  (str "You have hit the API with: " pingback))

(defroutes app-routes
  (GET "/" []
       (main-layout "<a href='#' id='click'>Click me!</a>"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      ring.middleware.anti-forgery/wrap-anti-forgery
      wrap-rpc
      handler/site))

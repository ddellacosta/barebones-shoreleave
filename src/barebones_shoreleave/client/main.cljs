(ns barebones-shoreleave.main
  (:use
   [jayq.core :only [$ bind]])
  (:require
   [shoreleave.remotes.http-rpc :as rpc])
  (:require-macros
   [shoreleave.remotes.macros :as srm])) ; https://github.com/shoreleave/shoreleave-remote

(def $click ($ :a#click))

(bind $click "click"
      ;; (js/alert "WE GOT A CLICK!")))
      (fn []
        ;; ### Confirm we have remote-calling activated
        (srm/rpc
         (ping "Testing...") [pong-response]
         (js/alert pong-response))))

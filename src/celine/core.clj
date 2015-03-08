(ns celine.core
	(:require 
		[clojure.tools.namespace.repl :refer [refresh]]
		[irclj.core :as irc]
		[celine.config :refer [config init-config]]
		[celine.network :refer [init-connection]]
		[celine.redis :refer [wcar*]]
		[taoensso.carmine :as car :refer (wcar)])
	(:use [clojure.pprint])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (reset! config (init-config))
  
  (let [conn (init-connection :hypeirc)]
  	(irc/join conn "#jason-test")
  	(irc/message conn "#jason-test" "hi")))
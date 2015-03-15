(ns celine.core
	(:require 
		[clojure.tools.namespace.repl :refer [refresh]]
		[irclj.core :as irc]
		[celine.plugins :refer [privmsg-event-handler register-plugin]]
		[celine.config :refer [config bots init-config!]]
		[celine.network :refer [init-connection ip-to-addr]]
		[celine.redis :refer [wcar*]]
		[celine.bot :as bot]
		[taoensso.carmine :as car :refer (wcar)])
	(:use [clojure.pprint])
	(:import java.net.InetAddress java.net.Inet4Address java.net.Inet6Address)
  (:gen-class))


(init-config!)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  ;(init-config!)
  ; (let [hypeirc (get @config :hypeirc)])

  ;(def conn (bot/new-bot (get-in @config [:networks :hypeirc])))
  (let [{:keys [server port nickname] :as network} (get-in @config [:networks :freenode])
  		ipaddr (ip-to-addr (InetAddress/getByName server))
  		conn (irc/connect ipaddr port nickname :callbacks {:privmsg privmsg-event-handler})]
  	(when conn
  		(irc/join conn "#celine-test")
  		(irc/message conn "#celine-test" "hi"))))

  ; (let [botz (bot/init-connections @config)
  ; (irc/join (get @bots :hypeirc) "#celine-test")
  ; (irc/message (get @bots :hypeirc) "#celine-test" "hi"))

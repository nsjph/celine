(ns celine.network
	(:require 
		[irclj.core :as irc]
		[celine.plugins :as plugins]
		[celine.config :refer [config]])
	(:import java.net.InetAddress java.net.Inet4Address java.net.Inet6Address))

(defn network-config [k]
	(get-in @config [:networks k]))

(defn networks [] (keys (:networks @config)))

(defn get-hosts [hostname]
	(InetAddress/getAllByName hostname))

(defn host-to-addr [host]
	(cond
		(instance? Inet4Address host) (.getHostAddress host)
		(instance? Inet6Address host) (str "[" (.getHostAddress host) "]")
		))

(defn host-to-addr-string [host]
	(let [addr (rand-nth (InetAddress/getAllByName host))]
		(cond 
			(instance? Inet4Address addr) (.getHostAddress addr)
			(instance? Inet6Address addr) (str "[" (.getHostAddress addr) "]"))))

(defn safe-connect [server port nickname callbacks]
	(println (str "Connecting to " server))
	(try 
		(irc/connect (host-to-addr server) port nickname :callbacks callbacks :timeout 10)
	(catch java.net.ConnectException e (println (str "Connection error: " (.getMessage e))))
	(catch Exception e (println (str "Connection error: " (.getMessage e))))))

(defn connect-to [servers port nickname callbacks]
	(if (empty? servers) 
		(throw (Exception. "No more servers to try"))
		(let [conn (safe-connect (first servers) port nickname callbacks)]
			(if conn
				conn
				(recur (rest servers) port nickname callbacks)))))


(defn init-connection [k & rest]
	(let [network (network-config k)
		hostname (network :server)
		servers (InetAddress/getAllByName hostname)
		nickname (@config :nickname)
		callbacks {
			:privmsg (fn [irc type & s] (prn irc type) (plugins/privmsg-event-handler irc type)) 
			:join (fn [irc type & s] (prn type s))}]

			(connect-to servers 6667 nickname callbacks)))

; (defn connect-to [k & rest]
; 	(let [network (network-config k)
; 		  host (network :server)
; 		  servers (InetAddress/getAllByName host)
; 		  server (host-to-addr-string (network :server))
; 		  nickname (@config :nickname)
; 		  callbacks {:privmsg (fn [irc type & s] (prn irc type) (plugins/privmsg-event-handler irc type))
; 		  	:join (fn [irc type & s] (prn type s))}]
; 		  (irc/connect server 6667 nickname :callbacks callbacks)))

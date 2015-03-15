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

(defn ip-to-addr [host]
	(cond
		(instance? Inet4Address host) (.getHostAddress host)
		(instance? Inet6Address host) (str "[" (.getHostAddress host) "]")
		))

(defn ip-to-addr-string [host]
	(let [addr (rand-nth (InetAddress/getAllByName host))]
		(cond 
			(instance? Inet4Address addr) (.getHostAddress addr)
			(instance? Inet6Address addr) (str "[" (.getHostAddress addr) "]"))))

(defn try-connect [ip port nickname callbacks]
	(println (str "Connecting to " ip))
	(try 
		(irc/connect (ip-to-addr ip) port nickname :callbacks callbacks :timeout 10)
	(catch java.net.ConnectException e (println (str "Connection error: " (.getMessage e))))
	(catch Exception e (println (str "Connection error: " (.getMessage e))))))

(defn connect-to [ipaddrs port nickname callbacks]
	(if (empty? ipaddrs) 
		(throw (Exception. "No more servers to try"))
		(let [conn (try-connect (first ipaddrs) port nickname callbacks)]
			(if conn
				conn
				(recur (rest ipaddrs) port nickname callbacks)))))


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

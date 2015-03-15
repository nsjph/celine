(ns celine.plugins
	(:require [me.raynes.fs :as fs]
		[irclj.core :as irc]
		[irclj.connection :as connection]
		[celine.config :refer [bots]]
		[celine.redis :refer [wcar*]]
		[taoensso.carmine :as car])
	(:gen-class))

(def plugins (atom {}))
(def prefix (atom "@"))

(defn starts-with-prefix? [msg prefix] (.startsWith msg prefix))

(defn register-plugin 
	[{:keys [event command handler] :as plugin}]
	(swap! plugins assoc event (assoc (get @plugins event) command handler)))

(defn privmsg-event-handler [conn {:keys [target host text nick] :as args}]
	(prn (args :raw))
	(when (true? (starts-with-prefix? text @prefix))
		(let [[command & cmdargs] (clojure.string/split text #"\s+")
			  [_ command] (clojure.string/split command #"@")]
			  (printf "cmd = [%s], args = %s\n" command cmdargs)
			  (when-let [handler (get-in @plugins [:privmsg command])]
			  	(handler conn args cmdargs))

			  )))

(def redis-set-plugin
	{:event :privmsg
	 :command "set"
	 :handler (fn [conn {:keys [nick user host] :as args} cmdargs]
	 	(let [k (first cmdargs) v (second cmdargs)]
	 		(printf "DEBUG: redis-set-plugin: setting %s to %s\n" k v)
	 		(when (wcar* (car/set k v))
	 			(irc/message conn nick (format "Set %s to %s" k v)))))})

(def redis-get-plugin
	{:event :privmsg
	 :command "get"
	 :handler (fn [conn {:keys [nick user host] :as args} cmdargs]
	 	(let [k (first cmdargs)]
	 		(printf "DEBUG: redis-get-plugin: getting %s\n" k)
	 			(irc/message conn nick (format "%s is %s" k (wcar* (car/get k))))))})


(register-plugin redis-set-plugin)
(register-plugin redis-get-plugin)
			
; (def addop-plugin
; 	{:prefix {"addop" (fn [conn {:keys [target text] :as args}]
; 		)}})

; (def auto-op 
; 	{:join {"auto-op" (fn [conn {:keys [params host user nick] :as args}]
; 		(let [channel (first params)]
; 			(when (some #(= % host) (get @opmasks channel))
; 				(irc/mode conn channel "+o" nick))))}})




(ns celine.plugins.admin
	(:require [celine.plugins :refer [register-plugin]]))

(def addop-plugin 
	{:event :privmsg 
	 :command "addop"
	 :handler (fn [conn {:keys [nick user host] :as from} cmdargs]
	(let [chanarg (first cmdargs) 
		  hostarg (second cmdargs)]
		  (prn chanarg hostarg)
	(swap! oplist assoc chanarg (conj (get @oplist chanarg) hostarg))
	(irc/message conn nick (format "Added %s\n" (str chanarg hostarg)))
	))})

(def listops-plugin
	{:event :privmsg
	 :command "listops"
	 :handler (fn [conn {:keys [nick user host] :as args} cmdargs]
	 	(prn cmdargs)
	 	(when-let [channel (first cmdargs)]
	 		(irc/message conn nick (get @oplist channel))))})

(register-plugin addop-plugin)
(register-plugin listops-plugin)
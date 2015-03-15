(ns celine.plugins.redis 
	(:require
		[celine.plugins :refer [register-plugin]]))

(def redis-set-plugin
	{:event :privmsg
	 :command "set"
	 :handler (fn [conn {:keys [nick user host] :as args} cmdargs]
	 	(let [k (first cmdargs) v (second cmdargs)]
	 		(printf "DEBUG: redis-set-plugin: setting %s to %s\n" k v)
	 		(when (wcar* (car/set k v))
	 			(irc/message conn nick (format "Set %s to %s" k v)))))})


(register-plugin redis-set-plugin)
(ns celine.plugins
	(:require [me.raynes.fs :as fs]
		[irclj.core :as irc]
		[irclj.connection :as connection])
	(:gen-class))

(def plugin-dir (fs/expand-home "~/.celine/plugins/"))
(def plugins (atom {}))
(def oplist (atom {})) ; list of strings, each a concatentated channel + user host, ie #example1.2.3.4
(def hostmasks (atom {}))
(def prefix (atom "@"))

(defn deep-merge
	"Deep merge two maps"
	[& values]
	(if (every? map? values)
		(apply merge-with deep-merge values)
		(last values)))

(defn merge-plugins [& args]
	(reduce deep-merge (map comp (list args))))

(defn init-plugin-dir []
	(when-not (true? (fs/exists? plugin-dir))
		(do
			(printf "Creating plugin directory %s\n" plugin-dir)
			(fs/mkdir plugin-dir))))

(defn starts-with-prefix? [msg prefix] (.startsWith msg prefix))

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

(defn add-plugin 
	[{:keys [event command handler] :as plugin}]
	(swap! plugins assoc event (assoc (get @plugins event) command handler)))

(defn privmsg-event-handler [conn {:keys [target host text nick] :as args}]
	(when (true? (starts-with-prefix? text @prefix))
		(let [[command & cmdargs] (clojure.string/split text #"\s+")
			  [_ command] (clojure.string/split command #"@")]
			  (printf "cmd = [%s], args = %s\n" command cmdargs)
			  (when-let [handler (get-in @plugins [:privmsg command])]
			  	(handler conn args cmdargs))

			  )))

(add-plugin addop-plugin)
(add-plugin listops-plugin)			
; (def addop-plugin
; 	{:prefix {"addop" (fn [conn {:keys [target text] :as args}]
; 		)}})

; (def auto-op 
; 	{:join {"auto-op" (fn [conn {:keys [params host user nick] :as args}]
; 		(let [channel (first params)]
; 			(when (some #(= % host) (get @opmasks channel))
; 				(irc/mode conn channel "+o" nick))))}})




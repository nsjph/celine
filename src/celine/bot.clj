(ns celine.bot
	(:require  
		[irclj.core :as irc]
		[celine.plugins :refer [privmsg-event-handler]]
		[celine.config :refer [config bots]]
		[celine.network :refer [connect-to try-connect]])
	(:import java.net.InetAddress java.net.Inet4Address java.net.Inet6Address))

; (defn get-bot [network] (get @bots network))

(defn new-bot2 [bot-config]
	(let [network (first bot-config)
		{:keys [channels server port nickname] :as network-config} (second bot-config)
		ipaddrs (InetAddress/getAllByName server)]
		(connect-to ipaddrs port nickname privmsg-event-handler)))

(defn new-bot [{:keys [server port nickname] :as network}]
	(let [ipaddrs (InetAddress/getAllByName server)]
		(try-connect (first ipaddrs) port nickname privmsg-event-handler)))

(defn init-connections [{:keys [networks] :as config}]
	(map new-bot2 networks))
	;(reset! bots (apply merge (map new-bot networks))))
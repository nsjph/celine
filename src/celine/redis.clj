(ns celine.redis
	(:require 
		[taoensso.carmine :as car :refer (wcar)]))

(def conn-opts (atom {:pool {} :spec {:host "127.0.0.1" :port 6379}}))
(defmacro wcar* [& body] `(car/wcar @conn-opts ~@body))
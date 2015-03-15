(ns celine.config
	(:require 
		[me.raynes.fs :as fs]
		[clojure.java.io :as io]
		[clojure.string :as string])
	(:use 
		[clojure.pprint]))

(def config-dir (atom (fs/expand-home "~/.celine/")))
(def config-path (str @config-dir "/celine.conf"))
(def config-defaults-path (io/resource "config-defaults.edn"))
(def config (atom {}))
(def bots (atom {}))

(defn read-config [path]
	(read-string (slurp path)))

(defn load-config []
	(if (fs/exists? config-path)
		(read-config config-path)
		(read-config config-defaults-path)))

(defn save-current-config []
	(when (true? (fs/exists? config-path))
		(do 
			(printf "Saving current config to %s\n" config-path)
			(spit config-path (with-out-str (pprint @config))))))

(defn init-config-dir [dir]
	(when-not (true? (fs/exists? dir))
		(do 
			(printf "Creating config directory %s\n" dir)
			(fs/mkdir dir))))


(defn init-config-file [] 
	(when-not (true? (fs/exists? config-path))
		(do 
			(printf "Saving default config to %s\n" config-path)
			(spit config-path (with-out-str (pprint (read-config config-defaults-path)))))))



(defn init-config! []
	(init-config-dir @config-dir)
	(init-config-file)
	(reset! config (load-config)))
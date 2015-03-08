(defproject celine "0.1.0-SNAPSHOT"
  :description "A simple IRC bot"
  :url "https://github.com/nsjph/celine"
  :license {:name "MIT"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
  	[irclj "0.5.0-alpha4"]
  	[me.raynes/fs "1.4.6"]
  	[com.taoensso/timbre "3.4.0"]
  	[org.clojure/tools.namespace "0.2.10"]
    [clj-http "1.0.1"]
    [pandect "0.5.1"]
    [com.taoensso/carmine "2.9.0"]]
  :main ^:skip-aot celine.core
  ;:main celine.core
  :target-path "target/%s"
  :profiles {:uberjar {:main celine.core :aot :all}})
  	; :bin-path "~/bin"
  	; :bootclasspath true})

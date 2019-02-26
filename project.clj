(defproject random-homework "0.1.0-SNAPSHOT"
  :description "Just some random homework..."
  :dependencies [;core deps
                 [org.clojure/clojure "1.10.0"]
                 [clojure.java-time "0.3.2"]

                 ; web service deps
                 [cheshire "5.8.1"]
                 [compojure "1.6.1"]
                 [ring/ring-jetty-adapter "1.7.1"]]

  :main ^:skip-aot random-homework.core
  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/test.check "0.9.0"]]}}
  :plugins [[lein-cloverage "1.0.13"]]
  )

(defproject random-homework "0.1.0-SNAPSHOT"
  :description "Just some random homework..."
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clojure.java-time "0.3.2"]]

  :main ^:skip-aot random-homework.core
  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/test.check "0.9.0"]]}})

(ns random-homework.gen-files
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as string]
            [random-homework.parse :as parse]))


(defn gen-single-file
  [file delim row-count]
  (with-open [wr (io/writer file)]
    (doseq [rec (gen/sample (spec/gen :person/record) row-count)]
      (doto wr
        (.write (string/join delim rec))
        (.write "\n")))))


(defn gen-files
  [row-count]
  (doseq [[delim-name delim] parse/delims]
    (gen-single-file (io/file "test-files" (str delim-name ".csv"))
                     delim
                     row-count)))


(defn -main
  []
  (gen-files 1000))













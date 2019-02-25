(ns random-homework.core
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as spec])
  (:import [java.io File])
  (:gen-class))


(defn valid-file-path?
  [path]
  (let [file (io/file path)]
    (and (.exists file)
         (.isFile file))))


(def delims
  {"space" " "
   "comma" ","
   "pipe" "|"})

(def sort-by-fields #{"last-name" "birth-date" "gender"})


(spec/def ::cli-args
  (spec/cat :file (spec/and string? valid-file-path?)
            :delim (spec/cat :flag #{"--delim"}
                             :delim (into #{} (keys delims)))
            :sort-by (spec/cat :flag #{"--sort-by"}
                               :field sort-by-fields)))


(defn parse-args
  [args]
  (let [{file :file {delim :delim} :delim {sortby :field} :sort-by} (spec/conform ::cli-args args)]
    {:op/file (io/file file)
     :op/delim (get delims delim)
     :op/sort-by sortby}))


(spec/def :op/file #(instance? File %))
(spec/def :op/delim (into #{} (vals delims)))
(spec/def :op/sort-by sort-by-fields)

(spec/fdef parse-args
           :args (spec/cat :args (spec/spec ::cli-args))
           :ret (spec/keys :req [:op/file :op/delim :op/sort-by]))


(def usage "Usage: lein run <file-path> --delim <delim> --sort-by <sort-by>
           delim:   comma | space | pipe
           sort-by: last-name | birth-date | gender")

(defn -main
  [& args]
  (if (spec/valid? ::cli-args args)
    ()
    (println usage)))
